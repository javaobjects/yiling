package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利阶梯 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateStageRequest extends BaseRequest {

    /**
     * 返利任务量阶梯ID
     */
    private Long taskStageId;

    /**
     * 满
     */
    private BigDecimal full;

    /**
     * 满单位：1-元 2-盒
     */
    private Integer fullUnit;

    /**
     * 返
     */
    private BigDecimal back;

    /**
     * 返单位：1-元 2-%
     */
    private Integer backUnit;

    /**
     * 排序
     */
    private Integer sort;

}
