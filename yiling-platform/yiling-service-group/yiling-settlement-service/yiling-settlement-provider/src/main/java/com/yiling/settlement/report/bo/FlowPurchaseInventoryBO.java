package com.yiling.settlement.report.bo;

import java.util.Objects;

import com.yiling.framework.common.util.Constants;

import lombok.Data;

/**
 * @author: dexi.yao
 * @date: 2022-05-26
 */
@Data
public class FlowPurchaseInventoryBO {

    /**
     * eid
     */
    private Long eid;

    /**
     * 以岭商品id
     */
    private Long ylGoodsId;

    /**
     * 内码
     */
    private String goodsInSn;

    /**
     * 大运河库存ID
     */
    private Long dyhId;

    /**
     * 大运河库存
     */
    private Long dyhQuantity=0L;

    /**
     * 大运河操作数量
     */
    private Long dyhOpQuantity=0L;

    /**
     * 京东库存ID
     */
    private Long jdId;

    /**
     * 京东库存
     */
    private Long jdQuantity=0L;

    /**
     * 京东操作数量
     */
    private Long jdOpQuantity=0L;



    /**
     * 根据eid商品id和内码生成唯一key
     *
     * @param eid
     * @param ylGoodsId
     * @param goodsInSn
     * @return
     */
    public static String getKey(Long eid, Long ylGoodsId, String goodsInSn) {
        return eid.toString()+ Constants.SEPARATOR_MIDDLELINE+ylGoodsId.toString()+Constants.SEPARATOR_MIDDLELINE+goodsInSn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FlowPurchaseInventoryBO that = (FlowPurchaseInventoryBO) o;
        return eid.equals(that.eid) && ylGoodsId.equals(that.ylGoodsId) && goodsInSn.equals(that.goodsInSn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, ylGoodsId, goodsInSn);
    }
}
