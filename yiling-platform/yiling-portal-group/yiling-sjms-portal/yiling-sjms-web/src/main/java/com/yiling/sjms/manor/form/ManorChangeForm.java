package com.yiling.sjms.manor.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/5/10
 */
@Data
public class ManorChangeForm extends BaseForm {
    /**
     * 医院id
     */
    @ApiModelProperty(value = "所选机构id")
    private Long crmEnterpriseId;

    /**
     * form表主键
     */
    @ApiModelProperty(value = "草稿不存在时，第一次新增不用传，其他场景必传")
    private Long formId;



    @ApiModelProperty(value = "备注")
    private String remark;

    private List<ManorChangeItemForm> list;
    /**
     * 是否是修改请求
     */
    private Boolean isUpdate;
}