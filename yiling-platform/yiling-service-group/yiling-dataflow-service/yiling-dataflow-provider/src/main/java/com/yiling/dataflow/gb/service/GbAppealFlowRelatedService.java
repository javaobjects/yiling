package com.yiling.dataflow.gb.service;

import java.util.List;

import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向团购申诉申请关联流向表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
public interface GbAppealFlowRelatedService extends BaseService<GbAppealFlowRelatedDO> {

    /**
     * 通过流向团购申请处理ID获取关联流向列表
     *
     * @param appealFormId
     * @return
     */
    List<GbAppealFlowRelatedDO> getListByAppealFormId(Long appealFormId);

    /**
     * 通过流向团购申请处理ID列表获取关联流向列表
     *
     * @param appealFormIdList
     * @return
     */
    List<GbAppealFlowRelatedDO> getByAppealFormIdList(List<Long> appealFormIdList);

    /**
     * 根据团购处理申请ID、原流向ID，查询关联
     *
     * @param appealFormId
     * @param flowWashId
     * @return
     */
    GbAppealFlowRelatedDO getByAppealFormIdAndFlowWashId(Long appealFormId, Long flowWashId);

    boolean updateById(SubstractGbAppealFlowRelatedRequest request);

    int deleteById(DeleteGbAppealFlowRelatedRequest request);

    /**
     * 根据团购处理id统计关联源流向数量
     *
     * @param appealFormId
     * @return
     */
    int countByAppealFormId(Long appealFormId);
}
