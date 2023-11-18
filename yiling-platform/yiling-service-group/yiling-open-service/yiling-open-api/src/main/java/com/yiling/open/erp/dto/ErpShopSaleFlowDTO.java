package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;
import lombok.Data;
import lombok.ToString;

/**
 * 采购流向信息实体
 *
 * @author: houjie.sun
 * @date: 2021/9/23
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpShopSaleFlowDTO extends BaseErpEntity {

    /**
     * Erp采购订主键
     */
    @JSONField(name = "so_id")
    @ToString.Include
    protected String soId;

    /**
     * Erp采购订单号
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "so_no")
    @ToString.Include
    protected String soNo;

    /**
     * 采购日期
     */
    @NotNull(message = "不能为空")
    @JSONField(name = "so_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date soTime;

    /**
     * 下单日期
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "shop_no")
    @ToString.Include
    private String shopNo;

    /**
     * 客户内码
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "shop_name")
    @ToString.Include
    private String shopName;

    /**
     * 客户名称
     */
    @JSONField(name = "enterprise_name")
    @ToString.Include
    private String enterpriseName;

    /**
     * 批号
     */
    @JSONField(name = "so_batch_no")
    @ToString.Include
    private String soBatchNo;

    /**
     * 销售数量
     */
    @JSONField(name = "so_quantity",serialzeFeatures = SerializerFeature.WriteBigDecimalAsPlain)
    @ToString.Include
    private BigDecimal soQuantity;

    /**
     * 生产日期
     */
    @JSONField(name = "so_product_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date soProductTime;

    /**
     * 效期
     */
    @JSONField(name = "so_effective_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date soEffectiveTime;

    /**
     * 价格
     */
    @JSONField(name = "so_price")
    @ToString.Include
    private BigDecimal soPrice;

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
    @JSONField(name = "so_license")
    @ToString.Include
    private String soLicense;

    /**
     * 商品规格
     */
    @JSONField(name = "so_specifications")
    @ToString.Include
    private String soSpecifications;

    /**
     * 商品单位
     */
    @JSONField(name = "so_unit")
    @ToString.Include
    private String soUnit;

    /**
     * 商品生产厂家
     */
    @JSONField(name = "so_manufacturer")
    @ToString.Include
    private String soManufacturer;


    private Long controlId;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 初始化数据
     */
    public ErpShopSaleFlowDTO() {
        this.soNo = "";
        this.shopNo = "";
        this.shopName = "";
        this.enterpriseName = "";
        this.soBatchNo = "";
        this.soQuantity = BigDecimal.ZERO;
        this.soPrice = BigDecimal.ZERO;
        this.goodsInSn = "";
        this.goodsName = "";
        this.soLicense = "";
        this.soSpecifications = "";
        this.soUnit = "";
        this.soManufacturer = "";
        this.controlId=0L;
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
        if(ObjectUtil.isNull(this.soQuantity)){
            this.soQuantity = BigDecimal.ZERO;
        }
        if(ObjectUtil.isNull(this.soPrice)){
            this.soPrice = BigDecimal.ZERO;
        }

        if(StrUtil.isBlank(this.soId)) {
            StringBuffer sb = new StringBuffer();
            sb.append(this.soNo).append(DateUtil.format(this.soTime, "yyyy-MM-dd HH:mm:ss")).append(this.shopNo).append(this.shopName).append(this.enterpriseName).append(this.soBatchNo).append(this.soQuantity.stripTrailingZeros().toPlainString()).append(this.soPrice.stripTrailingZeros().toPlainString()).append(this.goodsInSn).append(this.goodsName).append(this.soLicense).append(this.soSpecifications).append(this.soUnit).append(this.soManufacturer).append(this.suDeptNo);
            return SecureUtil.md5(sb.toString());
        }else{
            return this.soId;
        }
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpShopSaleFlow.getMethod();
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
