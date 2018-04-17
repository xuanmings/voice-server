package com.mifa.cloud.voice.server.controller.user;

import com.mifa.cloud.voice.server.annotation.Loggable;
import com.mifa.cloud.voice.server.commons.constants.AppConst;
import com.mifa.cloud.voice.server.commons.dto.CommonResponse;
import com.mifa.cloud.voice.server.dto.UserLoginDTO;
import com.mifa.cloud.voice.server.dto.UserLoginVO;
import com.mifa.cloud.voice.server.pojo.CustomerLoginInfo;
import com.mifa.cloud.voice.server.service.CustomerLoginInfoService;
import com.mifa.cloud.voice.server.utils.JwtTokenUtil;
import com.mifa.cloud.voice.server.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

/**
 * Created by Administrator on 2018/4/10.
 */
@RestController
@Api(value = "用户登陆",description = "用户登陆",produces = MediaType.APPLICATION_JSON)
@Slf4j
@RequestMapping(AppConst.BASE_PATH + "v1")
public class LoginController {

    @Autowired
    private CustomerLoginInfoService loginInfoService;

    @Autowired
    private PasswordUtil passwordUtil;

    @PostMapping("/user-login")
    @ApiOperation(value = "登陆")
    @Loggable(descp = "用户登陆")
    public CommonResponse<UserLoginVO> login(@RequestBody @Valid UserLoginDTO param) {

        CustomerLoginInfo loginInfo = null;
        // 判断登陆类型，手机号登陆或用户名登陆
        String mobileReg = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        if(param.getLoginName().matches(mobileReg)) {
            loginInfo = loginInfoService.findByLoginMobile(param.getLoginName());
        }else {
            // 用户名登陆
            loginInfo = loginInfoService.findByLoginName(param.getLoginName());
        }
        // 校验用户是否存在
        if(loginInfo == null) {
            return CommonResponse.failCommonResponse("用户名不存在");
        }
        // 校验密码是否正确
        boolean verifyFlag = passwordUtil.verify(param.getLoginPasswd(), loginInfo.getLoginPasswd(), loginInfo.getSalt());
        if(!verifyFlag) {
            return CommonResponse.failCommonResponse("密码错误");
        }

        //修改登陆时间、登陆ip
        loginInfo.setLastLoginTime(System.currentTimeMillis());
        loginInfo.setLastLoginIp(StringUtils.isNotEmpty(param.getLastLoginIp()) ? param.getLastLoginIp() : loginInfo.getLastLoginIp());
        loginInfoService.updateByPrimaryKeySelective(loginInfo);

        // 生成token返回
        String token = JwtTokenUtil.createToken(loginInfo.getContractNo(), 30);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .contractNo(loginInfo.getContractNo())
                .token(token)
                .build();

        return CommonResponse.successCommonResponse(userLoginVO);
    }


}
