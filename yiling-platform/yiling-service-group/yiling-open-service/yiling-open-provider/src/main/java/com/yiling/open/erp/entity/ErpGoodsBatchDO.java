package com.yiling.open.erp.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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
 * 药品库房批次表 erp_goods_batch
 *
 * @author 张爽
 * @date 2020-6-14
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_goods_batch")
public class ErpGoodsBatchDO extends BaseErpEntity {

    /**
     * 药品内码（供应商的ERP的商品主键）
      */
    @ToString.Include
    private String inSn="";

    /**
     * erp的库存编码id
     */
    @ToString.Include
    private String gbIdNo="";

    /**
     * 批次号
     */
    @ToString.Include
    private String gbBatchNo="";

    /**
     * 生产日期
     */
    @ToString.Include
    private Date gbProduceTime;

    /**
     * 有效期效期
     */
    @ToString.Include
    private Date gbEndTime;

    /**
     * 库存数量
     */
    @ToString.Include
    private BigDecimal gbNumber=BigDecimal.ZERO;

    /**
     * 生产地址
     */
    @ToString.Include
    private String gbProduceAddress="";

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
            return gbIdNo;
        }
        return gbIdNo + OpenConstants.SPLITE_SYMBOL + suDeptNo;
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
        return ErpTopicName.ErpGoodsBatch.getMethod();
    }

}