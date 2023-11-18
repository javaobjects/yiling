package com.yiling.dataflow.flowcollect.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowFleeingGoodsDTO;
import com.yiling.dataflow.flowcollect.dto.SaleAppealGoodsFormInfoDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveSaleAppealFlowRelateRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveSalesAppealDateRequest;

/**
 * 确认销量申诉api
 *
 * @author: shixing.sun
 * @date: 2023-03-06
 */
public interface SalesAppealConfirmApi {

    /**
     * 销量申诉上传数据新增
     *
     * @param requests 新增内容
     * @return 成功/失败
     */
    Boolean saveSalesAppealConfirmDate(List<SaveSalesAppealDateRequest> requests);

    /**
     * @param recordId
     * @param taskId
     * @param opUserId
     * @return
             */
    Boolean updateTaskIdByRecordId(Long recordId, Long taskId, Long opUserId);

    /**
     * 窜货申报上传数据分页查询
     *
     * @param request 查询条件
     * @return 窜货申报上传数据
     */
    Page<FlowFleeingGoodsDTO> pageList(QueryFlowMonthPageRequest request);

    /**
     * 保存销量申诉源流向与负流向关联数据
     * @param saveSaleAppealFlowRelateRequest
     * @return
     */
    long saveSaveSaleAppealFlowRelate(SaveSaleAppealFlowRelateRequest saveSaleAppealFlowRelateRequest);

    /**
     * 获取传输方式为：上传Excel, 未进行流向数据清洗任务销量申诉数据
     * @param formId 主流程表单id
     * @return 未进行流向数据清洗任务 传输方式为：上传Excel的销量申诉数据
     */
    List<SaleAppealGoodsFormInfoDTO> getSaleAppealGoodsFormInfoDTOList(Long formId);
}
