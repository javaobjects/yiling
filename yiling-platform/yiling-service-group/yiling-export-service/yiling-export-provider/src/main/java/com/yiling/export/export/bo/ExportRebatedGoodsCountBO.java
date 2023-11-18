package com.yiling.export.export.bo;

import java.util.Objects;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
public class ExportRebatedGoodsCountBO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 以岭商品id
     */
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 购进渠道：1-大运河采购 2-京东采购 3-库存不足
     */
    private Integer purchaseChannel;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        ExportRebatedGoodsCountBO that = (ExportRebatedGoodsCountBO) o;
        return Objects.equals(eid, that.eid) && Objects.equals(ylGoodsId, that.ylGoodsId) && Objects.equals(goodsInSn, that.goodsInSn) && Objects.equals(purchaseChannel, that.purchaseChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eid, ylGoodsId, goodsInSn, purchaseChannel);
    }
}
