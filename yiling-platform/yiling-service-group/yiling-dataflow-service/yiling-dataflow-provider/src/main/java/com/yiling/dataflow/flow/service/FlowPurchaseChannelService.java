package com.yiling.dataflow.flow.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.entity.FlowPurchaseChannelDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelService
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
public interface FlowPurchaseChannelService extends BaseService<FlowPurchaseChannelDO> {

    /**
     * 分页查询采购渠道
     * @param request
     * @return
     */
    Page<FlowPurchaseChannelDTO> pageList(QueryFlowPurchaseChannelRequest request);

    /**
     * 保存采购渠道
     * @param request
     * @return
     */
    Long save(SaveFlowPurchaseChannelRequest request);

    /**
     * 通过机构id和渠道机构id查询采购渠道
     * @param orgId
     * @param purchaseOrgId
     * @return
     */
    FlowPurchaseChannelDTO findByOrgIdAndPurchaseOrgId(Long orgId,Long purchaseOrgId);


    /**
     * 采购渠道机构编码查询所有采购渠道关系
     * @param purchaseOrgId
     * @return
     */
    List<FlowPurchaseChannelDTO> findByPurchaseOrgId(Long purchaseOrgId);

}
