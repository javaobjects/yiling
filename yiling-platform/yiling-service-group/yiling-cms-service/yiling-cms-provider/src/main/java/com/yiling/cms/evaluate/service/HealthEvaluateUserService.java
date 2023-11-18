package com.yiling.cms.evaluate.service;

import com.yiling.cms.evaluate.dto.HealthEvaluateUserDTO;
import com.yiling.cms.evaluate.dto.request.UserStartEvaluateRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateUserDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 健康测评用户表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateUserService extends BaseService<HealthEvaluateUserDO> {

    /**
     * 获取测评参与人数
     * @param healthEvaluateIdList
     * @return
     */
    Map<Long, Long> getTotalUserByEvaluateIdList(List<Long> healthEvaluateIdList);

    /**
     * 获取测评数据
     * @param healthEvaluateIdList
     * @return
     */
    List<HealthEvaluateUserDTO> getUserByEvaluateIdList(List<Long> healthEvaluateIdList);

    /**
     * 开始测评
     * @param request
     * @return
     */
    Long startEvaluate(UserStartEvaluateRequest request);

    /**
     * 完成测评
     * @param startEvaluateId
     */
    void finishEvaluate(Long startEvaluateId);
}
