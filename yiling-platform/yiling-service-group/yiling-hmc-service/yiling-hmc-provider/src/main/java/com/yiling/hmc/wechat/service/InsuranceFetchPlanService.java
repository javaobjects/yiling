package com.yiling.hmc.wechat.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.request.InsuranceFetchPlanPageRequest;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDO;

/**
 * <p>
 * C端拿药计划表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-31
 */
public interface InsuranceFetchPlanService extends BaseService<InsuranceFetchPlanDO> {

    /**
     * 保存拿药计划
     *
     * @param requestList
     * @return
     */
    Boolean saveFetchPlan(List<SaveInsuranceFetchPlanRequest> requestList);

    /**
     * 获取拿药计划
     *
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceFetchPlanDTO> getByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 获取最近拿药计划
     *
     * @param insuranceRecordId
     * @return
     */
    InsuranceFetchPlanDTO getLatestPlan(Long insuranceRecordId);

    /**
     * 更新拿药状态
     *
     * @param planDTO
     * @return
     */
    boolean updateFetchStatus(InsuranceFetchPlanDTO planDTO);

    /**
     * 计算总拿药次数
     *
     * @param insuranceRecordIdList
     * @return
     */
    Map<Long, Long> getFetchPlanCountMap(List<Long> insuranceRecordIdList, List<Long> recordPayIdList);

    /**
     * 根据交费记录id查拿药计划
     *
     * @param recordPayId
     * @return
     */
    List<InsuranceFetchPlanDTO> getByRecordPayId(Long recordPayId);

    /**
     * C端拿药计划表分页查询
     *
     * @param request 查询条件
     * @return C端拿药计划
     */
    Page<InsuranceFetchPlanDO> pageList(InsuranceFetchPlanPageRequest request);

    /**
     * 根据orderId获取拿药计划
     * @param id
     * @return
     */
    InsuranceFetchPlanDTO getByOrderId(Long id);

}
