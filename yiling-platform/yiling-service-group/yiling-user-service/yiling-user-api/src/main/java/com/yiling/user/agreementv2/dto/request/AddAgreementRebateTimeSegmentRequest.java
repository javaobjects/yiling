package com.yiling.user.agreementv2.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议时段 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateTimeSegmentRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 时段类型：1-全时段 2-子时段
     */
    private Integer type;

    /**
     * 时段开始时间
     */
    private Date startTime;

    /**
     * 时段结束时间
     */
    private Date endTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否规模返利（ka协议时才可能存在）
     */
    private Boolean scaleRebateFlag;

    /**
     * 是否基础服务奖励（ka协议时才可能存在）
     */
    private Boolean basicServiceRewardFlag;

    /**
     * 是否项目服务奖励（ka协议时才可能存在）
     */
    private Boolean projectServiceRewardFlag;

    /**
     * 协议返利商品组集合（最多6个商品组）
     */
    private List<AddAgreementRebateGoodsGroupRequest> agreementRebateGoodsGroupList;

    /**
     * 规模返利阶梯集合（ka协议时才可能存在）
     */
    private List<AddAgreementRebateScaleRebateRequest> agreementScaleRebateList;

    /**
     * 基础服务奖励阶梯集合（ka协议时才可能存在）
     */
    private List<AddAgreementRebateBasicServiceRewardRequest> agreementRebateBasicServiceRewardList;

    /**
     * 项目服务奖励阶梯集合（ka协议时才可能存在）
     */
    private List<AddAgreementRebateProjectServiceRewardRequest> agreementRebateProjectServiceRewardList;

}
