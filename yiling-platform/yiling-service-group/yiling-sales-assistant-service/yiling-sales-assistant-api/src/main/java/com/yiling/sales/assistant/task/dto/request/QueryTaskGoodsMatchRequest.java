package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/10/18
 */
@Data
@Accessors(chain = true)
public class QueryTaskGoodsMatchRequest extends BaseRequest {
    private static final long serialVersionUID = -2474992476277240252L;

    /**
     * 当前登录人id
     */
    private Long userId;
    /**
     * 商品名 选品种页面接口调用此参数有就传 ，进货单页面不需要传
     */
    private String goodsName;

    /**
     * 配送商id
     */
    private Long eid;
}