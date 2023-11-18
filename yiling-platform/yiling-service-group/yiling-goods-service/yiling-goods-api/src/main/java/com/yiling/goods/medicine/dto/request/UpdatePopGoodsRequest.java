package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * b2b商品表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePopGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品状态 1上架 2下架 3待设置
     */
    private Integer goodsStatus;

    /**
     * 下架原因：1平台下架 2质管下架 3供应商下架
     */
    private Integer outReason;

    /**
     * 商品基价
     */
    private BigDecimal price;

    private Integer isPatent;
    /**
     * 商品pop 产品线状态 0：未启用  1:启用
     */
    private Integer status;

    /**
     * 所属企业
     */
    private Long eid;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;

}
