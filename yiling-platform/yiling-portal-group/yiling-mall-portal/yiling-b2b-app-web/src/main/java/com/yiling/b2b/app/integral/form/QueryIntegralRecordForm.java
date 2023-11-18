package com.yiling.b2b.app.integral.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分发放/扣减记录 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralRecordForm extends QueryPageListForm {

    /**
     * 类型：1-发放 2-扣减
     */
    @ApiModelProperty(value = "类型：1-收入 2-支出")
    private Integer type;

}
