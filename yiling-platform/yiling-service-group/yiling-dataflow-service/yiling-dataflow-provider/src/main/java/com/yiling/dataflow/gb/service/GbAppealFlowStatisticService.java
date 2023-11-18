package com.yiling.dataflow.gb.service;

import java.util.List;

import com.yiling.dataflow.gb.bo.GbAppealFormFlowCountBO;
import com.yiling.dataflow.gb.dto.request.SaveOrUpdateGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowStatisticDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向团购统计表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
public interface GbAppealFlowStatisticService extends BaseService<GbAppealFlowStatisticDO> {

    boolean saveOrUpdateByFlowWashId(SaveOrUpdateGbAppealFlowStatisticRequest saveOrUpdateGbAppealFlowStatisticRequest);

    List<GbAppealFlowStatisticDO> getListByFlowWashIds(List<Long> flowWashIds);

    int substract(SubstractGbAppealFlowStatisticRequest request);

    /**
     * 根团购处理申请ID统计流向数量合计、匹配数量合计、未匹配数量合计
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
    GbAppealFlowStatisticDO getByFlowWashId(Long flowWashId);

    /**
     * 根据原流向ID列表查询
     *
     * @param flowWashIdList
     * @return
     */
    List<GbAppealFlowStatisticDO> getByFlowWashIdList(List<Long> flowWashIdList);

}
