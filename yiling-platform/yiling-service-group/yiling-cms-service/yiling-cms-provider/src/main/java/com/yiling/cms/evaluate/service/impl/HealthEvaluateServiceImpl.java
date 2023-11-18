package com.yiling.cms.evaluate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.yiling.cms.common.CmsErrorCode;
import com.yiling.cms.content.entity.ContentDeptDO;
import com.yiling.cms.content.entity.ContentDiseaseDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateMapper;
import com.yiling.cms.evaluate.dto.*;
import com.yiling.cms.evaluate.dto.request.*;
import com.yiling.cms.evaluate.entity.*;
import com.yiling.cms.evaluate.enums.QuestionTypeEnum;
import com.yiling.cms.evaluate.enums.RangeTypeEnum;
import com.yiling.cms.evaluate.service.*;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 健康测评表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Slf4j
@Service
public class HealthEvaluateServiceImpl extends BaseServiceImpl<HealthEvaluateMapper, HealthEvaluateDO> implements HealthEvaluateService {

    @Autowired
    HealthEvaluateLineService healthEvaluateLineService;

    @Autowired
    HealthEvaluateDeptService deptService;

    @Autowired
    HealthEvaluateDiseaseService diseaseService;

    @Autowired
    HealthEvaluateQuestionService questionService;

    @Autowired
    HealthEvaluateQuestionSelectService selectService;

    @Autowired
    HealthEvaluateQuestionBlankService blankService;

    @Autowired
    HealthEvaluateQuestionBmiService bmiService;

    @Autowired
    HealthEvaluateResultService resultService;

    @Autowired
    HealthEvaluateMarketAdviceService marketAdviceService;

    @Autowired
    HealthEvaluateMarketGoodsService marketGoodsService;

    @Autowired
    HealthEvaluateMarketPromoteService marketPromoteService;

    @Autowired
    HealthEvaluateUserService userService;

    @Autowired
    HealthEvaluateUserScoreService userScoreService;

    @Override
    @Transactional
    public Long addHealthEvaluate(AddHealthEvaluateRequest request) {

        // 保存测评
        HealthEvaluateDO evaluateDO = PojoUtils.map(request, HealthEvaluateDO.class);
        this.save(evaluateDO);

        // 保存业务引用
        if (CollUtil.isNotEmpty(request.getLineIdList())) {
            List<HealthEvaluateLineDO> lineDOList = request.getLineIdList().stream().map(item -> {
                HealthEvaluateLineDO lineDO = new HealthEvaluateLineDO();
                lineDO.setLineId(item);
                lineDO.setHealthEvaluateId(evaluateDO.getId());
                lineDO.setCreateUser(request.getOpUserId());
                lineDO.setUpdateUser(request.getOpUserId());
                return lineDO;
            }).collect(Collectors.toList());
            healthEvaluateLineService.saveBatch(lineDOList);
        }

        //关联疾病
        List<HealthEvaluateDiseaseDO> contentDiseaseDOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(request.getDiseaseIdList())) {
            request.getDiseaseIdList().forEach(diseaseId -> {
                HealthEvaluateDiseaseDO diseaseDO = new HealthEvaluateDiseaseDO();
                diseaseDO.setHealthEvaluateId(evaluateDO.getId()).setCreateUser(request.getOpUserId()).setDiseaseId(diseaseId);
                contentDiseaseDOList.add(diseaseDO);
            });
            diseaseService.saveBatch(contentDiseaseDOList);
        }

        //关联科室
        List<HealthEvaluateDeptDO> contentDeptDOS = Lists.newArrayList();
        if (CollUtil.isNotEmpty(request.getDeptIdList())) {
            request.getDeptIdList().forEach(deptId -> {
                HealthEvaluateDeptDO deptDO = new HealthEvaluateDeptDO();
                deptDO.setHealthEvaluateId(evaluateDO.getId()).setCreateUser(request.getOpUserId()).setDeptId(deptId);
                contentDeptDOS.add(deptDO);
            });
            deptService.saveBatch(contentDeptDOS);
        }

        return evaluateDO.getId();
    }

    @Override
    @Transactional
    public Boolean updateHealthEvaluate(UpdateHealthEvaluateRequest request) {
        HealthEvaluateDO evaluateDO = PojoUtils.map(request, HealthEvaluateDO.class);
        this.updateById(evaluateDO);

        HealthEvaluateLineDO entity = new HealthEvaluateLineDO();
        entity.setUpdateUser(request.getOpUserId());

        QueryWrapper<HealthEvaluateLineDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateLineDO::getHealthEvaluateId, request.getId());
        healthEvaluateLineService.batchDeleteWithFill(entity, wrapper);

        // 保存业务引用
        if (CollUtil.isNotEmpty(request.getLineIdList())) {
            List<HealthEvaluateLineDO> lineDOList = request.getLineIdList().stream().map(item -> {
                HealthEvaluateLineDO lineDO = new HealthEvaluateLineDO();
                lineDO.setLineId(item);
                lineDO.setHealthEvaluateId(evaluateDO.getId());
                lineDO.setCreateUser(request.getOpUserId());
                lineDO.setUpdateUser(request.getOpUserId());
                return lineDO;
            }).collect(Collectors.toList());

            healthEvaluateLineService.saveBatch(lineDOList);
        }

        // 设置关联科室
        HealthEvaluateDeptDO deptDO = new HealthEvaluateDeptDO();
        deptDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<HealthEvaluateDeptDO> deptDOWrapper = new QueryWrapper<>();
        deptDOWrapper.lambda().eq(HealthEvaluateDeptDO::getHealthEvaluateId, request.getId());
        deptService.batchDeleteWithFill(deptDO, deptDOWrapper);

        if (CollUtil.isNotEmpty(request.getDeptIdList())) {
            List<HealthEvaluateDeptDO> contentDeptDOS = Lists.newArrayList();
            request.getDeptIdList().forEach(deptId -> {
                HealthEvaluateDeptDO dept = new HealthEvaluateDeptDO();
                dept.setHealthEvaluateId(evaluateDO.getId()).setCreateUser(request.getOpUserId()).setDeptId(deptId);
                contentDeptDOS.add(dept);
            });
            deptService.saveBatch(contentDeptDOS);
        }


        //关联疾病
        HealthEvaluateDiseaseDO diseaseDO = new HealthEvaluateDiseaseDO();
        diseaseDO.setUpdateUser(request.getOpUserId());

        QueryWrapper<HealthEvaluateDiseaseDO> diseaseDoWrapper = new QueryWrapper<>();
        diseaseDoWrapper.lambda().eq(HealthEvaluateDiseaseDO::getHealthEvaluateId, request.getId());
        diseaseService.batchDeleteWithFill(diseaseDO, diseaseDoWrapper);

        List<HealthEvaluateDiseaseDO> contentDiseaseDOList = Lists.newArrayList();
        if (CollUtil.isNotEmpty(request.getDiseaseIdList())) {
            request.getDiseaseIdList().forEach(diseaseId -> {
                HealthEvaluateDiseaseDO disease = new HealthEvaluateDiseaseDO();
                disease.setHealthEvaluateId(evaluateDO.getId()).setCreateUser(request.getOpUserId()).setDiseaseId(diseaseId);
                contentDiseaseDOList.add(disease);
            });
            diseaseService.saveBatch(contentDiseaseDOList);
        }

        return Boolean.TRUE;
    }

    @Override
    public Page<HealthEvaluateDTO> listPage(QueryHealthEvaluatePageRequest request) {
        LambdaQueryWrapper<HealthEvaluateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(request.getHealthEvaluateName()), HealthEvaluateDO::getHealthEvaluateName, request.getHealthEvaluateName());
        wrapper.eq(Objects.nonNull(request.getHealthEvaluateType()), HealthEvaluateDO::getHealthEvaluateType, request.getHealthEvaluateType());
        wrapper.eq(Objects.nonNull(request.getPublishFlag()), HealthEvaluateDO::getPublishFlag, request.getPublishFlag());
        wrapper.ge(Objects.nonNull(request.getCreateTimeStart()), HealthEvaluateDO::getCreateTime, request.getCreateTimeStart());
        wrapper.le(Objects.nonNull(request.getCreateTimeEnd()), HealthEvaluateDO::getCreateTime, request.getCreateTimeEnd());
        wrapper.ge(Objects.nonNull(request.getUpdateTimeStart()), HealthEvaluateDO::getUpdateTime, request.getUpdateTimeStart());
        wrapper.le(Objects.nonNull(request.getUpdateTimeEnd()), HealthEvaluateDO::getUpdateTime, request.getUpdateTimeEnd());
        wrapper.orderByDesc(HealthEvaluateDO::getCreateTime);
        Page<HealthEvaluateDO> healthEvaluateDOPage = this.page(request.getPage(), wrapper);
        if (healthEvaluateDOPage.getTotal() == 0) {
            return new Page<>();
        }
        Page<HealthEvaluateDTO> evaluateDTOPage = new Page<>(healthEvaluateDOPage.getCurrent(), healthEvaluateDOPage.getSize());
        List<HealthEvaluateDTO> evaluateDTOList = PojoUtils.map(healthEvaluateDOPage.getRecords(), HealthEvaluateDTO.class);
        evaluateDTOPage.setRecords(evaluateDTOList);
        evaluateDTOPage.setTotal(healthEvaluateDOPage.getTotal());

        List<Long> evaluateIdList = evaluateDTOList.stream().map(HealthEvaluateDTO::getId).collect(Collectors.toList());
        List<HealthEvaluateLineDTO> healthEvaluateLineDTOS = healthEvaluateLineService.getByHealthEvaluateIdList(evaluateIdList);
        evaluateDTOList.forEach(item -> item.setLineIdList(healthEvaluateLineDTOS.stream().filter(line -> line.getHealthEvaluateId().equals(item.getId())).map(HealthEvaluateLineDTO::getLineId).collect(Collectors.toList())));
        return evaluateDTOPage;
    }

    @Override
    public void publishHealthEvaluate(PublishHealthEvaluateRequest request) {
        LambdaQueryWrapper<HealthEvaluateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HealthEvaluateDO::getId, request.getId());

        HealthEvaluateDO evaluateDO = new HealthEvaluateDO();
        evaluateDO.setPublishFlag(request.getPublishFlag());
        evaluateDO.setUpdateUser(request.getOpUserId());
        this.update(evaluateDO, wrapper);
    }

    @Override
    public List<HealthEvaluateDTO> getByIdList(List<Long> healthEvaluateIdList) {
        LambdaQueryWrapper<HealthEvaluateDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(HealthEvaluateDO::getId, healthEvaluateIdList);
        wrapper.eq(HealthEvaluateDO::getPublishFlag, 1);
        List<HealthEvaluateDO> evaluateDOList = this.list(wrapper);
        return PojoUtils.map(evaluateDOList, HealthEvaluateDTO.class);
    }

    @Override
    public Map<Long, Long> getTotalSubjectByEvaluateIdList(List<Long> healthEvaluateIdList) {
        List<HealthEvaluateQuestionDTO> questionList = questionService.getHealthEvaluateQuestionByEvaluateIdList(healthEvaluateIdList);
        if (CollUtil.isEmpty(questionList)) {
            return Maps.newHashMap();
        }
        return questionList.stream().collect(Collectors.groupingBy(HealthEvaluateQuestionDTO::getHealthEvaluateId, Collectors.counting()));
    }

    @Override
    public SubmitHealthEvaluateResultDTO submitEvaluateResult(SubmitEvaluateResultRequest request) {

        // 声明返回对象
        SubmitHealthEvaluateResultDTO submitHealthEvaluateResultDTO = new SubmitHealthEvaluateResultDTO();

        // 健康测评
        HealthEvaluateDO healthEvaluateDO = this.getById(request.getHealthEvaluateId());

        // 测评题目
        List<HealthEvaluateQuestionDTO> questionList = questionService.getFullQuestionsByHealthEvaluateId(request.getHealthEvaluateId());
        Map<Long, EvaluateResultDetailRequest> detailMap = request.getEvaluateResultDetailList().stream().collect(Collectors.toMap(EvaluateResultDetailRequest::getHealthEvaluateQuestionId, Function.identity()));

        // 取出第一题(如果涉及到性别，取出第一题作为参照)
        List<Long> selectIdList = request.getEvaluateResultDetailList().get(0).getSelectIdList();
        Long sexId = null;
        if (CollUtil.isNotEmpty(selectIdList)) {
            sexId = selectIdList.get(0);
        }

        BigDecimal totalScore = BigDecimal.ZERO;
        for (HealthEvaluateQuestionDTO item : questionList) {
            // 是否计分：0-否 1-是
            if (item.getIfScore() == 0) {
                continue;
            }
            // 是否必填
            if (item.getIfBlank() == 1 && !detailMap.containsKey(item.getId())) {
                throw new BusinessException(CmsErrorCode.HEALTH_EVALUATE_RESULT_ERROR);
            }
            EvaluateResultDetailRequest evaluateResultDetailRequest = detailMap.get(item.getId());
            if (Objects.isNull(evaluateResultDetailRequest)) {
                log.error("根据questionId未匹配到答案，questionId:{}", item.getId());
                continue;
            }
            // 1、单选题
            List<Long> userSelectIdList = evaluateResultDetailRequest.getSelectIdList();
            List<HealthEvaluateQuestionSelectDTO> sourceSelectList = item.getSelectList();
            List<HealthEvaluateQuestionBmiDTO> sourceBmiList = item.getBmiList();

            // 保存测评结果、分数
            HealthEvaluateUserScoreDO userScore = new HealthEvaluateUserScoreDO();
            userScore.setHealthEvaluateQuestionId(item.getId());
            userScore.setHealthEvaluateUserId(request.getStartEvaluateId());
            userScore.setCreateUser(request.getOpUserId());
            userScore.setUpdateUser(request.getOpUserId());
            userScore.setCreateTime(new Date());
            userScore.setUpdateTime(new Date());

            if (QuestionTypeEnum.SELECT.getCode().equals(item.getQuestionType())) {
                // 1、如果是非必填
                if (item.getIfBlank() == 0 && CollUtil.isEmpty(userSelectIdList)) {
                    log.error("当前题目非必填，跳过处理,题目设置结果集：{}, evaluateResultDetailRequest:{}", sourceSelectList, evaluateResultDetailRequest);
                    continue;
                }
                Optional<HealthEvaluateQuestionSelectDTO> result = sourceSelectList.stream().filter(source -> source.getId().equals(userSelectIdList.get(0))).findFirst();
                if (!result.isPresent()) {
                    log.error("根据选中结果未匹配到选项，跳过处理,题目设置结果集：{}, evaluateResultDetailRequest:{}", sourceSelectList, evaluateResultDetailRequest);
                    continue;
                }
                // 记分是否区分性别 0-否，1-是
                Integer ifScoreSex = result.get().getIfScoreSex();
                BigDecimal score = BigDecimal.ZERO;
                if (ifScoreSex == 0) {
                    score = result.get().getScore();
                } else {
                    // 1-男
                    if (Objects.nonNull(sexId) && sexId == 1) {
                        score = result.get().getScoreMen();

                    }
                    // 女
                    else if (Objects.nonNull(sexId) && sexId == 2) {
                        score = result.get().getScoreWomen();
                    }
                }
                totalScore = totalScore.add(score);

                userScore.setAnswer(String.valueOf(userSelectIdList.get(0)));
                userScore.setScore(score);

            }

            // 2、多选题
            if (QuestionTypeEnum.MULTI_SELECT.getCode().equals(item.getQuestionType())) {
                BigDecimal currentScore = BigDecimal.ZERO;
                for (HealthEvaluateQuestionSelectDTO source : sourceSelectList) {
                    if (userSelectIdList.stream().anyMatch(userSelectId -> userSelectId.equals(source.getId()))) {
                        // 记分是否区分性别 0-否，1-是
                        Integer ifScoreSex = source.getIfScoreSex();
                        if (ifScoreSex == 0) {
                            currentScore = currentScore.add(source.getScore());
                        } else {
                            // 1-男
                            if (Objects.nonNull(sexId) && sexId == 1) {
                                currentScore = currentScore.add(source.getScoreMen());
                            }
                            // 女
                            else if (Objects.nonNull(sexId) && sexId == 2) {
                                currentScore = currentScore.add(source.getScoreWomen());
                            }
                        }
                    }
                }

                totalScore = totalScore.add(currentScore);

                // 保存测评结果、分数
                userScore.setScore(currentScore);
                userScore.setAnswer(JSONUtil.toJsonStr(userSelectIdList));

            }

            // 3、BMI题
            if (QuestionTypeEnum.BMI.getCode().equals(item.getQuestionType())) {

                BigDecimal height2 = evaluateResultDetailRequest.getHeight().multiply(evaluateResultDetailRequest.getHeight()).divide(BigDecimal.valueOf(10000));
                BigDecimal bmiResult = evaluateResultDetailRequest.getWeight().divide(height2, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);

                BigDecimal score = BigDecimal.ZERO;
                for (HealthEvaluateQuestionBmiDTO sourceBmi : sourceBmiList) {
                    Range<BigDecimal> range = null;
                    if (sourceBmi.getRangeStartType() != 0 && sourceBmi.getRangeEndType() == 0) {
                        if (RangeTypeEnum.GT.getCode().equals(sourceBmi.getRangeStartType())) {
                            range = Range.greaterThan(sourceBmi.getRangeStart());
                        }
                        if (RangeTypeEnum.GE.getCode().equals(sourceBmi.getRangeStartType())) {
                            range = Range.atLeast(sourceBmi.getRangeStart());
                        }
                        if (RangeTypeEnum.LT.getCode().equals(sourceBmi.getRangeStartType())) {
                            range = Range.lessThan(sourceBmi.getRangeStart());
                        }
                        if (RangeTypeEnum.LE.getCode().equals(sourceBmi.getRangeStartType())) {
                            range = Range.atMost(sourceBmi.getRangeStart());
                        }
                        if (RangeTypeEnum.EQ.getCode().equals(sourceBmi.getRangeStartType())) {
                            range = Range.closed(sourceBmi.getRangeStart(), sourceBmi.getRangeStart());
                        }
                    } else {
                        if (RangeTypeEnum.GT.getCode().equals(sourceBmi.getRangeStartType()) && RangeTypeEnum.LT.getCode().equals(sourceBmi.getRangeEndType())) {
                            range = Range.open(sourceBmi.getRangeStart(), sourceBmi.getRangeEnd());
                        } else if (RangeTypeEnum.GE.getCode().equals(sourceBmi.getRangeStartType()) && RangeTypeEnum.LT.getCode().equals(sourceBmi.getRangeEndType())) {
                            range = Range.closedOpen(sourceBmi.getRangeStart(), sourceBmi.getRangeEnd());
                        } else if (RangeTypeEnum.GT.getCode().equals(sourceBmi.getRangeStartType()) && RangeTypeEnum.LE.getCode().equals(sourceBmi.getRangeEndType())) {
                            range = Range.openClosed(sourceBmi.getRangeStart(), sourceBmi.getRangeEnd());
                        } else if (RangeTypeEnum.GE.getCode().equals(sourceBmi.getRangeStartType()) && RangeTypeEnum.LE.getCode().equals(sourceBmi.getRangeEndType())) {
                            range = Range.closed(sourceBmi.getRangeStart(), sourceBmi.getRangeEnd());
                        } else if (RangeTypeEnum.EQ.getCode().equals(sourceBmi.getRangeStartType()) && RangeTypeEnum.EQ.getCode().equals(sourceBmi.getRangeEndType())) {
                            range = Range.closed(sourceBmi.getRangeStart(), sourceBmi.getRangeEnd());
                        }

                    }
                    if (Objects.nonNull(range) && range.contains(bmiResult)) {
                        log.info("根据后台配置生成区间，sourceBmi:{}, 区间:{}", JSONUtil.toJsonStr(sourceBmi), range);
                        score = sourceBmi.getScore();
                        break;
                    }
                }
                totalScore = totalScore.add(score);


                // 保存测评结果、分数
                userScore.setScore(score);
                userScore.setAnswer(evaluateResultDetailRequest.getHeight() + "," + evaluateResultDetailRequest.getWeight());
            }
            userScoreService.save(userScore);

        }

        // 获取测评配置结果信息
        List<HealthEvaluateResultDTO> resultDTOList = resultService.getResultListByEvaluateId(request.getHealthEvaluateId());

        // 匹配测评结果坐落区间
        if (CollUtil.isNotEmpty(resultDTOList)) {
            for (HealthEvaluateResultDTO resultDTO : resultDTOList) {
                Range<BigDecimal> range = null;
                if (resultDTO.getScoreStartType() != 0 && resultDTO.getScoreEndType() == 0) {
                    if (RangeTypeEnum.GT.getCode().equals(resultDTO.getScoreStartType())) {
                        range = Range.greaterThan(resultDTO.getScoreStart());
                    }
                    if (RangeTypeEnum.GE.getCode().equals(resultDTO.getScoreStartType())) {
                        range = Range.atLeast(resultDTO.getScoreStart());
                    }
                    if (RangeTypeEnum.LT.getCode().equals(resultDTO.getScoreStartType())) {
                        range = Range.lessThan(resultDTO.getScoreStart());
                    }
                    if (RangeTypeEnum.LE.getCode().equals(resultDTO.getScoreStartType())) {
                        range = Range.atMost(resultDTO.getScoreStart());
                    }
                    if (RangeTypeEnum.EQ.getCode().equals(resultDTO.getScoreStartType())) {
                        range = Range.closed(resultDTO.getScoreStart(), resultDTO.getScoreStart());
                    }
                } else {
                    if (RangeTypeEnum.GT.getCode().equals(resultDTO.getScoreStartType()) && RangeTypeEnum.LT.getCode().equals(resultDTO.getScoreEndType())) {
                        range = Range.open(resultDTO.getScoreStart(), resultDTO.getScoreEnd());
                    } else if (RangeTypeEnum.GE.getCode().equals(resultDTO.getScoreStartType()) && RangeTypeEnum.LT.getCode().equals(resultDTO.getScoreEndType())) {
                        range = Range.closedOpen(resultDTO.getScoreStart(), resultDTO.getScoreEnd());
                    } else if (RangeTypeEnum.GT.getCode().equals(resultDTO.getScoreStartType()) && RangeTypeEnum.LE.getCode().equals(resultDTO.getScoreEndType())) {
                        range = Range.openClosed(resultDTO.getScoreStart(), resultDTO.getScoreEnd());
                    } else if (RangeTypeEnum.GE.getCode().equals(resultDTO.getScoreStartType()) && RangeTypeEnum.LE.getCode().equals(resultDTO.getScoreEndType())) {
                        range = Range.closed(resultDTO.getScoreStart(), resultDTO.getScoreEnd());
                    } else if (RangeTypeEnum.EQ.getCode().equals(resultDTO.getScoreStartType()) && RangeTypeEnum.EQ.getCode().equals(resultDTO.getScoreEndType())) {
                        range = Range.closed(resultDTO.getScoreStart(), resultDTO.getScoreEnd());
                    }
                }
                if (Objects.nonNull(range) && range.contains(totalScore)) {
                    log.info("根据总分匹配到结果，resultDTO:{}, 总分:{}", JSONUtil.toJsonStr(resultDTO), totalScore);
                    submitHealthEvaluateResultDTO.setEvaluateResult(resultDTO.getEvaluateResult());
                    submitHealthEvaluateResultDTO.setResultDesc(resultDTO.getResultDesc());
                    submitHealthEvaluateResultDTO.setHealthTip(resultDTO.getHealthTip());
                    break;
                }
            }
        }

        // 获取改善建议
        List<HealthEvaluateMarketAdviceDTO> marketAdviceList = marketAdviceService.getMarketAdviceByEvaluateId(request.getHealthEvaluateId());

        // 获取关联药品
        List<HealthEvaluateMarketGoodsDTO> marketGoodsList = marketGoodsService.getMarketGoodsByEvaluateId(request.getHealthEvaluateId());

        submitHealthEvaluateResultDTO.setMarketGoodsList(marketGoodsList);
        submitHealthEvaluateResultDTO.setMarketAdviceList(marketAdviceList);

        // 更新测评结果为已完成
        userService.finishEvaluate(request.getStartEvaluateId());

        return submitHealthEvaluateResultDTO;

    }
}
