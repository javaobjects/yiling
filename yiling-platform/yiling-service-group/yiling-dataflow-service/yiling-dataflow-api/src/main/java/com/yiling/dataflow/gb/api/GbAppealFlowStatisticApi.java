package com.yiling.dataflow.gb.api;

import java.util.List;

import com.yiling.dataflow.gb.bo.GbAppealFormFlowCountBO;
import com.yiling.dataflow.gb.dto.GbAppealFlowStatisticDTO;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
public interface GbAppealFlowStatisticApi {

    /**
     * 根据团购处理申请ID统计流向数量合计、匹配数量合计、未匹配数量合计
     *
     * @param gbAppealFormId
     * @return
     */
    GbAppealFormFlowCountBO getFlowCountByGbAppealFormId(Long gbAppealFormId);

    /**
     * 根据原流向ID查询
     *
     * @param flowWashId
     * @return
     */
    GbAppealFlowStatisticDTO getByFlowWashId(Long flowWashId);

    /**
     * 根据原流向ID列表查询
     *
     * @param flowWashIdList
     * @return
     */
    List<GbAppealFlowStatisticDTO> getByFlowWashIdList(List<Long> flowWashIdList);

}
