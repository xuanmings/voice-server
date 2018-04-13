package com.mifa.cloud.voice.server.controller;

import com.google.common.net.HttpHeaders;
import com.mifa.cloud.voice.server.annotation.Loggable;
import com.mifa.cloud.voice.server.commons.constants.AppConst;
import com.mifa.cloud.voice.server.commons.dto.CommonResponse;
import com.mifa.cloud.voice.server.commons.dto.PageDto;
import com.mifa.cloud.voice.server.commons.dto.VoiceCategoryDto;
import com.mifa.cloud.voice.server.service.TemplateCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: songxm
 * @date: 2018/4/13 10:11
 * @version: v1.0.0
 */
@RestController
@CrossOrigin
@Api(value = "模板类目相关API.", tags = "模板类目API", description = "模板类目的添加,审核,测试,维护功能")
@RequestMapping(AppConst.BASE_AUTH_PATH + "v1")
public class TemplateCategoryController {

    @Autowired
    TemplateCategoryService templateCategoryService;

    @ApiOperation("查询模板类目列表")
    @RequestMapping(value = "/template/category-list", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", name = HttpHeaders.AUTHORIZATION, required = true, value = "service token", dataType = "string", defaultValue = AppConst.SAMPLE_TOKEN)
    })
    @Loggable(descp = "查询模板类目列表")
    public CommonResponse<PageDto<VoiceCategoryDto>> findTemplateCategory(@RequestParam(required = false, defaultValue = "0") Integer pid, @RequestParam(required = false) String contanctNo, @RequestParam(required = true, defaultValue = "1") Integer pageNum, @RequestParam(required = true,defaultValue = "3") Integer pageSize) {
        return templateCategoryService.queryVoiceCategoryList(pid, contanctNo,pageNum,pageSize);
    }
}