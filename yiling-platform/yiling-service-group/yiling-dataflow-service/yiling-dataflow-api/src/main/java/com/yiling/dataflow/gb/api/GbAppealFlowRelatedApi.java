package com.yiling.dataflow.gb.api;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.dataflow.gb.dto.GbAppealFlowRelatedDTO;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
public interface GbAppealFlowRelatedApi {

    /**
     * 通过流向团购申请处理ID列表获取关联流向列表
     *
     * @param appealFormIdList
     * @return
     */
    List<GbAppealFlowRelatedDTO> getByAppealFormIdList(List<Long> appealFormIdList);

    /**
     * 通过流向团购申请处理ID获取关联流向列表
     *
     * @param appealFormId
     * @return
     */
    List<GbAppealFlowRelatedDTO> getListByAppealFormId(Long appealFormId);

    /**
     * 根据团购处理申请ID、原流向ID，查询关联
     *
     * @param appealFormId
     * @param flowWashId
     * @return
     */
    GbAppealFlowRelatedDTO getByAppealFormIdAndFlowWashId(Long appealFormId, Long flowWashId);
}
