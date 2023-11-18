package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;
import lombok.ToString;

/**
 * 采购流向信息实体
 *
 * @author: houjie.sun
 * @date: 2021/9/22
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpPurchaseFlowDTO extends BaseErpEntity {

    /**
     * Erp采购订主键
     */
    @JSONField(name = "po_id")
    @ToString.Include
    private String poId;

    /**
     * Erp采购订单号
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "po_no")
    @ToString.Include
    protected String poNo;

    /**
     * 采购日期
     */
    @NotNull(message = "不能为空")
    @JSONField(name = "po_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date poTime;

    /**
     * 下单日期
     */
    @JSONField(name = "order_time", format = "yyyy-MM-dd")
    @ToString.Include
    private Date orderTime;

    /**
     * 客户内码
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "enterprise_inner_code")
    @ToString.Include
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    @JSONField(name = "enterprise_name")
    @ToString.Include
    private String enterpriseName;

    /**
     * 批号
     */
    @JSONField(name = "po_batch_no")
    @ToString.Include
    private String poBatchNo;

    /**
     * 采购数量
     */
    @JSONField(name = "po_quantity",serialzeFeatures = SerializerFeature.WriteBigDecimalAsPlain)
    @ToString.Include
    private BigDecimal poQuantity;

    /**
     * 生产日期
     */
    @JSONField(name = "po_product_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date poProductTime;

    /**
     * 效期
     */
    @JSONField(name = "po_effective_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date poEffectiveTime;

    /**
     * 价格
     */
    @JSONField(name = "po_price")
    @ToString.Include
    private BigDecimal poPrice;

    /**
     * 商品内码
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "goods_in_sn")
    @ToString.Include
    private String goodsInSn;

    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    @ToString.Include
    private String goodsName;

    /**
     * 批准文号
     */
    @JSONField(name = "po_license")
    @ToString.Include
    private String poLicense;

    /**
     * 商品规格
     */
    @JSONField(name = "po_specifications")
    @ToString.Include
    private String poSpecifications;

    /**
     * 商品单位
     */
    @JSONField(name = "po_unit")
    @ToString.Include
    private String poUnit;

    /**
     * 商品生产厂家
     */
    @JSONField(name = "po_manufacturer")
    @ToString.Include
    private String poManufacturer;

    /**
     * 数据来源
     */
    @JSONField(name = "po_source")
    @ToString.Include
    private String poSource;

    private Long controlId;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 初始化数据
     */
    public ErpPurchaseFlowDTO() {
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

        if(StrUtil.isBlank(this.poId)) {
            StringBuffer sb = new StringBuffer();
            sb.append(this.poNo).append(DateUtil.format(this.poTime, "yyyy-MM-dd HH:mm:ss")).append(this.enterpriseInnerCode).append(this.enterpriseName).append(this.poBatchNo).append(this.poQuantity.stripTrailingZeros().toPlainString()).append(this.poPrice.stripTrailingZeros().toPlainString()).append(this.goodsInSn).append(this.goodsName).append(this.poLicense).append(this.poSpecifications).append(this.poUnit).append(this.poManufacturer).append(this.poSource).append(this.suDeptNo).append(DateUtil.format(this.orderTime, "yyyy-MM-dd"));
            return SecureUtil.md5(sb.toString());
        }else{
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
