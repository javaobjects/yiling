package com.yiling.marketing.couponactivity.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityLogRequest extends BaseRequest {

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 类型（1-新增；2-修改；3-手动发放；4-自动发放）
     */
    private Integer type;

    /**
     * 状态（1-成功；2-失败）
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String faileReason;

    /**
     * 内容json串
     */
    private String jsonContent;

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
