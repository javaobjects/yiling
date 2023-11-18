package com.yiling.dataflow.relation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowGoodsPriceRelationRequest
 * @描述
 * @创建时间 2023/2/22
 * @修改人 shichen
 * @修改时间 2023/2/22
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowGoodsPriceRelationRequest extends BaseRequest {

    /**
     * 原始产品代码
     */
    private Long oldGoodsCode;

    /**
     * 原始产品名称
     */
    private String oldGoodsName;

    /**
     * 产品规格
     */
    private String spec;

    /**
     * 对应产品代码
     */
    private Long goodsCode;

    /**
     * 对应产品名称
     */
    private String goodsName;

    /**
     * 经销商代码
     */
    private String customerCode;

    /**
     * 经销商名称
     */
    private String customer;
}
