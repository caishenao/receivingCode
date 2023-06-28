package cn.cai.receivingCode.controller;

import cn.cai.receivingCode.commont.Result;
import cn.cai.receivingCode.model.ActivationCode;
import cn.cai.receivingCode.model.request.PageRequest;
import cn.cai.receivingCode.service.ActivationCodeService;
import cn.cai.receivingCode.utils.IpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/activationCode")
public class ActivationCodeController {


    @Autowired
    private ActivationCodeService activationCodeService;

    /**
     * 生成一个激活码，存储到redis中并返回
     */
    @GetMapping(value = "/generate")
    public Result<String> generate() {
        String result = activationCodeService.generate();
        return Result.success(result);
    }

    /**
     * 验证激活码是否有效（在redis中）
     * @param code 激活码
     */
    @GetMapping("/isEffective/{code}")
    public Result<Boolean> isEffective(@PathVariable("code") String code){
        boolean isEffective = activationCodeService.isEffective(code);
        return Result.success(isEffective);
    }

    /**
     * 删除激活吗
     * @param code 删除激活码
     */
    @DeleteMapping("/delete/{code}")
    public Result<Void> delete(@PathVariable("code") String code) {
        activationCodeService.delete(code);
        return Result.success();
    }

    /**
     * 清空redis缓存
     */
    @DeleteMapping("/deleteAllRedis")
    public Result<Long> deleteAllRedis(){
        Long length = activationCodeService.deleteAllRedis();
        return Result.success(length);
    }

    /**
     * 获取所有已经注册的数据
     * @return 所有已经注册的数据
     */
    @GetMapping("/getAllEnroll")
    public Result<Page<ActivationCode>> getAllEnroll(PageRequest page) {
        Page<ActivationCode> list = activationCodeService.getAllEnroll(page);
        return Result.success(list);
    }


    /**
     * 获取缓存中的所有key
     */
    @GetMapping("/getCache")
    public Result<List<String>> getCache(){
        List<String> codeList = activationCodeService.getCache();
        return Result.success(codeList);
    }


    /**
     * 激活码激活
     * @param code 激活码
     * @param request 请求体
     */
    @PostMapping("/registration/{code}")
    public Result<Void> registration(@PathVariable("code") String code, HttpServletRequest request) {
        activationCodeService.registration(code,request);
        return Result.success();
    }
    /**
     * 获取IP
     */
    @GetMapping("/getIP")
    public Result<?> getIP(HttpServletRequest request) {
        String ipAddr = IpUtil.getIpAddr(request);
        return Result.success(ipAddr);
    }
}
