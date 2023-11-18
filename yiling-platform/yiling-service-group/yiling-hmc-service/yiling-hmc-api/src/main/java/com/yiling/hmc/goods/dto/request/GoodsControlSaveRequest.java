package com.yiling.hmc.goods.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 GoodsControlSaveRequest
 * @描述 管控商品保存类
 * @创建时间 2022/3/30
 * @修改人 shichen
 * @修改时间 2022/3/30
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsControlSaveRequest extends BaseRequest {
    /**
     * 商品保险id
     */
    private Long id;
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
}
