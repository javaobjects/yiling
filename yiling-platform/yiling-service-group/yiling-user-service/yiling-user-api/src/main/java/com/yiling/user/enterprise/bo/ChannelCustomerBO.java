package com.yiling.user.enterprise.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;


/**
 *  企业渠道商 BO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/5 0005
 */
@Data
public class ChannelCustomerBO {
    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户ID
     */
    private Long customerEid;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

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
     * 渠道名称
     */
    private String channelName;

    /**
     * 采购关系个数
     */
    private Integer purchaseRelationNum;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 还款周期
     */
    private Integer period;

    /**
     * 账期额度
     */
    private BigDecimal useLimit;

    /**
     * 商务联系人个数
     */
    private Integer customerContactNum;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 地址
     */
    private String address;

    /**
     * 认证状态
     */
    private Integer authStatus;

    /**
     * 渠道ID
     */
    private Long channelId;

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
}
