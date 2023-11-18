package com.yiling.open.erp.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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
 * 药品库房批次表 erp_order_Send
 *
 * @author 张爽
 * @date 2020-6-14
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_order_send")
public class ErpOrderSendDO extends BaseErpEntity {

    @ToString.Include
    private Long orderDetailId;

    @ToString.Include
    private String osiId;

    @ToString.Include
    private Long orderId;

    @ToString.Include
    private String sendBatchNo;

    @ToString.Include
    private Date effectiveTime;

    @ToString.Include
    private Date productTime;

    @ToString.Include
    private BigDecimal sendNum;

    @ToString.Include
    private String deliveryNumber;

    @ToString.Include
    private String easSendOrderId;

    @ToString.Include
    private Date sendTime;

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
//        return osiId;
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