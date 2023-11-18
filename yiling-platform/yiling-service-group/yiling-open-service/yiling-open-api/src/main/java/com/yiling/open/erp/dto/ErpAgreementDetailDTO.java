package com.yiling.open.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
@Data
public class ErpAgreementDetailDTO implements java.io.Serializable{
    /**
     * 返利申请表id
     */
    @JSONField(name = "apply_id",ordinal = 20)
    private Long applyId;

    /**
     * 申请单号
     */
    @JSONField(name = "apply_code",ordinal = 20)
    private String applyCode;

    /**
     * 明细类型 1-协议类型 2-其他
     */
    @JSONField(name = "detail_type",ordinal = 20)
    private Integer detailType;


    /**
     * 返利金额
     */
    @JSONField(name = "amount",ordinal = 20)
    private BigDecimal amount;

    /**
     * 销售组织id
     */
    @JSONField(name = "seller_eid",ordinal = 20)
    private Long sellerEid;

    /**
     * 销售组织名称
     */
    @JSONField(name = "seller_name",ordinal = 20)
    private String sellerName;

    /**
     * 销售组织easCode
     */
    @JSONField(name = "seller_code",ordinal = 20)
    private String sellerCode;

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
     * 描述
     */
    @JSONField(name = "describe",ordinal = 20)
    private String describe;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time",format="yyyy-MM-dd HH:mm:ss",ordinal = 20)
    private Date createTime;

    /**
     * 创建人
     */
    @JSONField(name = "create_user",ordinal = 20)
    private Long createUser;

    /**
     * 修改时间
     */
    @JSONField(name = "update_time",format="yyyy-MM-dd HH:mm:ss",ordinal = 20)
    private Date updateTime;

    /**
     * 修改人
     */
    @JSONField(name = "update_user",ordinal = 20)
    private Long updateUser;

    /**
     * 备注
     */
    @JSONField(name = "remark",ordinal = 20)
    private String remark;
}
