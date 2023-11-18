package com.yiling.f2b.admin.agreement.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementGoodsPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "年度协议ID")
    @NotNull
    private Long agreementId;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 0-全部 1-非专利 2-专利")
    private Integer isPatent;

    @ApiModelProperty(value = "临时协议ID")
    private Long tempAgreementId;

    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

}
