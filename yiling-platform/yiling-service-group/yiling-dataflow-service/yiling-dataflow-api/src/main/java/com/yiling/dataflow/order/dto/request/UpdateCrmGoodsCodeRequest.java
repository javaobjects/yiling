package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCrmGoodsCodeRequest extends BaseRequest {

    private static final long serialVersionUID = 2269593922610968757L;
    /**
     * id
     */
    private Long id;

    /**
     * crm商品编码
     */
    private Long crmGoodsCode;
}
