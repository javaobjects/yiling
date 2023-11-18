package com.yiling.marketing.couponactivity.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponActivityRequest extends QueryPageListRequest {

    /**
     * 优惠券名称ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 预算编号
     */
    private String budgetCode;

    /**
     * 优惠券活动类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 活动分类（1-平台活动；2-商家活动）
     */
    private Integer sponsorType;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    private Integer status;

    /**
     * 用券时间（1-固定时间；2-发放领取后生效）
     */
    private Integer useDateType;

    /**
     * 用券开始时间
     */
    private Date beginTime;

    /**
     * 用券结束时间
     */
    private Date endTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间-开始时间
     */
    private Date createBeginTime;

    /**
     * 创建时间-结束时间
     */
    private Date createEndTime;

    /**
     * 当前登录账号所属企业ID
     */
    @NotNull
    private Long currentEid;

    /**
     * 活动状态：1-未开始 2-进行中 3-已结束
     */
    private Integer activityStatus;

    /**
     * 优惠券可用企业id
     */
    private Long availableEid;

    /**
     * 1商品券 2会员券
     */
    private Integer memberType=1;

    /**
     * 1全部会员方案 ，2部分会员方案
     */
    private Integer memberLimit;

    /**
     * 运营备注
     */
    private String remark;

    /**
     * 创建人id
     */
    private List<Long> creatUserIds;

    /**
     * 优惠券可id
     */
    private Long couponActivityId;
}
