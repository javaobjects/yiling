package com.yiling.dataflow.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsRelationTaskPageRequest extends QueryPageListRequest {

    /**
     * 以岭品关系ID
     */
    private Long flowGoodsRelationId;

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 同步状态：0未同步，1正在同步 2返利同步成功 4同步失败
     */
    private Integer syncStatus;

}
