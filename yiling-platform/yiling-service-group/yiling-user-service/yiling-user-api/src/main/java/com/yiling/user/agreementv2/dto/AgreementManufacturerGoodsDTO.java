package com.yiling.user.agreementv2.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家商品表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementManufacturerGoodsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 厂家ID
     */
    private Long manufacturerId;

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
     * 生产厂家名称
     */
    private String manufacturerName;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
