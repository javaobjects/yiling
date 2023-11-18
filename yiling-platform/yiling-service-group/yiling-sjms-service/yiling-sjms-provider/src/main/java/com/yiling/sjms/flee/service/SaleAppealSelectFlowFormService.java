package com.yiling.sjms.flee.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.entity.SaleAppealSelectFlowFormDO;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
public interface SaleAppealSelectFlowFormService extends BaseService<SaleAppealSelectFlowFormDO> {
    boolean saveSelectAppealFlowData(SaveSelectAppealFlowFormRequest request);

    /**
     * 根据id删除销售申诉选择流向数据
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean removeById(RemoveSelectAppealFlowFormRequest request);

    /**
     * 销售申诉选择流向数据列表
     *
     * @param formId 查询条件
     * @return 窜货申报数据
     */
    List<SaleAppealSelectFlowFormDO> ListByFormId(Long formId, Integer type);

    String saveAppealFlowDataAll(SaveSelectAppealFlowFormRequest request);

    /**
     * 查询销量申诉选择流向数据列表
     * @param formId
     * @param type
     * @return
     */
    List<SaleAppealSelectFlowFormDO> ListByFormIdAndType(Long formId, Integer type);
}
