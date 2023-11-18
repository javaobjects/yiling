package com.yiling.user.procrelation.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsRebateRequest extends BaseRequest {

    /**
     * 商品优惠折扣
     */
    private BigDecimal rebate;

    /**
     * 采购关系商品id
     */
    private List<Long> idList;
}
