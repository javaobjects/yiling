package com.yiling.hmc.wechat.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanGroupDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanDetailRequest;
import com.yiling.hmc.wechat.entity.InsuranceFetchPlanDetailDO;

/**
 * <p>
 * C端拿药计划明细表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-31
 */
public interface InsuranceFetchPlanDetailService extends BaseService<InsuranceFetchPlanDetailDO> {

    /**
     * 保存拿药计划明细
     * @param requestList
     * @return
     */
    Boolean saveFetchPlanDetail(List<SaveInsuranceFetchPlanDetailRequest> requestList);

    /**
     * 获取拿药计划明细
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceFetchPlanDetailDTO> getByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 获取每个拿药计划下商品数量
     * @param insuranceRecordIdList
     * @return
     */
    List<InsuranceFetchPlanGroupDTO> groupByInsuranceRecordId(List<Long> insuranceRecordIdList);

    /**
     * 计算拿药数量
     * @param insuranceRecordIdList
     * @return
     */
    Map<Long, List<Long>> getPerMonthCountMap(List<Long> insuranceRecordIdList, List<Long> recordPayIdList);

    /**
     * 计算待兑付药品数量
     * @param goodsIdList
     * @return
     */
    Map<Long,Long> queryGoodsCount(List<Long> goodsIdList);

}
