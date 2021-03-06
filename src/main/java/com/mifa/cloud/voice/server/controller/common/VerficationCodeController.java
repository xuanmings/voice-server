package com.mifa.cloud.voice.server.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.mifa.cloud.voice.server.LocalCache;
import com.mifa.cloud.voice.server.annotation.Loggable;
import com.mifa.cloud.voice.server.commons.constants.AppConst;
import com.mifa.cloud.voice.server.commons.dto.CommonResponse;
import com.mifa.cloud.voice.server.commons.enums.MQMsgEnum;
import com.mifa.cloud.voice.server.component.RandomSort;
import com.mifa.cloud.voice.server.component.redis.KeyValueDao;
import com.mifa.cloud.voice.server.config.StaticConst;
import com.mifa.cloud.voice.server.commons.dto.MobileVerficationCodeDTO;
import com.mifa.cloud.voice.server.commons.dto.MobileAuthCodeVerifyDTO;
import com.mifa.cloud.voice.server.commons.dto.UserPwdImgCodeDTO;
import com.mifa.cloud.voice.server.service.VerficationService;
import com.mifa.cloud.voice.server.utils.ImageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "验证码", description = "验证码", produces = MediaType.APPLICATION_JSON)
@Slf4j
@RequestMapping(AppConst.BASE_PATH + "v1")
public class VerficationCodeController {


    @Autowired
    private KeyValueDao keyValueDao;

    @Autowired
    private ImageUtil imageUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VerficationService verficationService;

    @GetMapping(value = "/img-auth-code/")
    @ApiOperation(value = "图形验证码", notes = "图形验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号码", required = true, paramType = "query")})
    @Loggable(descp = "获得图形验证码")
    public void imageVerficationCode(HttpServletRequest request, HttpServletResponse response){
        String mobilePhone = request.getParameter("mobile");
        if(StringUtils.isEmpty(mobilePhone)){
            log.error("获取图形验证码时mobilePhone参数为空");
            return;
        }

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        response.setContentType("image/jpeg");
        try {
            int w = 92, h = 40;
            String verifyCode = imageUtil.generateVerifyCode(4);
            log.info("手机号：{}，图形验证码：{}", mobilePhone, verifyCode);
            //保存验证码
            keyValueDao.set(StaticConst.IMG_IDENTIFY_CODE + mobilePhone,verifyCode,60*3);
            imageUtil.outputImage(w, h, response.getOutputStream(), verifyCode);
        } catch (IOException e) {
            log.error("输出图形验证码异常："+e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/mobile-auth-code/")
    @ApiOperation(value = "短信验证码", notes = "短信验证码")
    @Loggable(descp = "获得短信验证码")
    public CommonResponse<Void> mobileVerficationCode(@RequestBody @Valid MobileVerficationCodeDTO param) {
        // 判断是否符合发送条件
        if(LocalCache.hasRegisterRequest(param.getMobile())) {
            return CommonResponse.failCommonResponse();
        }
        String code = String.valueOf(RandomSort.generateRandomNum(6));
        keyValueDao.set(StaticConst.MOBILE_SMS_KEY + param.getMobile(), code, 60 * 3);
        Map<String, String> map = new HashMap<String, String>();
        map.put("bizType", "1");
        map.put("aliasName", "midunyuyinyun");
        map.put("tel", param.getMobile());
        map.put("identifyingCode", code);
        String json = JSONObject.toJSONString(map);
        log.info("send msg：" + json);
        rabbitTemplate.convertAndSend(MQMsgEnum.MOBILE_AUTH_CODE.getCode(), json);
        return CommonResponse.successCommonResponse();
    }

    @PostMapping("/auth-code/mobile")
    @ApiOperation(value = "校验手机验证码")
    @Loggable(descp = "校验手机验证码")
    public CommonResponse<Void> verifyMobileCode(@RequestBody @Valid MobileAuthCodeVerifyDTO param) {

        // 校验短信验证码
        String mobileAuthCode = verficationService.getmobileAuthCodeFromCache(param.getMobile());
        if(StringUtils.isEmpty(mobileAuthCode)) {
            return CommonResponse.failCommonResponse("手机验证码已过期，请重新获取");
        }
        if(!mobileAuthCode.equals(param.getMobileVerficationCode())) {
            return CommonResponse.failCommonResponse("验证码错误");
        }
        return CommonResponse.successCommonResponse();

    }

    @PostMapping("/auth-code/img")
    @ApiOperation(value = "校验图片验证码")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", name = HttpHeaders.AUTHORIZATION,
            required = true, value = "service token", dataType = "string")
    })
    /*@RequestHeader(HttpHeaders.AUTHORIZATION) String token*/
    @Loggable(descp = "校验图片验证码")
    public CommonResponse<Void> retrievePasswordVerifyImgCode(@RequestBody @Valid UserPwdImgCodeDTO param) {

        // 根据手机号获取缓存中的图片验证码
        String imgIdentifyCode = (String) keyValueDao.get(StaticConst.IMG_IDENTIFY_CODE + param.getMobile());
        if(StringUtils.isEmpty(imgIdentifyCode)) {
            return CommonResponse.failCommonResponse("图片验证码已过期，请重新获取");
        }
        if(!param.getImageVerficationCode().equalsIgnoreCase(imgIdentifyCode)) {
            return CommonResponse.failCommonResponse("验证码错误");
        }
        return CommonResponse.successCommonResponse();

    }


}
