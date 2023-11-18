package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmGoodsInfoRequest;
import com.yiling.dataflow.wash.dto.request.UpdateCrmOrganizationInfoRequest;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/3/2
 */
public interface FlowSaleWashService extends BaseService<FlowSaleWashDO> {

    /**
     * 分页查询销售流向清洗明细
     * @param request
     * @return
     */
    Page<FlowSaleWashDO> listPage(QueryFlowSaleWashPageRequest request);

    /**
     * 批量插入
     * @param flowSaleWashDOList
     */
    void batchInsert(List<FlowSaleWashDO> flowSaleWashDOList);

    /**
     * 根据任务清洗id获取List
     * @param fmwtId
     * @return
     */
    List<FlowSaleWashDO> getByFmwtId(Long fmwtId);

    /**
     * 根据所属年月等条件 获取List
     * @param request
     * @return
     */
    List<FlowSaleWashDTO> getByYearMonth(QueryFlowSaleWashListRequest request);

    Page<FlowSaleWashDTO> getPageByYearMonth(QueryFlowSaleWashPageRequest request);

    /**
     * 修改商品对照信息，以及对照状态
     * @param request
     */
    void updateCrmGoodsInfo(UpdateCrmGoodsInfoRequest request);

    /**
     * 修改客户对照信息，以及对照状态
     * @param request
     */
    void updateCrmOrganizationInfo(UpdateCrmOrganizationInfoRequest request);

    Integer goodsUnMappingStatusCount(Long fmwtId);

    Integer organizationUnMappingStatusCount(Long fmwtId);

    void setFlowSaleWashOrgUnlockFlag(FlowSaleWashDO flowSaleWashDO, Long enterpriseCersId);
}
