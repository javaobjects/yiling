package com.yiling.open.erp.entity;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 入库单明细表
 * </p>
 *
 * @author houjie.sun
 * @date 2022-03-18
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_order_purchase_delivery")
public class ErpOrderPurchaseDeliveryDO extends BaseErpEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 入库明细编码(主键)
     */
    @ToString.Include
    private String deliveryNo;

    /**
     * 入库数量
     */
    @ToString.Include
    private BigDecimal deliveryQuantity;

    /**
     * POP订单ERP出库单ID
     */
    @ToString.Include
    private Long orderDeliveryErpId;

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
