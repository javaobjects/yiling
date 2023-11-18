package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
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
@Accessors(chain = true)
public class AgreementDetailBO implements Serializable {

    /**
     * 协议主条款
     */
    private AgreementMainTermsBO agreementMainTerms;

    /**
     * 协议供销条款
     */
    private AgreementSupplySalesTermsBO agreementSupplySalesTerms;

    /**
     * 协议结算条款
     */
    private AgreementSettlementTermsBO agreementSettlementTerms;

    /**
     * 协议返利条款
     */
    private AgreementRebateTermsBO agreementRebateTerms;

    /**
     * 协议审核记录
     */
    private List<AgreementStatusLogDTO> agreementStatusLogList;

}
