package com.yiling.dataflow.flow.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowPurchaseChannelRequest;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelApi
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
public interface FlowPurchaseChannelApi {

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

    Boolean deleteById(Long id,Long opUserId);

    FlowPurchaseChannelDTO findById(Long id);

    /**
     * 采购渠道机构编码查询所有采购渠道关系
     * @param purchaseOrgId
     * @return
     */
    List<FlowPurchaseChannelDTO> findByPurchaseOrgId(Long purchaseOrgId);
}
