package com.yiling.marketing.couponactivityautogive.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveEnterpriseInfoDTO extends BaseDTO {

    /**
     * 自动发券活动ID
     */
    private Long couponActivityAutoGiveId;

    /**
     * 自动发券活动名称
     */
    private String couponActivityAutoGiveName;

    /**
     * 获取方式（1-运营发放；2-自动发放）
     */
    private Integer getType;

    /**
     * 被发放企业ID
     */
    private Long eid;

    /**
     * 被发放企业名称
     */
    private String ename;

    /**
     * 企业类型，字典enterprise_type：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer etype;

    /**
     * 所属区域编码（所属省份编码）
     */
    private String regionCode;

    /**
     * 所属区域名称（所属省份名称）
     */
    private String regionName;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    private Integer authStatus;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 已发放数量
     */
    private Integer giveNum;

    /**
     * 累计金额
     */
    private BigDecimal cumulativeAmount;

    /**
     * 所属企业ID
     */
    private Long ownEid;

    /**
     * 所属企业名称
     */
    private String ownEname;

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
     * 备注
     */
    private String remark;

}
