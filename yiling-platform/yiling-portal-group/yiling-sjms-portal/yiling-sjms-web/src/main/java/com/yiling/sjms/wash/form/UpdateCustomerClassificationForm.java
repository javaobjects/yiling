package com.yiling.sjms.wash.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerClassificationForm extends BaseForm {

    private List<Long> idList;

    private Integer customerClassification;

    private String remark;
}
