package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPurchasePageForm extends QueryPageListForm {

    /**
     * 商业eid
     */
    @ApiModelProperty(value = "eid")
    private Long eid;

    /**
     * 以岭品id
     */
    @ApiModelProperty(value = "以岭品id")
    private Long ylGoodsId;


}