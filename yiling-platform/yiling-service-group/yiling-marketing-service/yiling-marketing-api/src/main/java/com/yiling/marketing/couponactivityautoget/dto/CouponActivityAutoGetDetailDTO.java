package com.yiling.marketing.couponactivityautoget.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDetailDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGetDetailDTO extends BaseDTO {

    /**
     * 自主领券活动名称
     */
    private String name;

    /**
     * 活动开始时间
     */
    private Date beginTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    private Integer conditionEnterpriseType;

    /**
     * 指定企业类型(1:全部 2:指定)
     */
    private Integer conditionEnterpriseRange;

    /**
     * 已选企业类型，多个值用逗号隔开，字典enterprise_type（1-工业；2-商业；3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药房；8-诊所；9-民营医院；10-三级医院；11-二级医院；12-社区中心;13-县镇卫生院;14-社区站/村卫生所;15-县人民/中医院）
     */
    private List<String> conditionEnterpriseTypeValueList;

    /**
     * 指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-部分指定会员）
     */
    private Integer conditionUserType;

    /**
     * 状态：1-启用 2-停用 3-作废
     */
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

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
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 修改人姓名
     */
    private String updateUserName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关联优惠券列表
     */
    private List<CouponActivityAutoGiveCouponDetailDTO> couponActivityList;

}
