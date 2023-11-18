package com.yiling.sjms.flee.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Data
public class CreateFleeFlowForm extends BaseForm {

    /**
     * form表主键
     */
    @ApiModelProperty(value = "form表主键")
    private Long formId;

    /**
     * 确认时的备注意见
     */
    @ApiModelProperty(value = "确认时的备注意见")
    private String confirmDescribe;

    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    @ApiModelProperty(value = "传输类型：1、上传excel; 2、选择流向")
    private Integer transferType;

}
