package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询协议商品分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementGoodsPageForm extends QueryPageListForm {

    /**
     * 协议ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "协议ID", required = true)
    private Long id;





}
