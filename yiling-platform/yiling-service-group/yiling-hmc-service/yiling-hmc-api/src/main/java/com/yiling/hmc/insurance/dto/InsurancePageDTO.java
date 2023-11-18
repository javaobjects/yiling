package com.yiling.hmc.insurance.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险及相关信息
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsurancePageDTO extends InsuranceDTO {

    /**
     * 保险商品明细
     */
    private List<InsuranceDetailDTO> insuranceDetailDTOList;
}
