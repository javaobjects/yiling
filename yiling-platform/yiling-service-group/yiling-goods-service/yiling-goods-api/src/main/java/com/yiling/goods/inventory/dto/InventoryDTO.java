package com.yiling.goods.inventory.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InventoryDTO extends BaseDTO {

    private static final long serialVersionUID = -3337103042833235608L;

    /**
     * 商品ID
     */
    private Long skuId;
    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 库存数量
     */
    private Long qty;

    /**
     * 库存冻结数量
     */
    private Long frozenQty;

    /**
     * 商品编码
     */
    private String inSn;

    /**
     * 是否超卖商品 0-非超卖 1-超卖
     */
    private Integer overSoldType;

//    /**
//     * 批号
//     */
//    private String batchNumber;
//
//    /**
//     * 有效期
//     */
//    private Date expiryDate;

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
