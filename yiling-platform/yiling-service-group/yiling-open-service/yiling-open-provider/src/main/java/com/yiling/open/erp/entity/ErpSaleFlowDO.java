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
 * 流向销售明细信息表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_sale_flow")
public class ErpSaleFlowDO extends BaseErpEntity {

    /**
     * 主键
     */
    private String soId;

    /**
     * Erp销售订单号
     */
    private String soNo;

    /**
     * 销售日期
     */
    private Date soTime;

    /**
     * 下单日期
     */
    private Date orderTime;

    /**
     * 客户内码
     */
    private String enterpriseInnerCode;

    /**
     * 客户名称
     */
    private String enterpriseName;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    /**
     * 批号
     */
    private String soBatchNo;

    /**
     * 销售数量
     */
    private BigDecimal soQuantity;

    /**
     * 生产日期
     */
    private Date soProductTime;

    /**
     * 效期
     */
    private Date soEffectiveTime;

    /**
     * 价格
     */
    private BigDecimal soPrice;

    /**
     * 商品内码
     */
    private String goodsInSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 批准文号
     */
    private String soLicense;

    /**
     * 商品规格
     */
    private String soSpecifications;

    /**
     * 商品单位
     */
    private String soUnit;

    /**
     * 商品生产厂家
     */
    private String soManufacturer;

    /**
     * 订单来源，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private String soSource;

    /**
     * 任务表ID
     */
    private Long controlId;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    private Integer dataTag;

    /**
     * 初始化数据
     */
    public ErpSaleFlowDO() {
        this.soNo = "";
        this.enterpriseInnerCode = "";
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
        this.soSource = "";
        this.licenseNumber = "";
        this.controlId=0L;
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
        if(ObjectUtil.isNull(this.soQuantity)){
            this.soQuantity = BigDecimal.ZERO;
        }
        if(ObjectUtil.isNull(this.soPrice)){
            this.soPrice = BigDecimal.ZERO;
        }

        if(StrUtil.isBlank(this.soId)) {
            StringBuffer sb = new StringBuffer();
            sb.append(this.soNo).append(DateUtil.format(this.soTime, "yyyy-MM-dd HH:mm:ss")).append(this.enterpriseInnerCode).append(this.enterpriseName).append(this.soBatchNo).append(this.soQuantity.stripTrailingZeros().toPlainString()).append(this.soPrice.stripTrailingZeros().toPlainString()).append(this.goodsInSn).append(this.goodsName).append(this.soLicense).append(this.soSpecifications).append(this.soUnit).append(this.soManufacturer).append(this.soSource).append(this.suDeptNo).append(this.licenseNumber).append(DateUtil.format(this.orderTime, "yyyy-MM-dd"));
            return SecureUtil.md5(sb.toString());
        }else{
            return this.soId;
        }
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpSaleFlow.getMethod();
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
