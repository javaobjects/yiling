package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchTransitDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 在途订单库存、终端库存表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2023-03-06
 */
public interface FlowGoodsBatchTransitService extends BaseService<FlowGoodsBatchTransitDO> {

//    /**
//     * 根据所属月份、crm企业主键、标准商品编码列表查询在途订单列表
//     *
//     * @param month 所属月份
//     * @param crmId crm企业主键
//     * @param goodsCodeList 标准商品编码列表
//     * @return
//     */
//    List<FlowGoodsBatchTransitDTO> listByMonthAndCrmIdAndGoodsCodeList(String month, Long crmId, List<Long> goodsCodeList);

    Page<FlowGoodsBatchTransitDO> listPage(QueryFlowGoodsBatchTransitPageRequest request);

    /**
     * 根据经销商id列表、采购渠道机构id列表、商品编码列表查询
     *
     * @param goodsBatchType 库存类型：1-在途订单库存 2-终端库存，FlowGoodsBatchTransitTypeEnum
     * @param crmEnterpriseIdList 经销商id列表
     * @param crmSupplyIdList 采购渠道机构id列表（终端库存传null）
     * @param crmGoodsCodeList 商品编码列表
     * @return
     */
    List<FlowGoodsBatchTransitDO> listByEnterpriseAndSupplyIdAndGoodsCode(Integer goodsBatchType, List<Long> crmEnterpriseIdList, List<Long> crmSupplyIdList, List<Long> crmGoodsCodeList);

    /**
     * 保存
     *
     * @param list
     * @return
     */
    Boolean batchSave(List<SaveFlowGoodsBatchTransitRequest> list);

    /**
     * 编辑在途订单
     *
     * @param list
     * @return
     */
    Boolean batchUpdate(List<UpdateFlowGoodsBatchTransitRequest> list);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    FlowGoodsBatchTransitDO getById(Long id);

    /**
     * 根据id逻辑删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id, Long currentUserId);
}
