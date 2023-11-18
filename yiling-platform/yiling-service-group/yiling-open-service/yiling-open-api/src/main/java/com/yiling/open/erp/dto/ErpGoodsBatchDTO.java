package com.yiling.open.erp.dto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;
import com.yiling.open.erp.validation.group.Delete;

import lombok.Data;
import lombok.ToString;

/**
 * 药品库房批次表 erp_goods_batch
 *
 * @author 张爽
 * @date 2020-6-14
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpGoodsBatchDTO extends BaseErpEntity {

    // 药品内码（供应商的ERP的商品主键）
    @NotEmpty(message = "不能为空")
    @JSONField(name = "in_sn")
    @ToString.Include
    private String inSn="";

    // erp的库存编码id
    @NotEmpty(message = "不能为空", groups = Delete.class)
    @JSONField(name = "gb_id_no")
    @ToString.Include
    private String gbIdNo="";;

    // 批次号
    @JSONField(name = "gb_batch_no")
    @ToString.Include
    private String gbBatchNo="";

    // 生产日期
    @JSONField(name = "gb_produce_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date gbProduceTime;

    // 有效期效期
    @JSONField(name = "gb_end_time", format = "yyyy-MM-dd HH:mm:ss")
    @ToString.Include
    private Date gbEndTime;

    // 库存数量
    @NotNull(message = "不能为空")
    @Max(value = 99999999, message = "不能超过{value}")
    @JSONField(name = "gb_number")
    @ToString.Include
    private BigDecimal gbNumber=BigDecimal.ZERO;

    // 生产地址
    @JSONField(name = "gb_produce_address")
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