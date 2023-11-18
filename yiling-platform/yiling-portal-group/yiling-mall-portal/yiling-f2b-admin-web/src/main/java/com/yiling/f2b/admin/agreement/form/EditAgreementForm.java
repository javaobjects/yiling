package com.yiling.f2b.admin.agreement.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditAgreementForm extends BaseForm {

	/**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "协议id")
    private Long id;

    /**
     * 协议商品列表
     */
    @ApiModelProperty(value = "协议商品列表")
    private List<SaveAgreementGoodsForm> agreementGoodsList;

}
