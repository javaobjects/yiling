package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveCouponActivityGiveEnterpriseInfoRequest extends BaseRequest {

    /**
     * 所属企业ID
     */
    private Long ownEid;

    /**
     * 所属企业名称
     */
    private String ownEname;

    /**
     * 统一设置发放数量
     */
    @NotNull
    private Integer unifyGiveNum;

    /**
     * 添加发放供应商列表
     */
    @NotNull
    private List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList;

    /**
     * 优惠券维度的发放（根据优惠券ID查询已存在信息，进行新增/更新）
     */
    private Boolean couponActivityFlag;

    /**
     * 企业维度的发放（根据企业ID查询已存在信息，进行新增/更新）
     */
    private Boolean enterpriseFlag;

    /**
     * 操作人姓名
     */
    private String opUserName;

}
