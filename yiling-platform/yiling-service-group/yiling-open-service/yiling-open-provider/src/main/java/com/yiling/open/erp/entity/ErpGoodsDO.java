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
 * 药品信息实体
 *
 * @author Administrator
 */
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_goods")
public class ErpGoodsDO extends BaseErpEntity {

    @ToString.Include
    private String inSn;

    @ToString.Include
    private String sn;

    @ToString.Include
    private String name;

    @ToString.Include
    private String commonName;

    @ToString.Include
    private String aliasName;

    @ToString.Include
    private String licenseNo;

    @ToString.Include
    private String specifications;

    @ToString.Include
    private String unit;

    @ToString.Include
    private Integer middlePackage;

    @ToString.Include
    private Integer bigPackage;

    @ToString.Include
    private String manufacturer;

    @ToString.Include
    private String manufacturerCode;

    @ToString.Include
    private BigDecimal price;

    @ToString.Include
    private Integer canSplit;

    @ToString.Include
    private Integer goodsStatus;

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