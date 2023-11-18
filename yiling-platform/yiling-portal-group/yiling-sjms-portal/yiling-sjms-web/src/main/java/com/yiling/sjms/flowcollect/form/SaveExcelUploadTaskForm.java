package com.yiling.sjms.flowcollect.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 生成上传任务 Form
 *
 * @author lun.yu
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveExcelUploadTaskForm extends BaseForm {

    /**
     * 文件Key
     */
    @NotEmpty
    @ApiModelProperty(value = "文件Key", required = true)
    private String fileKey;

    /**
     * 文件名
     */
    @NotEmpty
    @ApiModelProperty(value = "文件名", required = true)
    private String fileName;

}
