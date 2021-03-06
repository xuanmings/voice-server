package com.mifa.cloud.voice.server.commons.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2018/4/25.
 */
@ApiModel
@Data
public class SystemKeyValueQueryDTO {

    @ApiModelProperty(value = "客户号", required = true)
    @NotEmpty(message = "客户号不能为空")
    private String contractNo;

    private String paramValue;

    private String remark;

}
