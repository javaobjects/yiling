package com.yiling.sjms.flow.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 BatchSaveCustomerMappingForm
 * @描述
 * @创建时间 2023/3/13
 * @修改人 shichen
 * @修改时间 2023/3/13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchSaveCustomerMappingForm extends BaseForm {

    List<SaveFlowEnterpriseCustomerMappingForm> saveList;
}
