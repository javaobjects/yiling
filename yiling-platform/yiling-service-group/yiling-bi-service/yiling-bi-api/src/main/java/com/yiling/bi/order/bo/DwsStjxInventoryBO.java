package com.yiling.bi.order.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2022/9/27
 */
@Data
public class DwsStjxInventoryBO implements Serializable {

    private static final long serialVersionUID = -7241297589756328552L;

    private Long id;

    private String dyear;

    private String dmonth;

    private String customer;

    private String wlCode;

    private String wlName;

    private String wlSpec;

    private String crmGoodsid;

    private String b2bGoodsid;

    /**
     * 大运河库存
     */
    private BigDecimal b2bInventory;

    /**
     * 京东库存
     */
    private BigDecimal jdInventory;
}
