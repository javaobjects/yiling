package com.yiling.basic.contract.dto.request;

import com.yiling.basic.contract.dto.ContractParamDTO;
import com.yiling.basic.contract.dto.DocumentParamDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCreateRequest extends BaseRequest {

    private static final long serialVersionUID = 999715912918299822L;

    /**
     * 合同参数
     */
    private ContractParamRequest contractParam;

    /**
     * 模板参数
     */
    private DocumentParamRequest documentParam;
}
