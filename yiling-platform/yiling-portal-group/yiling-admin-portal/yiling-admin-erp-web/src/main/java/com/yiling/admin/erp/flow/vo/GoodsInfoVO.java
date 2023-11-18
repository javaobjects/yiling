package com.yiling.admin.erp.flow.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/8/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsInfoVO extends BaseVO {

    /**
     * 商品名称
     */
    private String name;


    /**
     * 销售规格
     */
    private String specifications;

    /**
     * 售卖规格
     */
    private String sellSpecifications;

    /**
     * 售卖规格ID
     */
    private Long  sellSpecificationsId;

    /**
     * 生产厂家
     */
    private String manufacturer;
}
