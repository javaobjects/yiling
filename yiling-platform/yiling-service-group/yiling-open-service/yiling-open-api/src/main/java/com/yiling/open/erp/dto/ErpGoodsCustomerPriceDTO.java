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
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-03
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpGoodsCustomerPriceDTO extends BaseErpEntity {

    private static final long serialVersionUID = 1L;

    /**
     * erp主键
     */
    @JSONField(name = "gcp_id_no")
    @ToString.Include
    private String gcpIdNo;

    /**
     * 客户内码
     */
    @JSONField(name = "inner_code")
    @ToString.Include
    private String innerCode;

    /**
     * 商品内码
     */
    @JSONField(name = "in_sn")
    @ToString.Include
    private String inSn;

    /**
     * 客户定价
     */
    @JSONField(name = "price")
    @ToString.Include
    private BigDecimal price;

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
            return gcpIdNo;
        }
        return gcpIdNo + OpenConstants.SPLITE_SYMBOL + suDeptNo;
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
        return ErpTopicName.ErpGoodsPrice.getMethod();
    }


}
