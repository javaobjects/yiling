package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/11/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StatisticsGoodsSaleRequest  extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long goodsId;

    private Long b2bSaleNumber;

}
