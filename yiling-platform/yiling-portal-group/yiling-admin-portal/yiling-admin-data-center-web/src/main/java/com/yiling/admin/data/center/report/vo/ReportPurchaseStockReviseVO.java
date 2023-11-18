package com.yiling.admin.data.center.report.vo;

import java.util.Date;
import java.util.Objects;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportPurchaseStockReviseVO extends BaseVO {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 以岭品id
     */
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSpecifications;

    /**
     * 调整数量
     */
    private Long reviseQuantity;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    private Integer purchaseChannel;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        ReportPurchaseStockReviseVO that = (ReportPurchaseStockReviseVO) o;
        return Objects.equals(eid, that.eid) && Objects.equals(ylGoodsId, that.ylGoodsId) && Objects.equals(goodsInSn, that.goodsInSn) && Objects.equals(purchaseChannel, that.purchaseChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eid, ylGoodsId, goodsInSn, purchaseChannel);
    }
}
