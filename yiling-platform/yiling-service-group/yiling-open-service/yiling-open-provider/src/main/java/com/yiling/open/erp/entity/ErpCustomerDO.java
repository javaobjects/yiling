package com.yiling.open.erp.entity;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Data
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_customer")
public class ErpCustomerDO extends BaseErpEntity {

    /**
     * 企业内码
     */
    @ToString.Include
    protected String innerCode;

    /**
     * 企业编码
     */
    @ToString.Include
    private String   sn;

    /**
     * 企业名称
     */
    @ToString.Include
    private String   name;

    /**
     * 企业分组名称
     */
    @ToString.Include
    private String   groupName;

    /**
     * 营业执照号
     */
    @ToString.Include
    private String   licenseNo;

    /**
     * 终端类型
     */
    @ToString.Include
    private String   customerType;

    /**
     * 联系人
     */
    @ToString.Include
    private String   contact;

    /**
     * 区号-号码
     */
    @ToString.Include
    private String   phone;

    /**
     * 省份
     */
    @ToString.Include
    private String   province;

    /**
     * 市
     */
    @ToString.Include
    private String   city;

    /**
     * 区域
     */
    @JSONField(name = "region")
    @ToString.Include
    private String   region;

    /**
     * 企业地址
     */
    @ToString.Include
    private String   address;

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
            return innerCode;
        }
        return innerCode + OpenConstants.SPLITE_SYMBOL + suDeptNo;
    }

    @Override
    public String getTaskNo() {
        return ErpTopicName.ErpCustomer.getMethod();
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
