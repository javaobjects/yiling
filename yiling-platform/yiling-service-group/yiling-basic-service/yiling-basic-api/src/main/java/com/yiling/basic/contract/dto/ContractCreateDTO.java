package com.yiling.basic.contract.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 999715912918299822L;

    /**
     * 合同参数
     */
    private ContractParamDTO contractParam;

    /**
     * 模板参数
     */
    private DocumentParamDTO documentParam;
}
