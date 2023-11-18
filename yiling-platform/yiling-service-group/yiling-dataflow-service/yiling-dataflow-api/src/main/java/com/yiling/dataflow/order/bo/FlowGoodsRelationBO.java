package com.yiling.dataflow.order.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/5/27
 */
@Data
public class FlowGoodsRelationBO implements Serializable {


    private static final long serialVersionUID = 1035212322948168150L;
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
     * 商业商品内码
     */
    private String goodsInSn;

    /**
     * 商业商品规格
     */
    private String goodsSpecifications;

    /**
     * 以岭商品ID
     */
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    private String ylGoodsSpecifications;

}
