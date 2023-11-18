package com.yiling.dataflow.wash.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchTransitDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowGoodsBatchTransitRequest;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
public interface FlowInTransitOrderApi {

    Page<FlowGoodsBatchTransitDTO> listPage(QueryFlowGoodsBatchTransitPageRequest request);

    /**
     * 根据经销商id列表、采购渠道机构id列表、商品编码列表查询
     *
     * @param goodsBatchType 库存类型：1-在途订单库存 2-终端库存，FlowGoodsBatchTransitTypeEnum
     * @param crmEnterpriseIdList 经销商id列表
     * @param crmSupplyIdList 采购渠道机构id列表
     * @param crmGoodsCodeList 商品编码列表
     * @return
     */
    List<FlowGoodsBatchTransitDTO> listByEnterpriseAndSupplyIdAndGoodsCode(Integer goodsBatchType, List<Long> crmEnterpriseIdList, List<Long> crmSupplyIdList, List<Long> crmGoodsCodeList);

    /**
     * 保存在途订单
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
    FlowGoodsBatchTransitDTO getById(Long id);

    /**
     * 根据id逻辑删除
     *
     * @param id
     * @return
     */
    int deleteById(Long id, Long currentUserId);

    /**
     * 根据机构id、标准产品编码map，获取三者关系id
     *
     * @param crmEnterpriseIdGoodsCodeMap key-机构id，value-标准产品编码
     * @param type 0-保存 1-编辑
     * @param gbDetailMonth 所属年月
     * @return 机构id对应的三者关系map：key-机构id，value-三者关系id
     */
    Map<Long, Long> getCrmEnterpriseRelationShipMap(Map<Long, Long> crmEnterpriseIdGoodsCodeMap, int type, String gbDetailMonth);

    /**
     * 校验所属年月
     * 在途订单上报、终端库存上报
     *
     * @param businessName 业务名称（在途订单上报、终端库存上报）
     * @param gbDetailMonth 提交的所属年月
     * @return
     */
    String checkFlowMonthWashControl(String businessName, String gbDetailMonth);

    /**
     * 获取上报日程的在途订单、终端库存的上报时间
     * 1.获取状态开启的日程、状态开启的在途订单/终端库存的上报
     * 2.没有状态开启的，则获取当月的日程
     * 3.没有当月的日程，则获取上个月的日程
     *
     * @return
     */
    FlowMonthWashControlDTO getGoodsBatchTime();

    /**
     * 获取用户数据权限
     *
     * @param currentUserCode 当前用户工号
     * @param method 操作方法描述
     * @return
     */
    Map<String, List<String>> buildUserDatascope(String currentUserCode, String method);
}
