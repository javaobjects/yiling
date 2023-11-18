package com.yiling.open.cms.content.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
@Accessors(chain = true)
public class GetContentForm {

    @NotNull
   private Long id;

    @ApiModelProperty(value = "wx_doctor表id")
    private Long wxDoctorId;
}