package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利任务量阶梯 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateTaskStageRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 商品组ID
     */
    private Long groupId;

    /**
     * 协议返利范围ID
     */
    private Long rebateScopeId;

    /**
     * 任务量
     */
    private BigDecimal taskNum;

    /**
     * 任务量单位：1-元 2-盒
     */
    private Integer taskUnit;

    /**
     * 超任务量汇总返
     */
    private BigDecimal overSumBack;

    /**
     * 超任务量汇总返单位：1-元 2-%
     */
    private Integer overSumBackUnit;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 协议返利阶梯集合
     */
    private List<AddAgreementRebateStageRequest> agreementRebateStageList;

}
