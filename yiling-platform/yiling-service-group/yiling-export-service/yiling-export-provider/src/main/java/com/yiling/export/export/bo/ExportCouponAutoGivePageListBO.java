package com.yiling.export.export.bo;

import java.util.Date;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2021/12/16
 */
@Data
public class ExportCouponAutoGivePageListBO {

    /**
     * ID
     */
    private Long id;

    /**
     * 自动发券活动名称
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
     * 活动时间
     */
    private String effectiveTime;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    private Integer type;

    /**
     * 自动发券类型（1-订单累计金额；2-会员自动发券）
     */
    private String typeStr;

    /**
     * 累计金额/数量（如果是订单累计金额，则表示累计金额。如果是订单累计数量，则表示累计订单数量。）
     */
    private Integer cumulative;

    /**
     * 最多发放次数
     */
    private Integer maxGiveNum;

    /**
     * 已发放数量
     */
    private Integer giveCount = 0;

    /**
     * 状态（1-启用；2-停用；3-废弃）
     */
    private Integer status;

    /**
     * 状态（1-启用；2-停用；3-废弃）
     */
    private String statusStr;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private String createTimeStr;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改时间
     */
    private String updateTimeStr;

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
    private String activityStatus;

}
