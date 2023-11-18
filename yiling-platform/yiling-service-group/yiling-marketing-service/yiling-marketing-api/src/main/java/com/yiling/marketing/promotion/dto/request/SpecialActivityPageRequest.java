package com.yiling.marketing.promotion.dto.request;


import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动主表新增请求参数
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SpecialActivityPageRequest extends QueryPageListRequest {

    /**
     * 促销活动名称
     */
    private Long id;

    /**
     * 促销活动名称
     */
    private String specialActivityName;


    /**
     * 预约人姓名
     */
    private String appointmentUserName;

    /**
     * 营销活动参与企业
     */
    private String specialActivityEnterpriseName;

    /**
     * 活动类型（活动类型 1-满赠，2-特价，3-秒杀, 4-组合包）
     */
    private Integer type;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;


    /**
     * 开始-开始时间
     */
    private Date beginStartTime;

    /**
     * 开始-结束时间
     */
    private Date beginEndTime;

    /**
     * 预约开始时间
     */
    private Date appointmentStartTime;

    /**
     * 预约结束时间
     */
    private Date appointmentEndTime;

    /**
     * 企业名称
     */
    private String eName;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

}
