package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import lombok.Data;
import lombok.ToString;

/**
 * 药品入库单明细表 erp_order_purchase_delivery
 *
 * @author: houjie.sun
 * @date: 2022/3/18
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpOrderPurchaseDeliveryDTO extends BaseErpEntity {

    /**
     * 入库明细编码(主键)
     */
    @JSONField(name = "delivery_no")
    @ToString.Include
    private String deliveryNo;

    /**
     * 入库数量
     */
    @JSONField(name = "delivery_quantity")
    @ToString.Include
    private BigDecimal deliveryQuantity;

    /**
     * POP订单ERP出库单ID
     */
    @JSONField(name = "order_delivery_erp_id")
    @ToString.Include
    private Long orderDeliveryErpId;

    /**
     * POP订单ERP出库单明细ID
     */
    @JSONField(name = "order_delivery_erp_detail_id")
    @ToString.Include
    private Long orderDeliveryErpDetailId;

    @Override
    public Long getPrimaryKey() {
        return this.getId();
    }

    @Override
    public void setPrimaryKey(Long primaryKey) {
        this.setId(primaryKey);
    }

    @Override
    public String getErpPrimaryKey() {
        if (StringUtils.isEmpty(suDeptNo)) {
            return deliveryNo;
        }
        return deliveryNo + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    @Override
    public String sign() {
        try {
            return SignatureAlgorithm.byte2hex(SignatureAlgorithm.encryptMD5(toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpOrderPurchaseDelivery.getMethod();
    }

}
