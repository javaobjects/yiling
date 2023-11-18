package com.yiling.open.erp.entity;

import java.io.IOException;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_settlement")
public class ErpSettlementDO extends BaseErpEntity {

    private static final long serialVersionUID = 1L;

    /**
     * erp主键
     */
    private String erpChargeId;

    /**
     * 客户内码
     */
    private String chargeNo;

    private String chargeOffType;

    private String chargeOffNo;

    private String chargeOffAmount;

    private Date chargeTime;

    private String goodsInSn;

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
