package com.yiling.bi.dayunhe.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("dayunhe_sale_order")
public class DayunheSaleOrderDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private String supplierName;

    private String saleOrder;

    private String goodsInSn;

    private String soBatchNo;

    private Integer soQuantity;

    private String goodsName;

    private String soSpecifications;


}
