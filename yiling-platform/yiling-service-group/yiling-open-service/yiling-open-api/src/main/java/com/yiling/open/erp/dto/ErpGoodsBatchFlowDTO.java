package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;
import com.yiling.open.erp.validation.group.InterFaceValidation;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.ToString;

/**
 * 库存流向信息DTO
 *
 * @author: houjie.sun
 *  * @date: 2022/2/14
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpGoodsBatchFlowDTO extends BaseErpEntity {

    /**
     * Erp库存流水ID主键
     */
    @NotBlank(message = "不能为空")
    @JSONField(name = "gb_id_no")
    @ToString.Include
    private String gbIdNo;

    /**
     * 入库时间
     */
    @NotNull(message = "不能为空",groups = InterFaceValidation.class)
    @JSONField(name = "gb_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date gbTime;

    /**
     * 商品内码
     */
    @JSONField(name = "in_sn")
    @ToString.Include
    private String inSn;

    /**
     * 批次号
     */
    @JSONField(name = "gb_batch_no")
    @ToString.Include
    private String gbBatchNo;

    /**
     * 生产日期
     */
    @JSONField(name = "gb_produce_time")
    @ToString.Include
    private String gbProduceTime;

    /**
     * 效期
     */
    @JSONField(name = "gb_end_time")
    @ToString.Include
    private String gbEndTime;

    /**
     * 商品名称
     */
    @JSONField(name = "gb_name")
    @ToString.Include
    private String gbName;

    /**
     * 批准文号
     */
    @JSONField(name = "gb_license")
    @ToString.Include
    private String gbLicense;

    /**
     * 商品规格
     */
    @JSONField(name = "gb_specifications")
    @ToString.Include
    private String gbSpecifications;

    /**
     * 商品单位
     */
    @JSONField(name = "gb_unit")
    @ToString.Include
    private String gbUnit;

    /**
     * 库存数量
     */
    @JSONField(name = "gb_number")
    @ToString.Include
    private BigDecimal gbNumber;

    /**
     * 生产厂家
     */
    @JSONField(name = "gb_manufacturer")
    @ToString.Include
    private String gbManufacturer;

    /**
     * 生产地址
     */
    @JSONField(name = "gb_produce_address")
    @ToString.Include
    private String gbProduceAddress;

    /**
     * 初始化数据
     */
    public ErpGoodsBatchFlowDTO() {
        this.inSn = "";
        this.gbBatchNo = "";
        this.gbName = "";
        this.gbLicense = "";
        this.gbSpecifications = "";
        this.gbUnit = "";
        this.gbNumber = BigDecimal.ZERO;
        this.gbManufacturer = "";
        this.gbProduceAddress = "";
        this.gbProduceAddress = "";
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
        if (StrUtil.isBlank(suDeptNo)) {
            return gbIdNo;
        }
        return gbIdNo + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpGoodsBatchFlow.getMethod();
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


    /**
     * 流向缓存本地时间
     * @return
     */
    @Override
    public String getFlowKey(){
        return getErpPrimaryKey();
    }

    /**
     * 流向缓存本地md5
     * @return
     */
    @Override
    public String getFlowCacheFileMd5(){
        return sign();
    }

}
