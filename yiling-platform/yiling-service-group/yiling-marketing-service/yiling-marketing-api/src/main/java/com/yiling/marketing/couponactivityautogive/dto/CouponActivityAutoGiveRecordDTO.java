package com.yiling.marketing.couponactivityautogive.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityAutoGiveRecordDTO extends BaseDTO {

    /**
     * 自动发券活动ID
     */
    private Long couponActivityAutoGiveId;

    /**
     * 自动发券活动名称
     */
    private String couponActivityAutoGiveName;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 惠券活动名称
     */
    private String couponActivityName;

    /**
     * 获取方式（1-运营发放；2-自动发放）
     */
    private Integer getType;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 发放数量
     */
    private Integer giveNum;

    /**
     * 发放状态（1-已发放；2-发放失败）
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String faileReason;

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
     * 所属企业名称
     */
    private String batchNumber;

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
