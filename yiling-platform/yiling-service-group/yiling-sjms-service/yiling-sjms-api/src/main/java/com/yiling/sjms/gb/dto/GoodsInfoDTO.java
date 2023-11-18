package com.yiling.sjms.gb.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsInfoDTO extends BaseDO {

    /**
     * 团购ID
     */
    private Long gbId;

    /**
     * 出库终端和商业公司信息id
     */
    private Long companyId;

    /**
     * 产品编码
     */
    private Long code;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品规格
     */
    private String specification;

    /**
     * 小包装
     */
    private Integer smallPackage;

    /**
     * 团购数量（盒）
     */
    private Integer quantityBox;

    /**
     * 团购数量（件）
     */
    private BigDecimal quantity;

    /**
     * 实际团购单价
     */
    private BigDecimal finalPrice;

    /**
     * 实际团购金额
     */
    private BigDecimal finalAmount;

    /**
     * 产品核算价
     */
    private BigDecimal price;

    /**
     * 团购核算总额
     */
    private BigDecimal amount;

    /**
     * 团购成功回款日期
     */
    private Date paymentTime;

    /**
     * 流向月份
     */
    private Date flowMonth;



}
