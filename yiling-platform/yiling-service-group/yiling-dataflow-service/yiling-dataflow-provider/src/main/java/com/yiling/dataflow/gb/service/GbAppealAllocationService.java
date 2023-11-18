package com.yiling.dataflow.gb.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFlowAllocationRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormAllocationPageRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowAllocationRequest;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向团购申诉分配结果表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-05-15
 */
public interface GbAppealAllocationService extends BaseService<GbAppealAllocationDO> {

    /**
     * 根据团购申诉申请ID，查询团购数据处理扣减/增加 列表分页
     *
     * @param request
     * @return
     */
    Page<GbAppealAllocationDO> flowStatisticListPage(QueryGbAppealFormAllocationPageRequest request);

    /**
     * 根据团购申诉申请ID、分配类型，查询团购处理结果列表
     *
     * @param appealFormId
     * @param allocationType
     * @return
     */
    List<GbAppealAllocationDO> listByAppealFormIdAndAllocationType(Long appealFormId, Integer allocationType);

    /**
     * 保存团购流向处理结果
     *
     * @param list
     * @return
     */
    List<Long> saveOrUpdateAllocationList(List<SubstractGbAppealFlowAllocationRequest> list);

    /**
     * 根据团购处理id、源流向id查询列表
     *
     * @param appealFormId
     * @param flowWashId
     * @param allocationType
     * @return
     */
    List<GbAppealAllocationDO> listByAppealFormIdAndFlowWashId(Long appealFormId, Long flowWashId, Integer allocationType);


    int deleteByIdList(DeleteGbAppealFlowAllocationRequest request);

}
