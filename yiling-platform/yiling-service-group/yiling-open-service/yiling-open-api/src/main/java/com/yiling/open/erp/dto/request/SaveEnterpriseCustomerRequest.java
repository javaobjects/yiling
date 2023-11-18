package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveEnterpriseCustomerRequest extends BaseRequest {

    /**
     * 客户eid
     */
    private Long customerEid;

    /**
     * open库erp客户
     */
    private ErpCustomerDTO erpCustomer;

    /**
     * 天眼查企业信息
     */
    TycEnterpriseInfoDTO tycEnterprise;
}
