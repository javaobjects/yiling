package com.yiling.user.agreementv2.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议供销条款 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSupplySalesTermsRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进（见枚举：AgreementBuyChannelEnum）
     */
    private Integer buyChannel;

    /**
     * 是否拉单支持：0-否 1-是
     */
    private Integer pullOrderFlag;

    /**
     * 是否分销协议支持：0-否 1-是
     */
    private Integer distributionAgreementFlag;

    /**
     * 是否全系列品种：0-否 1-是
     */
    private Integer allLevelKindsFlag;

    /**
     * 指定商业公司集合(只有购进渠道选择指定商业公司才传入值)
     */
    private List<AddAgreementSupplySalesEnterpriseRequest> supplySalesEnterpriseList;

    /**
     * 供销商品组集合（最多6个商品组）
     */
    private List<AddAgreementSupplySalesGoodsGroupRequest> supplySalesGoodsGroupList;

}
