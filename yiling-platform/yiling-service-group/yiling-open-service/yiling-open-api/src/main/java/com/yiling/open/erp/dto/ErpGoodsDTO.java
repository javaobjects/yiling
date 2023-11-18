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
 * 药品信息实体
 *
 * @author Administrator
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpGoodsDTO extends BaseErpEntity {

    @JSONField(name = "in_sn")
    @ToString.Include
    private String inSn;

    @JSONField(name = "sn")
    @ToString.Include
    private String sn="";

    @JSONField(name = "name")
    @ToString.Include
    private String name="";

    @JSONField(name = "common_name")
    @ToString.Include
    private String commonName="";

    @JSONField(name = "alias_name")
    @ToString.Include
    private String aliasName="";

    @JSONField(name = "license_no")
    @ToString.Include
    private String licenseNo="";

    @JSONField(name = "specifications")
    @ToString.Include
    private String specifications="";

    @JSONField(name = "unit")
    @ToString.Include
    private String unit="";

    @JSONField(name = "middle_package")
    @ToString.Include
    private Integer middlePackage=1;

    @JSONField(name = "big_package")
    @ToString.Include
    private Integer bigPackage=1;

    @JSONField(name = "manufacturer")
    @ToString.Include
    private String manufacturer="";

    @JSONField(name = "manufacturer_code")
    @ToString.Include
    private String manufacturerCode="";

    @JSONField(name = "price")
    @ToString.Include
    private BigDecimal price = BigDecimal.ZERO;

    @JSONField(name = "can_split")
    @ToString.Include
    private Integer canSplit=1;

    @JSONField(name = "goods_status")
    @ToString.Include
    private Integer goodsStatus;

    @JSONField(name = "manufacturer_address")
    @ToString.Include
    private String manufacturerAddress;

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
            return inSn;
        }
        return inSn + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpGoods.getMethod();
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

}