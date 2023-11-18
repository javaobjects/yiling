package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利范围 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateScopeBO extends BaseVO implements Serializable {

    /**
     * 控销类型：1-无 2-黑名单 3-白名单
     */
    @ApiModelProperty("控销类型：1-无 2-黑名单 3-白名单 (见字典：agreement_control_sale_type)")
    private Integer controlSaleType;

    /**
     * 协议返利控销条件：1-区域 2-客户类型（只有控销类型不选择无，才可能会有控销条件）
     */
    @ApiModelProperty("协议返利控销条件：1-区域 2-客户类型（只有控销类型不选择无，才可能会有控销条件）")
    private List<Integer> agreementRebateControlList;

    /**
     * 协议返利控销区域
     */
    @ApiModelProperty("协议返利控销区域")
    private AgreementRebateControlAreaBO agreementRebateControlArea;

    /**
     * 协议返利控销客户类型
     */
    @ApiModelProperty("协议返利控销客户类型")
    private List<Integer> agreementRebateControlCustomerType;

    /**
     * 协议返利任务量阶梯集合
     */
    @ApiModelProperty("协议返利任务量阶梯集合（最多6个阶梯）")
    private List<AgreementRebateTaskStageBO> agreementRebateTaskStageList;

}