package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销条款 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesTermsBO extends BaseVO implements Serializable {

    /**
     * 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
     */
    @ApiModelProperty(value = "购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进")
    private Integer buyChannel;

    /**
     * 是否拉单支持：0-否 1-是
     */
    @ApiModelProperty(value = "是否拉单支持：0-否 1-是")
    private Integer pullOrderFlag;

    /**
     * 是否分销协议支持：0-否 1-是
     */
    @ApiModelProperty(value = "是否分销协议支持：0-否 1-是")
    private Integer distributionAgreementFlag;

    /**
     * 是否全系列品种：0-否 1-是
     */
    @ApiModelProperty(value = "是否全系列品种：0-否 1-是")
    private Integer allLevelKindsFlag;

    /**
     * 指定商业公司集合
     */
    @ApiModelProperty("指定商业公司集合")
    private List<AgreementSupplySalesEnterpriseBO> supplySalesEnterpriseList;

    /**
     * 供销商品组集合
     */
    @ApiModelProperty(value = "供销商品组集合")
    private List<AgreementSupplySalesGoodsGroupBO> supplySalesGoodsGroupList;

}
