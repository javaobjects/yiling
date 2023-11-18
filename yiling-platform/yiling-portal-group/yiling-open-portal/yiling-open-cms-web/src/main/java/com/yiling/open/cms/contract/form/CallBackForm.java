package com.yiling.open.cms.contract.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallBackForm extends BaseForm {

    private Long contractId;

    private Long tenantId;

    private String tenantName;

    private String sn;

    private String status;

    private String type;

    private String contact;
}
