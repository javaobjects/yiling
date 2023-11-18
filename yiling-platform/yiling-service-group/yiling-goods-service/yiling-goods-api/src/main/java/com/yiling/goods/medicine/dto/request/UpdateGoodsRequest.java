package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long goodsId;

    private BigDecimal price;

    private Integer goodsStatus;

    private Integer goodsLine;

    private Integer isPatent;

    /**
     * 下架原因
     */
    private Integer outReason;

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
