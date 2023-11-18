package com.yiling.hmc.wechat.api;

import java.util.List;
import java.util.Map;

import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDetailDTO;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanGroupDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanDetailRequest;

/**
 * 拿药计划明细API
 *
 * @Author fan.shen
 * @Date 2022/3/31
 */
public interface InsuranceFetchPlanDetailApi {

    /**
     * 保存拿药计划详情
     *
     * @param requestList
     * @return
     */
    Boolean saveFetchPlanDetail(List<SaveInsuranceFetchPlanDetailRequest> requestList);

    /**
     * 获取拿药计划详情
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
     * 计算待兑付药品数量
     * @param goodsIdList
     * @return
     */
    Map<Long,Long> queryGoodsCount(List<Long> goodsIdList);
}
