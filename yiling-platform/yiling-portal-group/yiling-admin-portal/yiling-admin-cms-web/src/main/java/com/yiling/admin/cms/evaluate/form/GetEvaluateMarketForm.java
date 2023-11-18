package com.yiling.admin.cms.evaluate.form;

import com.yiling.framework.common.base.form.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2022-12-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetEvaluateMarketForm extends BaseForm {

    private Long id;
}