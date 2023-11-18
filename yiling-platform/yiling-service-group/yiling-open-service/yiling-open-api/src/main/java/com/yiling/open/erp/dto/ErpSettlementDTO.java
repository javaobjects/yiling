package com.yiling.open.erp.dto;

import java.io.IOException;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;

import lombok.Data;
import lombok.ToString;

/**
 * @author: shuang.zhang
 * @date: 2021/7/23
 */
@ToString
@Data
public class ErpSettlementDTO extends BaseErpEntity {

    @JSONField(name = "charge_no")
    private String chargeNo;

    @JSONField(name = "charge_off_type")
    private String chargeOffType;

    @JSONField(name = "charge_off_no")
    private String chargeOffNo;

    @JSONField(name = "charge_off_amount")
    private String chargeOffAmount;

    @JSONField(name = "charge_time",format="yyyy-MM-dd",ordinal = 22)
    private Date   chargeTime;

    @JSONField(name = "goods_in_sn")
    private String goodsInSn;

    @JSONField(name = "erp_charge_id")
    private String erpChargeId;

    @JSONField(name = "return_type")
    private String returnType;

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
            return erpChargeId;
        }
        return erpChargeId;
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
        return ErpTopicName.ErpStatement.getMethod();
    }

}
