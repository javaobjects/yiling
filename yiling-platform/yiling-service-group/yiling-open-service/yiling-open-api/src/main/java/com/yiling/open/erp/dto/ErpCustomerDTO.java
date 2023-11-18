package com.yiling.open.erp.dto;

import java.io.IOException;

import javax.validation.constraints.NotEmpty;

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
 * 企业信息实体
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@ToString(onlyExplicitlyIncluded = true)
@Data
public class ErpCustomerDTO extends BaseErpEntity {

    /**
     * 企业内码
     */
    @NotEmpty(message = "不能为空", groups = Delete.class)
    @JSONField(name = "inner_code")
    @ToString.Include
    protected String innerCode;

    /**
     * 企业编码
     */
    @JSONField(name = "sn")
    @ToString.Include
    private String   sn;

    /**
     * 企业名称
     */
    @JSONField(name = "name")
    @ToString.Include
    private String   name;

    /**
     * 企业分组名称
     */
    @JSONField(name = "group_name")
    @ToString.Include
    private String   groupName;

    /**
     * 营业执照号
     */
    @JSONField(name = "license_no")
    @ToString.Include
    private String   licenseNo;

    /**
     * 终端类型
     */
    @JSONField(name = "customer_type")
    @ToString.Include
    private String   customerType;

    /**
     * 客户类型对应的企业类型
     */
    private Integer enterpriseType;

    /**
     * 联系人
     */
    @JSONField(name = "contact")
    @ToString.Include
    private String   contact;

    /**
     * 区号-号码
     */
    @JSONField(name = "phone")
    @ToString.Include
    private String   phone;

    /**
     * 省份
     */
    @JSONField(name = "province")
    @ToString.Include
    private String   province;

    /**
     * 市
     */
    @JSONField(name = "city")
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
    @JSONField(name = "address")
    @ToString.Include
    private String   address;

    /**
     * 省份编码
     */
    private String   provinceCode;

    /**
     * 市编码
     */
    private String   cityCode;

    /**
     * 区域编码
     */
    private String   regionCode;

    /**
     * 初始化数据
     */
    public ErpCustomerDTO() {
        this.sn = "";
        this.name = "";
        this.groupName = "";
        this.licenseNo = "";
        this.customerType = "";
        this.contact = "";
        this.phone = "";
        this.province = "";
        this.city = "";
        this.region = "";
        this.address = "";
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
