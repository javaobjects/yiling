package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新增厂家 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/23
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementManufacturerForm extends BaseForm {

    /**
     * 厂家编号
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("厂家编号")
    private Long eid;

    /**
     * 厂家名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty("厂家名称")
    private String ename;

    /**
     * 厂家类型 1-生产厂家 2-品牌厂家
     */
    @NotNull
    @Range(min = 1,max = 2)
    @ApiModelProperty("厂家类型 1-生产厂家 2-品牌厂家")
    private Integer type;

}
