package com.yiling.open.cms.contract.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/12/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractRecallForm extends BaseForm {

    // 合同id
    private Long contractId;

    // 作废原因
    private String reason;
}
