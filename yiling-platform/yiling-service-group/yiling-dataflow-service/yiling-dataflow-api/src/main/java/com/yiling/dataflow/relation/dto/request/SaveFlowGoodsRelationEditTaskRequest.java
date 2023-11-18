package com.yiling.dataflow.relation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveFlowGoodsRelationEditTaskRequest extends BaseRequest{

    /**
     * 业务类型：1-erp流向销售同步 2-商品审核同步 3-以岭品关系修改同步
     */
    private Integer businessType;

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
    private String flowSaleIds;

}
