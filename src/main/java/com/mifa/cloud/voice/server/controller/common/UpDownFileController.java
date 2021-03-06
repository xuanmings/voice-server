package com.mifa.cloud.voice.server.controller.common;


import com.mifa.cloud.voice.server.annotation.Loggable;
import com.mifa.cloud.voice.server.commons.constants.AppConst;
import com.mifa.cloud.voice.server.commons.dto.CommonResponse;
import com.mifa.cloud.voice.server.commons.enums.FileStatusEnum;
import com.mifa.cloud.voice.server.commons.enums.FileTypeEnums;
import com.mifa.cloud.voice.server.commons.enums.BizTypeEnums;
import com.mifa.cloud.voice.server.config.ConstConfig;
import com.mifa.cloud.voice.server.commons.dto.DownLoadFileVO;
import com.mifa.cloud.voice.server.commons.dto.UploadFileVO;
import com.mifa.cloud.voice.server.pojo.UploadFileLog;
import com.mifa.cloud.voice.server.service.UploadFileLogService;
import com.mifa.cloud.voice.server.utils.UploadFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Api(value = "文件管理", description = "文件管理", produces = MediaType.APPLICATION_JSON)
@Slf4j
@RequestMapping(AppConst.BASE_AUTH_PATH + "v1")
public class UpDownFileController {

    @Autowired
    private ConstConfig aconst;
    @Autowired
    private UploadFileUtil uploadFileUtil;
    @Autowired
    private UploadFileLogService uploadFileLogService;


    @PostMapping(value = "/upload-file")
    @ApiOperation(value = "单个上传文件", notes = "单个上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = HttpHeaders.AUTHORIZATION,
                    required = true, value = "service token", dataType = "string")
    })
    @Loggable(descp = "单个上传文件")
    public CommonResponse<UploadFileVO> uploadFileCompress(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileTypeEnums fileType,
            @RequestParam("bizType") BizTypeEnums bizType,
            @RequestParam("contractNo") String contractNo,
            @RequestParam(value = "groupId", required = false) String groupId
    ) throws Exception {
        String originalFilename = file.getOriginalFilename();
        boolean flag = uploadFileUtil.fileTypeReg(fileType, originalFilename);
        if (!flag) return CommonResponse.failCommonResponse("请选择正确的格式");
        return CommonResponse.successCommonResponse(uploadFileUtil.upload(file, fileType, bizType, contractNo, groupId, aconst));
    }

    @GetMapping("/download-file")
    @ApiOperation(value = "下载文件", notes = "下载文件")
    @Loggable(descp = "下载文件")
    public CommonResponse<DownLoadFileVO> downLoadFile(
            @RequestParam("fileType") FileTypeEnums fileType,
            @RequestParam("bizType") BizTypeEnums bizType) {

        List<UploadFileLog> uploadFileLogs = uploadFileLogService.selectByFileTypeAndBizType(fileType.name(), bizType.name(), FileStatusEnum.EFFECTIVE.getCode());
        if (uploadFileLogs.isEmpty()) {
            return CommonResponse.failCommonResponse("文件不存在");
        }
        DownLoadFileVO vo = DownLoadFileVO.builder()
                .fileUrl(uploadFileLogs.get(0) != null ? aconst.H5_URL_PATH + uploadFileLogs.get(0).getFileUrl() : "")
                .build();
        return CommonResponse.successCommonResponse(vo);
    }


}
