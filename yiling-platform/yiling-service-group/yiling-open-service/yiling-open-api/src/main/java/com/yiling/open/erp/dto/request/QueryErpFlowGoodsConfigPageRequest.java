package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpFlowGoodsConfigPageRequest extends QueryPageListRequest {

    /**
     * 商业ID
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    private String goodsInSn;

    /**
     * 创建时间-开始
     */
    private String startCreateTime;

    /**
     * 创建时间-结束
     */
    private String endCreateTime;

}
