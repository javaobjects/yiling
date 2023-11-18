package com.yiling.f2b.admin.agreementv2.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.user.agreementv2.dto.AgreementStatusLogDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel("协议详情VO")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementDetailVO extends BaseVO {

    /**
     * 协议主条款
     */
    @ApiModelProperty("协议主条款")
    private AgreementMainTermsVO agreementMainTerms;

    /**
     * 协议供销条款
     */
    @ApiModelProperty("协议供销条款")
    private AgreementSupplySalesTermsVO agreementSupplySalesTerms;

    /**
     * 协议结算条款
     */
    @ApiModelProperty("协议结算条款")
    private AgreementSettlementTermsVO agreementSettlementTerms;

    /**
     * 协议返利条款
     */
    @ApiModelProperty("协议返利条款")
    private AgreementRebateTermsVO agreementRebateTerms;

    /**
     * 协议审核记录
     */
    @ApiModelProperty("协议审核记录")
    private List<AgreementStatusLogVO> agreementStatusLogList;

}
