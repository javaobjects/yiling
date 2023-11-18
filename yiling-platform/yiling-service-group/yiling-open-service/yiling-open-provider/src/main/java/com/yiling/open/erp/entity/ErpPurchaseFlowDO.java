package com.yiling.open.erp.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_purchase_flow")
public class ErpPurchaseFlowDO extends BaseErpEntity {

    private String poId;

    private String poNo;

    private Date poTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    private String enterpriseInnerCode;

    private String enterpriseName;

    private String poBatchNo;

    private BigDecimal poQuantity;

    private Date poProductTime;

    private Date poEffectiveTime;

    private BigDecimal poPrice;

    private String goodsInSn;

    private String goodsName;

    private String poLicense;

    private String poSpecifications;

    private String poUnit;

    private String poManufacturer;

    private String poSource;

    private Long controlId;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 初始化数据
     */
    public ErpPurchaseFlowDO() {
        this.poNo = "";
        this.enterpriseInnerCode = "";
        this.enterpriseName = "";
        this.poBatchNo = "";
        this.poQuantity = BigDecimal.ZERO;
        this.poPrice = BigDecimal.ZERO;
        this.goodsInSn = "";
        this.goodsName = "";
        this.poLicense = "";
        this.poSpecifications = "";
        this.poUnit = "";
        this.poManufacturer = "";
        this.poSource = "";
        this.orderTime = DateUtil.parse("1970-01-01 00:00:00");
    }

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
        if(ObjectUtil.isNull(this.poQuantity)){
            this.poQuantity = BigDecimal.ZERO;
        }
        if(ObjectUtil.isNull(this.poPrice)){
            this.poPrice = BigDecimal.ZERO;
        }

        if (StrUtil.isBlank(this.poId)) {
            StringBuffer sb = new StringBuffer();
            sb.append(this.poNo).append(DateUtil.format(this.poTime, "yyyy-MM-dd HH:mm:ss")).append(this.enterpriseInnerCode).append(this.enterpriseName).append(this.poBatchNo).append(this.poQuantity.stripTrailingZeros().toPlainString()).append(this.poPrice.stripTrailingZeros().toPlainString()).append(this.goodsInSn).append(this.goodsName).append(this.poLicense).append(this.poSpecifications).append(this.poUnit).append(this.poManufacturer).append(this.poSource).append(this.suDeptNo).append(DateUtil.format(this.orderTime, "yyyy-MM-dd"));
            return SecureUtil.md5(sb.toString());
        } else {
            return this.poId;
        }
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpPurchaseFlow.getMethod();
    }

    @Override
    public String sign() {
        try {
            System.out.println(toString());
            return SignatureAlgorithm.byte2hex(SignatureAlgorithm.encryptMD5(toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
