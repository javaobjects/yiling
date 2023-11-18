package com.yiling.f2b.admin.agreementv2.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新增厂家商品 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/23
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementManufacturerGoodsForm extends BaseForm {

    /**
     * 厂家ID
     */
    @NotNull
    @ApiModelProperty("厂家ID")
    private Long manufacturerId;

    /**
     * 商品集合
     */
    @NotEmpty
    @ApiModelProperty("商品集合")
    private List<AgreementManufacturerGoodsForm> list;

}
