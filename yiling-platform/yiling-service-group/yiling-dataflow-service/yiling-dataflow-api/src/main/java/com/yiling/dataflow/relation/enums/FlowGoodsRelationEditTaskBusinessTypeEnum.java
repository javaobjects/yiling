package com.yiling.dataflow.relation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/10/12
 */
@Getter
@AllArgsConstructor
public enum FlowGoodsRelationEditTaskBusinessTypeEnum {

    ERP_FLOW_SALE_SYNC(1, "erp流向销售同步"),
    GOODS_AUDIT_APPROVED_OR_EDIT(2, "商品审核或修改同步"),
    FLOW_GOODS_RELATION_EDIT(3, "商家品与以岭品对应关系修改"),
    FLOW_SALE_ENTERPRISETAG(4, "流向销售根据商业标签调度"),
    ;

    private Integer code;
    private String desc;

    public static FlowGoodsRelationEditTaskBusinessTypeEnum getFromCode(Integer code) {
        for (FlowGoodsRelationEditTaskBusinessTypeEnum e : FlowGoodsRelationEditTaskBusinessTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
