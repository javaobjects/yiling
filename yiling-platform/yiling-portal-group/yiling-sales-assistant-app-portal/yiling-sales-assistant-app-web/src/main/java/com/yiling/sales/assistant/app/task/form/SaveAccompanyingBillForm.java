package com.yiling.sales.assistant.app.task.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 上传随货同行单
 * @author: gxl
 * @date: 2023/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class SaveAccompanyingBillForm extends BaseForm {

    private static final long serialVersionUID = -441339561086671611L;

    @ApiModelProperty(value = "新增时不传")
    private Long id;

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号",required = true)
    @Length(max=40)
    private String docCode;

    /**
     * 随货同行单
     */
    @ApiModelProperty(value = "随货同行单图片key",required = true)
    private String accompanyingBillPic;


}