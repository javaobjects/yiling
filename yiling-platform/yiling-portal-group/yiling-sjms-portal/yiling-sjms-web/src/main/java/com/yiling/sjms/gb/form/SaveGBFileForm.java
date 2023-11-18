package com.yiling.sjms.gb.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上传证据
 */
@Data
public class SaveGBFileForm extends BaseForm {
    /**
     * 文件key
     */
    @ApiModelProperty(value = "文件key")
    @NotEmpty
    private List<FileInfoForm> fileKeyList;

    @NotNull
    @ApiModelProperty(value = "团购ID")
    private Long gbId;
}
