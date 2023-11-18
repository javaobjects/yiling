package com.yiling.admin.data.center.report.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author gxl
 * @date 2022-02-24
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
