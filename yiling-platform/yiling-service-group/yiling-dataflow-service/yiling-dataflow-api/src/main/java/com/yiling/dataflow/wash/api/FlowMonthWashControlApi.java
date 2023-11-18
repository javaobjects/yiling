package com.yiling.dataflow.wash.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashControlPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateFlowMonthWashControlRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface FlowMonthWashControlApi {

    /**
     * 新增日程表
     * 1、所属年月
     * 2、判断提取时间返回是否有重复开启
     *
     * @param saveOrUpdateFlowMonthWashControlRequest
     * @return
     */
    boolean saveOrUpdate(SaveOrUpdateFlowMonthWashControlRequest saveOrUpdateFlowMonthWashControlRequest);

    /**
     * 通过主键id获取日程信息
     *
     * @param id
     * @return
     */
    FlowMonthWashControlDTO getById(Long id);

    /**
     * 获取日程列表信息
     *
     * @param request
     * @return
     */
    Page<FlowMonthWashControlDTO> listPage(QueryFlowMonthWashControlPageRequest request);

    /**
     * 通过年月获取日程信息
     *
     * @param year
     * @param month
     * @return
     */
    FlowMonthWashControlDTO getByYearAndMonth(Integer year, Integer month);

    /**
     * 获取是否可以第一阶段清洗
     *
     * @return 等于0没有开启 大于0开启
     */
    FlowMonthWashControlDTO getWashStatus();

    /**
     * 获取当前开启的日程
     * @return
     */
    FlowMonthWashControlDTO getCurrentFlowMonthWashControl();

    /**
     * 非锁销量分配是否可以编辑
     * @return FlowMonthWashControlDTO对象 1、对象!=null可以编辑 2、对象==null不能编辑
     */
    FlowMonthWashControlDTO getUnlockStatus();

    /**
     * 团购处理(锁定部分、非客户分类）是否可以编辑
     * @return FlowMonthWashControlDTO对象 1、对象!=null可以编辑 2、对象==null不能编辑
     */
    FlowMonthWashControlDTO getGbLockStatus();

    /**
     * 非锁流向团购处理
     * @return FlowMonthWashControlDTO对象 1、对象!=null可以编辑 2、对象==null不能编辑
     */
    FlowMonthWashControlDTO getGbUnlockStatus();

    FlowMonthWashControlDTO getBasisStatus();

    /**
     * 验证是否还有没有关闭的状态
     * @return
     */
    boolean verifyStatus();
}
