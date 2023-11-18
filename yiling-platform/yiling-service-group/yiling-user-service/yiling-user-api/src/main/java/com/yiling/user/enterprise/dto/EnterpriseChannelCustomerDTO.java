package com.yiling.user.enterprise.dto;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业渠道商 DTO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/4 0004
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseChannelCustomerDTO extends EnterpriseCustomerDTO{

    /**
     * 状态：启用 停用
     */
    private Integer status;

    /**
     * 企业状态
     */
    private String enterpriseStatus;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 类型
     */
    private String customerType;

    /**
     * 渠道Id
     */
    private Long channelId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 采购关系个数
     */
    private Integer purchaseRelationNum;

    /**
     * 商务联系人个数
     */
    private Integer customerContactNum;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 客户分组ID
     */
    private Long customerGroupId;

    /**
     * 客户分组
     */
    private String customerGroupName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 商务联系人（联系电话）
     */
    private String customerContactInfo;

    /**
     * 支付方式（账期支付/预付款/无）
     */
    private String paymentMethod;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 认证状态
     */
    private Integer authStatus;
}
