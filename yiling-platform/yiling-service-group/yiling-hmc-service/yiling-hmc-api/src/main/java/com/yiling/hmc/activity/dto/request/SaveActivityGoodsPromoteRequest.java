package com.yiling.hmc.activity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveActivityGoodsPromoteRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 活动状态 1-启用，2-停用
     */
    private Integer activityStatus;

    /**
     * 活动类型 2-医带患，3-八子补肾
     */
    private Integer activityType;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动描述
     */
    private String activityDesc;

}
