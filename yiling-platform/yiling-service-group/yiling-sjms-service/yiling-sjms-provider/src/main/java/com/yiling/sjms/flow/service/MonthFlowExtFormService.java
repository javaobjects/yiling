package com.yiling.sjms.flow.service;

import com.yiling.sjms.flow.dto.MonthFlowExtFormDTO;
import com.yiling.sjms.flow.entity.MonthFlowExtFormDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 补传月流向拓展表单 服务类
 * </p>
 *
 * @author gxl
 * @date 2023-06-25
 */
public interface MonthFlowExtFormService extends BaseService<MonthFlowExtFormDO> {
    MonthFlowExtFormDO getByFormId(Long formId);

    MonthFlowExtFormDTO queryAppendix(Long formId);
}
