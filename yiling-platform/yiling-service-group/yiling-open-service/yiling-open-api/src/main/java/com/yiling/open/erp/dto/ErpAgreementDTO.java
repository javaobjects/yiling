package com.yiling.open.erp.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
@Data
public class ErpAgreementDTO implements java.io.Serializable{

    /**
     * 年度
     */
    @JSONField(name = "year",ordinal = 20)
    private String year;

    /**
     * 月份
     */
    @JSONField(name = "month",ordinal = 20)
    private String month;

    /**
     * 商品
     */
    @JSONField(name = "goods_name",ordinal = 20)
    private String goodsName;

    /**
     * 申请单id
     */
    @JSONField(name = "apply_id",ordinal = 20)
    private Long applyId;

    /**
     * 申请单号
     */
    @JSONField(name = "apply_code",ordinal = 20)
    private String applyCode;

    /**
     * 销售组织code
     */
    @JSONField(name = "seller_code",ordinal = 20)
    private String sellerCode;

    /**
     * 销售组织
     */
    @JSONField(name = "seller_name",ordinal = 20)
    private String sellerName;

    /**
     * 入账企业code
     */
    @JSONField(name = "entry_code",ordinal = 20)
    private String entryCode;

    /**
     * 入账企业
     */
    @JSONField(name = "entry_name",ordinal = 20)
    private String entryName;

    /**
     * 所属企业code
     */
    @JSONField(name = "owner_code",ordinal = 20)
    private String ownerCode;

    /**
     * 所属企业
     */
    @JSONField(name = "owner_name",ordinal = 20)
    private String ownerName;

    /**
     * 冲红金额
     */
    @JSONField(name = "total_amount",ordinal = 20)
    private BigDecimal totalAmount;

    /**
     * 协议名称
     */
    @JSONField(name = "agreement_name",ordinal = 20)
    private String agreementName;

    /**
     * 协议描述
     */
    @JSONField(name = "agreement_content",ordinal = 20)
    private String agreementContent;

    /**
     * 省份
     */
    @JSONField(name = "province_name",ordinal = 20)
    private String provinceName;

    /**
     * 返利种类
     */
    @JSONField(name = "rebate_category",ordinal = 20)
    private String rebateCategory;

    /**
     * 费用科目
     */
    @JSONField(name = "cost_subject",ordinal = 20)
    private String costSubject;

    /**
     * 费用归属部门
     */
    @JSONField(name = "cost_dept",ordinal = 20)
    private String costDept;

    /**
     * 执行部门
     */
    @JSONField(name = "execute_dept",ordinal = 20)
    private String executeDept;

    /**
     * 批复代码
     */
    @JSONField(name = "reply_code",ordinal = 20)
    private String replyCode;

    /**
     * 申请时间
     */
    @JSONField(name = "create_time",format="yyyy-MM-dd HH:mm:ss",ordinal = 20)
    private Date createTime;

    /**
     * 申请人
     */
    @JSONField(name = "create_user",ordinal = 20)
    private Long createUser;

    /**
     * 申请人工号
     */
    @JSONField(name = "create_user_code",ordinal = 20)
    private String createUserCode;

    /**
     * 入账时间
     */
    @JSONField(name = "entry_time",format="yyyy-MM-dd HH:mm:ss",ordinal = 20)
    private Date entryTime;

    /**
     * 订单明细
     */
    @JSONField(name = "agreement_detail_list",ordinal = 25)
    private List<ErpAgreementDetailDTO> details;
}
