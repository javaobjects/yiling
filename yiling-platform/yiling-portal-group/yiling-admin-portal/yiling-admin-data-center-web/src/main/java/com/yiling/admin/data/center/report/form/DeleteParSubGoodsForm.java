package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数-商品类型
 * </p>
 *
 * @author dexi.yao
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteParSubGoodsForm extends BaseForm {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty("id---修改时必填")
    private Long id;

}
