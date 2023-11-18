package com.yiling.hmc.wechat.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 拿药计划详情分组DTO
 * 每个参保记录下有N件商品
 *
 * @author fan.shen
 * @date 2022/4/12
 */
@Data
@Accessors(chain = true)
public class InsuranceFetchPlanGroupDTO extends BaseDTO {

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 共N件商品
     */
    private Integer goodsCount;


}
