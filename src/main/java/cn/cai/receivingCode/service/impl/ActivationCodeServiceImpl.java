package cn.cai.receivingCode.service.impl;


import cn.cai.receivingCode.mapper.ActivationCodeMapper;
import cn.cai.receivingCode.model.ActivationCode;
import cn.cai.receivingCode.model.request.PageRequest;
import cn.cai.receivingCode.service.ActivationCodeService;
import cn.cai.receivingCode.utils.IpUtil;
import cn.cai.receivingCode.utils.RandomUtils;
import cn.cai.receivingCode.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ActivationCodeServiceImpl extends ServiceImpl<ActivationCodeMapper, ActivationCode> implements ActivationCodeService{

    @Autowired
    private ActivationCodeMapper activationCodeMapper;
    @Autowired
    private RedisCache redisCache;

    @Value("${activationCode.overdue}")
    private Integer overdue;
    @Value("${activationCode.length}")
    private Integer length;
    @Value("${activationCode.export}")
    private Long export;

    /**
     * 删除Redis缓存服务器的数据
     */
    @Override
    public Long deleteAllRedis() {
        Set<String> keys = redisCache.redisTemplate.keys("*");
        return redisCache.deleteObject(keys);
    }

    /**
     * 生成唯一的激活码
     * @return 生成唯一的激活码
     */
    @Override
    public String generate() {
        String randomStr;
        do {
            // 1. 随机生成一个唯一的字符保证唯一
            randomStr = RandomUtils.randomNum(length);
        }while (!isSole(randomStr));
        // 2. 存储进redis服务器中
        redisCache.setCacheObject(randomStr,randomStr,overdue, TimeUnit.MINUTES);
        // 3. 返回唯一的字符串
        return randomStr;
    }

    /**
     * 验证激活码是否有效
     * @param code 验证码
     * @return 是否有效 true:有效;false:无效
     */
    @Override
    public Boolean isEffective(String code) {
        String randomStr = redisCache.getCacheObject(code);
        return StringUtils.isNotBlank(randomStr);
    }

    /**
     * 根据验证码删除
     * @param code 验证码
     */
    @Override
    public void delete(String code) {
        // 1. 删除redis中的key
        redisCache.deleteObject(code);
        // 2. 删除数据库记录
        activationCodeMapper.deleteById(code);
    }

    /**
     * 获取缓存中存储的key
     * @return 所有的key
     */
    @Override
    public List<String> getCache() {
        Set<String> keys = redisCache.redisTemplate.keys("*");
        List<String> all = new ArrayList<>();
        all.addAll(keys);
        return all;
    }

    /**
     * 激活码注册
     * @param code 激活码
     * @param request 请求体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registration(String code, HttpServletRequest request) {
        // 1. 验证注册码是否有效
        String activationCode = redisCache.getCacheObject(code);
        if (StringUtils.isBlank(activationCode)) {
            throw new RuntimeException("验证码失效!");
        }
        // 2. 获取用户ip
        String ipAddr = IpUtil.getIpAddr(request);
        // 3. 添加到数据库中
        ActivationCode addCode = new ActivationCode();
        addCode.setActivationCode(activationCode);
        addCode.setIp(ipAddr);
        addCode.setCreateTime(LocalDateTime.now());
        addCode.setUpdateTime(LocalDateTime.now());
        LocalDateTime overdueTime = LocalDateTime.now().plus(export, ChronoUnit.DAYS);
        addCode.setOverdueTime(overdueTime);
        addCode.setVerifyTime(LocalDateTime.now());
        addCode.setRegistrationTime(LocalDateTime.now());

        // 4. 保存到数据库
        activationCodeMapper.insert(addCode);

        // 5. 清除缓存
        redisCache.deleteObject(code);
    }

    /**
     * 获取所有已经注册的数据
     * @param page 分页请求
     * @return 所有已经注册的数据
     */
    @Override
    public Page<ActivationCode> getAllEnroll(PageRequest page) {
        Page<ActivationCode> activationCodePage = new Page<>(page.getPageNumber(),page.getPageSize());
        LambdaQueryWrapper<ActivationCode> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderBy(true,false,ActivationCode::getRegistrationTime);
        return activationCodeMapper.selectPage(activationCodePage,lambdaQueryWrapper);
    }


    /**
     * 验证传入的字符串是否唯一
     * @param randomStr 产生的随机字符串
     * @return 是否唯一 true: 唯一；false:不唯一
     */
    private boolean isSole(String randomStr) {
        // 1. 验证redis中是否存在
        Object cacheObject = redisCache.getCacheObject(randomStr);
        if (null != cacheObject) {
            return false;
        }
        // 2. 验证mysql中是否存在
        ActivationCode activationCode = activationCodeMapper.selectById(randomStr);
        return null == activationCode;
    }
}
