package com.yiling.cms.evaluate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.yiling.cms.evaluate.dto.*;
import com.yiling.cms.evaluate.dto.request.AddHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.dto.request.EditHealthEvaluateQuestionRequest;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateQuestionDetailRequest;
import com.yiling.cms.evaluate.entity.HealthEvaluateQuestionDO;
import com.yiling.cms.evaluate.dao.HealthEvaluateQuestionMapper;
import com.yiling.cms.evaluate.enums.QuestionTypeEnum;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionBlankService;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionBmiService;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionSelectService;
import com.yiling.cms.evaluate.service.HealthEvaluateQuestionService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 健康测评题目表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-12-06
 */
@Service
public class HealthEvaluateQuestionServiceImpl extends BaseServiceImpl<HealthEvaluateQuestionMapper, HealthEvaluateQuestionDO> implements HealthEvaluateQuestionService {

    @Autowired
    HealthEvaluateQuestionSelectService selectService;

    @Autowired
    HealthEvaluateQuestionBlankService blankService;

    @Autowired
    HealthEvaluateQuestionBmiService bmiService;

    @Override
    public List<HealthEvaluateQuestionDTO> getHealthEvaluateQuestionByEvaluateId(Long evaluateId) {
        QueryWrapper<HealthEvaluateQuestionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateQuestionDO::getHealthEvaluateId, evaluateId);
        List<HealthEvaluateQuestionDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateQuestionDTO.class);
    }

    @Override
    public List<HealthEvaluateQuestionDTO> getHealthEvaluateQuestionByEvaluateIdList(List<Long> evaluateIdList) {
        QueryWrapper<HealthEvaluateQuestionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(HealthEvaluateQuestionDO::getHealthEvaluateId, evaluateIdList);
        List<HealthEvaluateQuestionDO> list = this.list(wrapper);
        return PojoUtils.map(list, HealthEvaluateQuestionDTO.class);
    }

    @Override
    public List<HealthEvaluateQuestionDTO> getFullQuestionsByHealthEvaluateId(Long id) {

        QueryWrapper<HealthEvaluateQuestionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateQuestionDO::getHealthEvaluateId, id);
        List<HealthEvaluateQuestionDO> questionDOList = this.list(wrapper);
        List<Long> selectIdList = questionDOList.stream().filter(item -> QuestionTypeEnum.isSelect(item.getQuestionType())).map(HealthEvaluateQuestionDO::getId).collect(Collectors.toList());
        List<Long> blankIdList = questionDOList.stream().filter(item -> QuestionTypeEnum.BLANK.getCode().equals(item.getQuestionType())).map(HealthEvaluateQuestionDO::getId).collect(Collectors.toList());
        List<Long> bmiIdList = questionDOList.stream().filter(item -> QuestionTypeEnum.BMI.getCode().equals(item.getQuestionType())).map(HealthEvaluateQuestionDO::getId).collect(Collectors.toList());

        List<HealthEvaluateQuestionDTO> questionDTOList = PojoUtils.map(questionDOList, HealthEvaluateQuestionDTO.class);

        if (CollUtil.isNotEmpty(selectIdList)) {
            List<HealthEvaluateQuestionSelectDTO> selectDTOList = selectService.getByQuestionIdList(selectIdList);
            Map<Long, List<HealthEvaluateQuestionSelectDTO>> selectDTOMap = selectDTOList.stream().collect(Collectors.groupingBy(HealthEvaluateQuestionSelectDTO::getHealthEvaluateQuestionId));
            questionDTOList.forEach(item -> {
                if (selectDTOMap.containsKey(item.getId())) {
                    item.setSelectList(selectDTOMap.get(item.getId()));
                }
            });
        }
        if (CollUtil.isNotEmpty(blankIdList)) {
            List<HealthEvaluateQuestionBlankDTO> blankDTOList = blankService.getByQuestionIdList(blankIdList);
            Map<Long, HealthEvaluateQuestionBlankDTO> blankDTOMap = blankDTOList.stream().collect(Collectors.toMap(HealthEvaluateQuestionBlankDTO::getHealthEvaluateQuestionId, o -> o, (k1, k2) -> k1));
            questionDTOList.forEach(item -> {
                if (blankDTOMap.containsKey(item.getId())) {
                    item.setBlank(blankDTOMap.get(item.getId()));
                }
            });
        }
        if (CollUtil.isNotEmpty(bmiIdList)) {
            List<HealthEvaluateQuestionBmiDTO> bmiDTOList = bmiService.getByQuestionIdList(bmiIdList);
            Map<Long, List<HealthEvaluateQuestionBmiDTO>> bmiDTOMap = bmiDTOList.stream().collect(Collectors.groupingBy(HealthEvaluateQuestionBmiDTO::getHealthEvaluateQuestionId));
            questionDTOList.forEach(item -> {
                if (bmiDTOMap.containsKey(item.getId())) {
                    item.setBmiList(bmiDTOMap.get(item.getId()));
                }
            });
        }

        return questionDTOList;
    }

    @Override
    public HealthEvaluateQuestionDTO getQuestionsByQuestionId(Long id) {


        QueryWrapper<HealthEvaluateQuestionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(HealthEvaluateQuestionDO::getId, id);
        HealthEvaluateQuestionDO questionDO = this.getOne(wrapper);
        HealthEvaluateQuestionDTO questionDTO = PojoUtils.map(questionDO, HealthEvaluateQuestionDTO.class);

        if(QuestionTypeEnum.BMI.getCode().equals(questionDO.getQuestionType())) {
            List<HealthEvaluateQuestionBmiDTO> bmiDTOList = bmiService.getByQuestionIdList(Collections.singletonList(id));
            questionDTO.setBmiList(bmiDTOList);
        }

        if(QuestionTypeEnum.BLANK.getCode().equals(questionDO.getQuestionType())) {
            List<HealthEvaluateQuestionBlankDTO> blankDTOList = blankService.getByQuestionIdList(Collections.singletonList(id));
            questionDTO.setBlank(blankDTOList.get(0));
        }

        if(QuestionTypeEnum.isSelect(questionDO.getQuestionType())) {
            List<HealthEvaluateQuestionSelectDTO> selectDTOList = selectService.getByQuestionIdList(Collections.singletonList(id));
            questionDTO.setSelectList(selectDTOList);
        }

        return questionDTO;
    }

    @Override
    @Transactional
    public Boolean addHealthEvaluateQuestion(AddHealthEvaluateQuestionRequest request) {

        Map<Integer, Function<QuestionContextDTO, Boolean>> questionHandlerMap = Maps.newHashMap();
        questionHandlerMap.put(QuestionTypeEnum.SELECT.getCode(), this::selectQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.BLANK.getCode(), this::blankQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.MULTI_SELECT.getCode(), this::selectQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.BMI.getCode(), this::bmiQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.SEX_SELECT.getCode(), this::sexSelectQuestionHandler);

        request.getQuestionList().forEach(question -> {

            // 1、保存题目
            HealthEvaluateQuestionDO questionDO = PojoUtils.map(question, HealthEvaluateQuestionDO.class);
            questionDO.setHealthEvaluateId(request.getHealthEvaluateId());
            this.save(questionDO);

            // 2、保存详情
            QuestionContextDTO contextDTO = new QuestionContextDTO();
            contextDTO.setHealthEvaluateId(request.getHealthEvaluateId());
            contextDTO.setHealthEvaluateQuestionId(questionDO.getId());
            contextDTO.setQuestionDetailRequest(question);
            contextDTO.setOpUserId(request.getOpUserId());
            questionHandlerMap.get(question.getQuestionType()).apply(contextDTO);

        });
        return Boolean.TRUE;
    }

    /**
     * 选择题
     *
     * @param contextDTO
     * @return
     */
    public Boolean selectQuestionHandler(QuestionContextDTO contextDTO) {
        return selectService.add(contextDTO);
    }

    /**
     * 性别选择题
     *
     * @param contextDTO
     * @return
     */
    public Boolean sexSelectQuestionHandler(QuestionContextDTO contextDTO) {
        return Boolean.TRUE;
    }

    /**
     * BMI题
     *
     * @param contextDTO
     * @return
     */
    public Boolean bmiQuestionHandler(QuestionContextDTO contextDTO) {
        return bmiService.add(contextDTO);
    }

    /**
     * 填空题
     *
     * @param contextDTO
     * @return
     */
    public Boolean blankQuestionHandler(QuestionContextDTO contextDTO) {
        return blankService.add(contextDTO);
    }

    @Override
    @Transactional
    public Boolean delQuestionsById(Long id) {
        HealthEvaluateQuestionDO questionDO = this.getById(id);
        if (QuestionTypeEnum.isSelect(questionDO.getQuestionType())) {
            selectService.delByQuestionId(id);
        }
        if (QuestionTypeEnum.BLANK.getCode().equals(questionDO.getQuestionType())) {
            blankService.delByQuestionId(id);
        }
        if (QuestionTypeEnum.BMI.getCode().equals(questionDO.getQuestionType())) {
            bmiService.delByQuestionId(id);
        }
        this.deleteByIdWithFill(questionDO);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean editQuestions(EditHealthEvaluateQuestionRequest request) {
        HealthEvaluateQuestionDetailRequest question = request.getQuestion();
        HealthEvaluateQuestionDO questionDO = PojoUtils.map(question, HealthEvaluateQuestionDO.class);
        questionDO.setHealthEvaluateId(request.getHealthEvaluateId());
        this.updateById(questionDO);

        if (QuestionTypeEnum.isSelect(questionDO.getQuestionType())) {
            selectService.delByQuestionId(request.getQuestion().getId());
        }
        if (QuestionTypeEnum.BLANK.getCode().equals(questionDO.getQuestionType())) {
            blankService.delByQuestionId(request.getQuestion().getId());
        }
        if (QuestionTypeEnum.BMI.getCode().equals(questionDO.getQuestionType())) {
            bmiService.delByQuestionId(request.getQuestion().getId());
        }

        Map<Integer, Function<QuestionContextDTO, Boolean>> questionHandlerMap = Maps.newHashMap();
        questionHandlerMap.put(QuestionTypeEnum.SELECT.getCode(), this::selectQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.BLANK.getCode(), this::blankQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.MULTI_SELECT.getCode(), this::selectQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.BMI.getCode(), this::bmiQuestionHandler);
        questionHandlerMap.put(QuestionTypeEnum.SEX_SELECT.getCode(), this::sexSelectQuestionHandler);

        QuestionContextDTO contextDTO = new QuestionContextDTO();
        contextDTO.setHealthEvaluateId(request.getHealthEvaluateId());
        contextDTO.setHealthEvaluateQuestionId(request.getQuestion().getId());
        contextDTO.setQuestionDetailRequest(question);
        contextDTO.setOpUserId(request.getOpUserId());
        questionHandlerMap.get(question.getQuestionType()).apply(contextDTO);
        return Boolean.TRUE;
    }
}
