package com.yiling.open.cms.contract.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCancelForm extends BaseForm {

    // 合同id
    private Long contractId;

    // 发起方的印章ID，用于签署作废声明
    private Long sealId;

    // 作废原因
    private String reason;

    // 作废成功后是否删除合同正文文档，默认为false
    private Boolean removeContract;
}
