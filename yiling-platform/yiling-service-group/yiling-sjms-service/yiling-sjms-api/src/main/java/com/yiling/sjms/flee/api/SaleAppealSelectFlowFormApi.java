package com.yiling.sjms.flee.api;

import java.util.List;

import com.yiling.sjms.flee.dto.SaleAppealSelectFlowFormDTO;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSelectAppealFlowFormRequest;

/**
 * 销售申诉选择流向
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
public interface SaleAppealSelectFlowFormApi {

    /**
     * 选择流向数据新增
     *
     * @param request 选择流向数据
     * @return
     */
    boolean saveSelectAppealFlowData(SaveSelectAppealFlowFormRequest request);
    /**
     * 根据id删除销售申诉选择流向数据
     *
     * @param request 删除条件
     * @return 成功/失败
     */
    boolean removeById(RemoveSelectAppealFlowFormRequest request);

    /**
     * 销售申诉选择流向列表数据
     * @param formId
     * @param type
     * @return
     */
    List<SaleAppealSelectFlowFormDTO> selectAppealFlowPageList(Long formId, Integer type);

    /**
     * 销售申诉选择流向列表数据
     * @param formId
     * @param type
     * @return
     */
    List<SaleAppealSelectFlowFormDTO> selectAppealFlowList(Long formId, Integer type);

    /**
     * 选择流向批量新增
     * @param request
     * @return
     */
    String saveAppealFlowDataAll(SaveSelectAppealFlowFormRequest request);
}
