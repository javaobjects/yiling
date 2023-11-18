package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 健康测评营销
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DelHealthEvaluateMarketForm extends BaseForm {

    @ApiModelProperty(value = "添加类型 1-关联药品，2-改善建议，3-推广服务")
    private Integer type;

    @ApiModelProperty(value = "id")
    private Long id;


}
