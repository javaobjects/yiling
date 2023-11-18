package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowMonthWashTaskRequest;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
public interface FlowMonthWashTaskService extends BaseService<FlowMonthWashTaskDO> {

    Page<FlowMonthWashTaskDO> listPage(QueryFlowMonthWashTaskPageRequest request);

    int updateWashStatusById(Long id, Integer washStatus);

    int updateReportConsumeStatusById(Long id, Integer reportConsumeStatus);

    void deleteTaskAndFlowDataById(Long id);

    /**
     * 创建清洗任务
     * @param request
     * @param isSendMq true=发送mq false=不发送mq自己单独发送
     * @return
     */
    long create(SaveFlowMonthWashTaskRequest request,boolean isSendMq);

    /**
     * 通过日程ID查询已经生成的机构id
     * @param fmwcId 日程ID
     * @param flowClassify 分类ID
     * @return
     */
    List<Long> getCrmEnterIdsByFmwcIdAndClassify(long fmwcId, int flowClassify);

    void confirm(Long id);

    List<FlowMonthWashTaskDO> getByControlId(Long fmwcId);

    /**
     * 重新计算未匹配商品数量
     * @param id
     */
    void reUnGoodsMappingCount(Long id);

    /**
     * 重新计算未匹配客户数量
     * @param id
     */
    void reUnCustomerMappingCount(Long id);

    /**
     * 重新计算未匹配供应商数量
     * @param id
     */
    void reUnSupplierMappingCount(Long id);

    /**
     * 修改商品映射配置重新刷新月流向数据
     * @param flowEnterpriseGoodsMappingDTOList
     */
    void updateMonthFlowByGoodsMapping(List<FlowEnterpriseGoodsMappingDTO> flowEnterpriseGoodsMappingDTOList);

    /**
     * 修改商品映射配置重新刷新日流向数据
     * @param flowEnterpriseGoodsMappingDTOList
     */
    void updateDayFlowByGoodsMapping(List<FlowEnterpriseGoodsMappingDTO> flowEnterpriseGoodsMappingDTOList);

    /**
     * 修改客户映射配置重新统计任务
     * @param flowEnterpriseCustomerMappingDTOList
     */
    void updateMonthFlowByCustomerMapping(List<FlowEnterpriseCustomerMappingDTO> flowEnterpriseCustomerMappingDTOList);

    /**
     * 修改客户映射配置重新统计任务
     * @param flowEnterpriseCustomerMappingDTOList
     */
    void updateDayFlowByCustomerMapping(List<FlowEnterpriseCustomerMappingDTO> flowEnterpriseCustomerMappingDTOList);

    /**
     * 修改供应商映射配置重新统计任务
     * @param flowEnterpriseSupplierMappingDTOList
     */
    void updateMonthFlowBySupplierMapping(List<FlowEnterpriseSupplierMappingDTO> flowEnterpriseSupplierMappingDTOList);

    /**
     * 修改供应商映射配置重新统计任务
     * @param flowEnterpriseSupplierMappingDTOList
     */
    void updateDayFlowBySupplierMapping(List<FlowEnterpriseSupplierMappingDTO> flowEnterpriseSupplierMappingDTOList);
}
