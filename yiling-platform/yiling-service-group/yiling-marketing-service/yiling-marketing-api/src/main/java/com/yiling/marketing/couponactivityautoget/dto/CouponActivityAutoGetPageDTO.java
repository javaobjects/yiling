package com.yiling.marketing.couponactivityautoget.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class CouponActivityAutoGetPageDTO extends BaseDTO {

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
     * 状态（1-启用 2-停用 3-作废）
     */
    private Integer status;

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
     * 活动状态（1-未开始 2-进行中 3-已结束）
     */
    private Integer activityStatus;

    /**
     * 已领取数量
     */
    private Integer receivedNum;

    /**
     * 修改标识（true-可修改；false-不可修改）
     */
    private Boolean updateFlag;

}
