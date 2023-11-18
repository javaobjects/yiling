package com.yiling.sjms.flee.service;

import java.util.List;

import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.entity.SalesAppealExtFormDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 销量申诉拓展表单 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
public interface SalesAppealExtFormService extends BaseService<SalesAppealExtFormDO> {

    SalesAppealExtFormDTO getByFormId(Long formId);

    /**
     * 查询日程未开启，销量申诉确认已提交生成流向表单申请，未清洗的表单id
     * @return
     */
    List<Long> getSubmitUnCleaned();
}
