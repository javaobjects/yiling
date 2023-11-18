package com.yiling.hmc.insurance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险商品明细
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceDetailDTO extends BaseDTO {

    /**
     * 保险id
     */
    private Long insuranceId;

    /**
     * 药品id
     */
    private Long controlId;

    /**
     * 药品名称
     */
    private String goodsName;

    /**
     * 保司跟以岭的结算单价
     */
    private BigDecimal settlePrice;

    /**
     * 每月1次，每次拿多少盒
     */
    private Long monthCount;

    /**
     * 保司药品编码
     */
    private String insuranceGoodsCode;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
