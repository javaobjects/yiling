package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import lombok.Data;
import lombok.ToString;

/**
 * 药品库房批次表 erp_order_Send
 *
 * @author 张爽
 * @date 2020-6-14
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpOrderSendDTO extends BaseErpEntity {

    /**
     * 出库单明细ID
     */
    @JSONField(name = "order_detail_id")
    @ToString.Include
    private Long orderDetailId;

    /**
     * ERP发货单明细表ID
     */
    @JSONField(name = "osi_id")
    @ToString.Include
    private String osiId;

    /**
     * pop订单ID
     */
    @JSONField(name = "order_id")
    @ToString.Include
    private Long orderId;

    /**
     * 发货批次
     */
    @JSONField(name = "send_batch_no")
    @ToString.Include
    private String sendBatchNo;

    /**
     * 商品有效期
     */
    @JSONField(name = "effective_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date effectiveTime;

    /**
     * 生产日期
     */
    @JSONField(name = "product_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date productTime;

    /**
     * 批次的发货数量
     */
    @JSONField(name = "send_num")
    @ToString.Include
    private BigDecimal sendNum;

    /**
     * 出库单号
     */
    @JSONField(name = "delivery_number")
    @ToString.Include
    private String deliveryNumber;

    /**
     * eas出库单主键
     */
    @JSONField(name = "eas_send_order_id")
    @ToString.Include
    private String easSendOrderId;

    /**
     * 发货时间
     */
    @JSONField(name = "send_time")
    @ToString.Include
    private Date sendTime;

    /**
     * 状态:1正常发货2订单关闭3作废
     */
    @JSONField(name = "send_type")
    @ToString.Include
    private Integer sendType;

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
            return osiId;
        }
        return osiId + OpenConstants.SPLITE_SYMBOL + suDeptNo;
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
        return ErpTopicName.ErpOrderSend.getMethod();
    }

}