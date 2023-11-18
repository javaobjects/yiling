package com.yiling.user.agreementv2.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利范围 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateScopeRequest extends BaseRequest {

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
     * 控销类型：1-无 2-黑名单 3-白名单
     */
    private Integer controlSaleType;

    /**
     * 协议返利控销条件
     */
    private List<Integer> agreementRebateControlList;

    /**
     * 协议返利控销区域
     */
    private AddAgreementRebateControlAreaRequest addAgreementRebateControlArea;

    /**
     * 协议返利控销客户类型
     */
    private List<Integer> agreementRebateControlCustomerType;

    /**
     * 协议返利任务量阶梯集合（最多6个阶梯）
     */
    private List<AddAgreementRebateTaskStageRequest> agreementRebateTaskStageList;

}
