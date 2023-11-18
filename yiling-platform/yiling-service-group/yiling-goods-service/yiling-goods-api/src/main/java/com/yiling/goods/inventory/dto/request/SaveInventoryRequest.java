package com.yiling.goods.inventory.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveInventoryRequest  extends BaseRequest {

    private static final long serialVersionUID = -3337103042833235608L;

    /**
     * 库存主键ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 商品内码
     */
    private String inSn;

    /**
     * 是否超卖商品 0-非超卖 1-超卖
     */
    private Integer overSoldType;

    /**
     * 库存数量
     */
    private Long qty;

    /**
     * 库存冻结数量
     */
    private Long frozenQty;

    /**
     * 批号
     */
    private String batchNumber;

    /**
     * 有效期
     */
    private Date expiryDate;
}
