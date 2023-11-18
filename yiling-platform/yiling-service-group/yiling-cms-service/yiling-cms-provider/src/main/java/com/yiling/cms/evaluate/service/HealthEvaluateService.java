package com.yiling.cms.evaluate.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.evaluate.dto.HealthEvaluateDTO;
import com.yiling.cms.evaluate.dto.SubmitHealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.*;
import com.yiling.cms.evaluate.entity.HealthEvaluateDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 健康测评表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
public interface HealthEvaluateService extends BaseService<HealthEvaluateDO> {

    /**
     * 保存
     * @param addHealthEvaluateRequest
     * @return
     */
    Long addHealthEvaluate(AddHealthEvaluateRequest addHealthEvaluateRequest);

    /**
     * 更新测评
     * @param updateHealthEvaluateRequest
     * @return
     */
    Boolean updateHealthEvaluate(UpdateHealthEvaluateRequest updateHealthEvaluateRequest);

    /**
     * 列表查询
     * @param request
     * @return
     */
    Page<HealthEvaluateDTO> listPage(QueryHealthEvaluatePageRequest request);

    /**
     * 发布测评
     * @param request
     */
    void publishHealthEvaluate(PublishHealthEvaluateRequest request);

    /**
     * 获取测评
     * @param healthEvaluateIdList
     * @return
     */
    List<HealthEvaluateDTO> getByIdList(List<Long> healthEvaluateIdList);

    /**
     * 获取测评题目
     * @param healthEvaluateIdList
     * @return
     */
    Map<Long, Long> getTotalSubjectByEvaluateIdList(List<Long> healthEvaluateIdList);

    /**
     * 保存测评结果
     * @param request
     */
    SubmitHealthEvaluateResultDTO submitEvaluateResult(SubmitEvaluateResultRequest request);
}
