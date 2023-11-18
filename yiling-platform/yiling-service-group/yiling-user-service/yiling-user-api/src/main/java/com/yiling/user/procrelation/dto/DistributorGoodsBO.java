package com.yiling.user.procrelation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: dexi.yao
 * @date: 2023-05-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsBO implements Serializable {

    private static final long serialVersionUID = 8514311253174837491L;

    /**
     * pop采购关系id
     */
    private Long relationId;

    /**
     * 配送商eid
     */
    private Long distributorEid;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 标准库商品名称
     */
    private String standardGoodsName;

    /**
     * 标准库批准文号
     */
    private String standardLicenseNo;

    /**
     * 售卖规格
     */
    private String sellSpecifications;

    /**
     * 批准文号
     */
    private String licenseNo;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 商品优化折扣，单位为百分比
     */
    private BigDecimal rebate;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        DistributorGoodsBO that = (DistributorGoodsBO) o;
        return distributorEid.equals(that.distributorEid) && goodsId.equals(that.goodsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(distributorEid, goodsId);
    }
}
