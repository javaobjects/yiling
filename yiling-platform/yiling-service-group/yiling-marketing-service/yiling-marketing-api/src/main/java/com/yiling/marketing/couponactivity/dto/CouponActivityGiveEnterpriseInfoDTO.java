package com.yiling.marketing.couponactivity.dto;

import com.yiling.framework.common.base.BaseDTO;

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
public class CouponActivityGiveEnterpriseInfoDTO extends BaseDTO {

    /**
     * 优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 获取方式（1-运营发放；2-自动发放）
     */
    private Integer getType;

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
     * 终端区域编码
     */
    private String regionCode;

    /**
     * 终端区域名称
     */
    private String regionName;

    /**
     * 认证状态（1-未认证 2-认证通过 3-认证不通过）
     */
    private Integer authStatus;

}
