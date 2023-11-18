package com.yiling.sjms.gb.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.sun.istack.NotNull;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 保存团购信息
 */
@Data
public class SaveGBCancelInfoForm extends BaseForm {

    /**
     * 团购ID
     */
    @NotNull
    @ApiModelProperty(value = "团购ID")
    private Long gbId;

    /**
     *取消原因
     */
    @NotNull
    @ApiModelProperty(value = "取消原因")
    private String cancelReason;

    /**
     *类型 1-保存草稿 2-提交
     */
    @NotNull
    @ApiModelProperty(value = "类型 1-保存草稿 2-提交 3-驳回提交 " ,required = true)
    private Integer type;
}
