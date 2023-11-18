package com.yiling.open.erp.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.SignatureAlgorithm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 连锁门店销售明细信息表
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-03-20
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_shop_sale_flow")
public class ErpShopSaleFlowDO extends BaseErpEntity {

    /**
     * 门店编码
     */
    private String shopNo;

    /**
     * 门店名称
     */
    private String shopName;

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
     * 客户名称
     */
    private String enterpriseName;

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
     * 任务主键ID
     */
    private Long controlId;



    /**
     * 初始化数据
     */
    public ErpShopSaleFlowDO() {
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
        this.controlId = 0L;
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
