package com.yiling.dataflow.crm.dto.request;

import java.util.List;

import com.yiling.dataflow.agency.dto.request.SaveOrUpdateCrmDepartProductGroupRequest;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateCrmGoodsGroupRequest
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmGoodsGroupRequest extends BaseRequest {

    private Long id;

    /**
     * 产品组名称
     */
    private String name;

    /**
     * 产品组状态
     */
    private Integer status;

    /**
     * 商品组关联商品列表
     */
    List<SaveCrmGoodsGroupRelationRequest> goodsRelationList;

    /**
     * 商品组关联部门列表
     */
    List<SaveOrUpdateCrmDepartProductGroupRequest> departmentRelationList;
}
