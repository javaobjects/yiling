package com.yiling.user.agreementv2.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销商品 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesGoodsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 供销商品组ID
     */
    private Long controlGoodsGroupId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 规格商品ID
     */
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 生产厂家
     */
    private String producerManufacturer;

    /**
     * 供货含税单价
     */
    private BigDecimal supplyTaxPrice;

    /**
     * 出库价含税单价
     */
    private BigDecimal exitWarehouseTaxPrice;

    /**
     * 零售价含税单价
     */
    private BigDecimal retailTaxPrice;

    /**
     * 是否独家：0-否 1-是
     */
    private Integer exclusiveFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

}
