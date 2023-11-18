package com.yiling.marketing.couponactivityautoget.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGetRulesRequest extends BaseRequest {

    /**
     * id
     */
    @NotNull
    private Long id;

    /**
     * 客户范围1-全部客户 2-指定客户 3指定范围客户
     */
    @NotNull
    private Integer conditionEnterpriseRange;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    @NotNull
    private Integer conditionEnterpriseType;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员； 5-部分用户）
     * CouponGetEnterpriseLimitTypeEnum
     */
    private Integer conditionUserType;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作人id
     */
    private Long currentUserId;

}
