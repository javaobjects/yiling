package com.yiling.cms.evaluate.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.evaluate.dto.HealthEvaluateDTO;
import com.yiling.cms.evaluate.dto.HealthEvaluateUserDTO;
import com.yiling.cms.evaluate.dto.SubmitHealthEvaluateResultDTO;
import com.yiling.cms.evaluate.dto.request.*;

import java.util.List;
import java.util.Map;

/**
 * 健康测评API
 * @author: fan.shen
 * @date: 2022/12/6
 */
public interface HealthEvaluateApi {

    /**
     * 添加健康测评
     * @param addHealthEvaluateRequest
     * @return
     */
     Long addHealthEvaluate(AddHealthEvaluateRequest addHealthEvaluateRequest);

    /**
     * 编辑健康测评
     * @param updateHealthEvaluateRequest
     * @return
     */
    Boolean updateHealthEvaluate(UpdateHealthEvaluateRequest updateHealthEvaluateRequest);

    /**
     * 健康测评分页
     * @param request
     * @return
     */
    Page<HealthEvaluateDTO> listPage(QueryHealthEvaluatePageRequest request);

    /**
     * 单个健康测评
     * @param id
     * @return
     */
    HealthEvaluateDTO getHealthEvaluateById(Long id);

    /**
     * 获取展示业务线
     * @param id
     * @return
     */
    List<Long> getLineIdList(Long id);

    /**
     * 发布测评
     * @param request
     */
    void publishHealthEvaluate(PublishHealthEvaluateRequest request);

    /**
     * 获取测评首页数据
     * @return
     */
    List<HealthEvaluateDTO> getIndexPage();

    /**
     * 获取测评题目
     * @param healthEvaluateIdList
     * @return
     */
    Map<Long, Long> getTotalQuestionByEvaluateIdList(List<Long> healthEvaluateIdList);

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
     */
    Long startEvaluate(UserStartEvaluateRequest request);

    /**
     * 保存测评结果
     * @param request
     */
    SubmitHealthEvaluateResultDTO submitEvaluateResult(SubmitEvaluateResultRequest request);
}
