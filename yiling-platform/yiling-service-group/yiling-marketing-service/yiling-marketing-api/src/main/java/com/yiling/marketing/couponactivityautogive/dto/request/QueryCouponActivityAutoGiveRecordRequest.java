package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryCouponActivityAutoGiveRecordRequest extends QueryPageListRequest {

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
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 企业类型
     */
    private Integer etype;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 发放状态（1-已发放；2-发放失败）
     */
    private Integer status;

    /**
     * 创建时间-开始
     */
    private Date createBeginTime;

    /**
     * 创建时间-结束
     */
    private Date createEndTime;

}
