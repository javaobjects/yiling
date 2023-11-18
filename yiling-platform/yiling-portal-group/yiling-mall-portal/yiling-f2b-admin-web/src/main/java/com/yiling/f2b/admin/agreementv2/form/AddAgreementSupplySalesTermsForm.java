package com.yiling.f2b.admin.agreementv2.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议供销条款 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSupplySalesTermsForm extends BaseForm {

    /**
     * 购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进
     */
    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "购进渠道：1-直供 2-所有商业公司购进 3-指定商业公司购进",required = true)
    private Integer buyChannel;

    /**
     * 是否拉单支持：0-否 1-是
     */
    @NotNull
    @ApiModelProperty(value = "是否拉单支持：0-否 1-是",required = true)
    private Integer pullOrderFlag;

    /**
     * 是否分销协议支持：0-否 1-是
     */
    @NotNull
    @ApiModelProperty(value = "是否分销协议支持：0-否 1-是",required = true)
    private Integer distributionAgreementFlag;

    /**
     * 是否全系列品种：0-否 1-是
     */
    @NotNull
    @ApiModelProperty(value = "是否全系列品种：0-否 1-是",required = true)
    private Integer allLevelKindsFlag;

    /**
     * 指定商业公司集合(只有购进渠道选择指定商业公司才传入值)
     */
    @ApiModelProperty("指定商业公司集合（只有购进渠道选择指定商业公司，才传入值）")
    private List<AddAgreementSupplySalesEnterpriseForm> supplySalesEnterpriseList;

    /**
     * 供销商品组集合
     */
    @ApiModelProperty(value = "供销商品组集合（只有是否全系列品种字段选择否，才传入值）")
    private List<AddAgreementSupplySalesGoodsGroupForm> supplySalesGoodsGroupList;

}
