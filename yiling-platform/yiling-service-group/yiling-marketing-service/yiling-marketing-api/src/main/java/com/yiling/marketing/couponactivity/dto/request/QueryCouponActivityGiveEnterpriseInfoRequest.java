package com.yiling.marketing.couponactivity.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityGiveEnterpriseInfoRequest extends QueryPageListRequest {

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 优惠券活动名称
     */
    private String couponActivityName;

    /**
     * 企业id
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
     * 终端区域编码（所属省份编码）
     */
    private String regionCode;

    /**
     * 自动发券活动ID
     */
    private Long couponActivityAutoGiveId;

    /**
     * 自动发券活动名称
     */
    private String couponActivityAutoGiveName;

    /**
     * 所属企业ID
     */
    private Long ownEid;

    /**
     * 所属企业名称
     */
    private String ownEname;

    /**
     * 创建时间-开始
     */
    private Date createBeginTime;

    /**
     * 创建时间-结束
     */
    private Date createEndTime;

}
