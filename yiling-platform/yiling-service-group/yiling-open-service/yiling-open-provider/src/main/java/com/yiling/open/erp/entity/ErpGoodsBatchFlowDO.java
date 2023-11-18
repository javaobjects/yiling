package com.yiling.open.erp.entity;

import java.io.IOException;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 库存流向
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_goods_batch_flow")
public class ErpGoodsBatchFlowDO extends BaseErpEntity {

    private String gbIdNo;

    private Date gbTime;

    private String inSn;

    private String gbBatchNo;

    private String gbProduceTime;

    private String gbEndTime;

    private String gbName;

    private String gbLicense;

    private String gbSpecifications;

    private String gbUnit;

    private String gbNumber;

    private String gbManufacturer;

    private String gbProduceAddress;

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

}
