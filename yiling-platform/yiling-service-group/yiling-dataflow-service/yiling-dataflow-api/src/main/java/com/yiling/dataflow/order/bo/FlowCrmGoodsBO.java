package com.yiling.dataflow.order.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 FlowCrmGoodsBO
 * @描述
 * @创建时间 2023/2/23
 * @修改人 shichen
 * @修改时间 2023/2/23
 **/
@Data
public class FlowCrmGoodsBO implements Serializable {

    private String goodsName;

    private String specifications;

    private String ename;

    /**
     * 商业代码（商家eid）
     */
    private Long eid;

    private String crmCode;
}
