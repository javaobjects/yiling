package com.yiling.f2b.admin.agreementv2.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

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
@Accessors(chain = true)
public class DeleteAgreementManufacturerGoodsForm {

    /**
     * 厂家商品ID集合
     */
    @NotEmpty
    @ApiModelProperty("主键ID集合")
    private List<Long> idList;



}
