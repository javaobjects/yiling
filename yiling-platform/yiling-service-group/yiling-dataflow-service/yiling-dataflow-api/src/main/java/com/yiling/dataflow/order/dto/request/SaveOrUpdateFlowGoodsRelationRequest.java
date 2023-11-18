package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateFlowGoodsRelationRequest extends BaseRequest {

    /**
     * 商业公司eid
     */
    private Long eid;

    /**
     * 商业名称
     */
    private String ename;

    /**
     * 商业商品名称
     */
    private String goodsName;

    /**
     * 商业商品规格
     */
    private String goodsSpecifications;

    /**
     * 生产厂家
     */
    private String goodsManufacturer;

    /**
     * 以岭品id（商品审核通过处理使用）
     */
    private Long ylGoodsId;

    /**
     * 以岭品名称
     */
    private String ylGoodsName;

    /**
     * 以岭品规格
     */
    private String ylGoodsSpecifications;

    /**
     * 商业商品内码列表
     */
    private List<String> goodsInSnList;

}
