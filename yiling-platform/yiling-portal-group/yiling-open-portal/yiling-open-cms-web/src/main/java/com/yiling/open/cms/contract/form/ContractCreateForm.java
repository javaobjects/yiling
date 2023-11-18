package com.yiling.open.cms.contract.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCreateForm extends BaseForm {
    /**
     * 合同参数
     */
    private ContractParamForm contractParam;

    /**
     * 模板参数
     */
    private DocumentParamForm documentParam;
}
