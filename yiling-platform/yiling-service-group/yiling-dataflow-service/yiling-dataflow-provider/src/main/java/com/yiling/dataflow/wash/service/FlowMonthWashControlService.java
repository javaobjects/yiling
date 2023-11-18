package com.yiling.dataflow.wash.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashControlPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateFlowMonthWashControlRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowMonthWashControlStatusRequest;
import com.yiling.dataflow.wash.dto.request.UpdateStageRequest;
import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
public interface FlowMonthWashControlService extends BaseService<FlowMonthWashControlDO> {

    /**
     * 新增日程表
     * 1、所属年月
     * 2、判断提取时间返回是否有重复开启
     * @param saveOrUpdateFlowMonthWashControlRequest
     * @return
     */
    boolean saveOrUpdate(SaveOrUpdateFlowMonthWashControlRequest saveOrUpdateFlowMonthWashControlRequest);

    /**
     * 分页查询
     * @param request
     * @return
     */
    Page<FlowMonthWashControlDTO> listPage(QueryFlowMonthWashControlPageRequest request);

    /**
     * 通过年月获取日程信息
     * @param year
     * @param month
     * @return
     */
    FlowMonthWashControlDTO getByYearAndMonth(Integer year,Integer month);

    /**
     * 获取是否可以上传流向（产品对照）
     * @return 等于0没有开启 大于0开启
     */
    FlowMonthWashControlDTO getWashStatus();

    /**
     * 获取当前开启的日程
     * @return
     */
    FlowMonthWashControlDTO getCurrentFlowMonthWashControl();

    /**
     *
     * @return
     */
    FlowMonthWashControlDTO getUnlockStatus();

    FlowMonthWashControlDTO getGbLockStatus();

    FlowMonthWashControlDTO getGbUnlockStatus();

    FlowMonthWashControlDTO getBasisStatus();

    /**
     * 验证是否可以新增日程
     * @return
     */
    boolean verifyStatus();
}
