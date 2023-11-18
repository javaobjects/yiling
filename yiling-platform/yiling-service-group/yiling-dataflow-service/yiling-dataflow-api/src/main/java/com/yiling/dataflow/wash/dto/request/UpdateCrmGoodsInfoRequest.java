package com.yiling.dataflow.wash.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCrmGoodsInfoRequest extends BaseRequest {

    private static final long serialVersionUID = -3369646944882682320L;

    /**
     * id
     */
    private Long id;

    /**
     * crm商品编码
     */
    private Long crmGoodsCode;

    /**
     * crm商品名称
     */
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    private String crmGoodsSpecifications;

    /**
     * 换算数量
     */
    private BigDecimal conversionQuantity;
}
