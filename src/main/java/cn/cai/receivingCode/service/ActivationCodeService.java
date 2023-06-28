package cn.cai.receivingCode.service;

import cn.cai.receivingCode.model.ActivationCode;
import cn.cai.receivingCode.model.request.PageRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
public interface ActivationCodeService extends IService<ActivationCode> {

    /**
     * 生成一个唯一的激活码
     * @return 唯一的激活码
     */
    String generate();

    /**
     * 校验验证码是否有效
     * @param code 验证码
     * @return true:有效;false:无效
     */
    Boolean isEffective(String code);

    /**
     * 根据验证码删除
     * @param code 验证码
     */
    void delete(String code);

    /**
     * 获取缓存中存储的key
     * @return 所有的key
     */
    List<String> getCache();

    /**
     * 注册
     * @param code 激活码
     * @param request 请求体
     */
    void registration(String code, HttpServletRequest request);

    /**
     * 获取所有已经注册的数据
     * @return 所有已经注册的数据
     */
    Page<ActivationCode> getAllEnroll(PageRequest page);

    /**
     * 删除Redis缓存服务器的数据
     */
    Long deleteAllRedis();

}
