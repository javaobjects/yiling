package com.yiling.hmc.goods.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author shichen
 * @类名 GoodsControlBO
 * @描述
 * @创建时间 2022/3/30
 * @修改人 shichen
 * @修改时间 2022/3/30
 **/
@Data
public class GoodsControlBO implements Serializable {
    private static final long serialVersionUID = -6507259957524387400L;

    /**
     * 商品保险id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 标准库规格id
     */
    private Long sellSpecificationsId;
    /**
     * 标准库商品id
     */
    private Long standardId;
    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;
    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String sellSpecifications;
}
