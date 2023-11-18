package com.yiling.sjms.flowcollect.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询下载文件名 Form
 *
 * @author lun.yu
 * @date 2023-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetDownLoadNameForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 类型：1-月流向上传 2-销售申述 3-窜货申报
     */
    @NotNull
    @ApiModelProperty(value = "类型：1-月流向上传 2-销售申述 3-窜货申报", required = true)
    private Integer type;

}
