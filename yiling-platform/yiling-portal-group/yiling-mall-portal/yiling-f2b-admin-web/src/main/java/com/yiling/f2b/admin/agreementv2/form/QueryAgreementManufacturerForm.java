package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家管理列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/23
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementManufacturerForm extends QueryPageListForm {

    /**
     * 厂家编号
     */
    @Min(1)
    @ApiModelProperty("厂家编号")
    private Long eid;

    /**
     * 厂家名称
     */
    @Length(max = 50)
    @ApiModelProperty("厂家名称")
    private String ename;

}
