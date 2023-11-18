package com.yiling.hmc.evaluate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.yiling.cms.evaluate.api.HealthEvaluateApi;
import com.yiling.cms.evaluate.api.HealthEvaluateQuestionApi;
import com.yiling.cms.evaluate.dto.*;
import com.yiling.cms.evaluate.dto.request.SubmitEvaluateResultRequest;
import com.yiling.cms.evaluate.dto.request.UserStartEvaluateRequest;
import com.yiling.cms.evaluate.enums.HealthEvaluateTypeEnum;
import com.yiling.cms.evaluate.enums.RangeTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.JsonUtil;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.constant.GoodsConstant;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.hmc.evaluate.form.GetBMIResultForm;
import com.yiling.hmc.evaluate.form.SubmitEvaluateResultForm;
import com.yiling.hmc.evaluate.vo.*;
import com.yiling.hmc.gzh.api.HmcGzhApi;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.RecommendDoctorDTO;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 健康测评
 *
 * @author: fan.shen
 * @date: 2022-12-12
 */
@Api(tags = "健康测评")
@RestController
@RequestMapping("/healthEvaluate")
@Slf4j
public class HealthEvaluateController extends BaseController {

    @DubboReference
    HmcGzhApi hmcGzhApi;

    @DubboReference
    HealthEvaluateApi healthEvaluateApi;

    @DubboReference
    HealthEvaluateQuestionApi healthEvaluateQuestionApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Autowired
    FileService fileService;

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    RedisService redisService;

    @ApiOperation(value = "测评首页")
    @PostMapping("indexPage")
    @Log(title = "测评首页", businessType = BusinessTypeEnum.OTHER)
    public Result<HealthEvaluateIndexPageVO> indexPage(@CurrentUser CurrentUserInfo currentUser) {
        List<HealthEvaluateDTO> healthEvaluateDTOList = healthEvaluateApi.getIndexPage();
        HealthEvaluateIndexPageVO indexPageVO = new HealthEvaluateIndexPageVO();
        if (CollUtil.isEmpty(healthEvaluateDTOList)) {
            log.info("未获取到测评");
            return Result.success(indexPageVO);
        }

        // 获取测评题目数量
        List<Long> healthEvaluateIdList = healthEvaluateDTOList.stream().map(HealthEvaluateDTO::getId).collect(Collectors.toList());
        Map<Long, Long> evaluateCountMap = healthEvaluateApi.getTotalQuestionByEvaluateIdList(healthEvaluateIdList);
        if (CollUtil.isEmpty(evaluateCountMap)) {
            log.info("未获取到测评题目设置");
            return Result.success(indexPageVO);
        }

        // 获取测评参与人数
        Map<Long, Long> userCountMap = healthEvaluateApi.getTotalUserByEvaluateIdList(healthEvaluateIdList);

        List<HealthEvaluateVO> evaluateVOList = PojoUtils.map(healthEvaluateDTOList, HealthEvaluateVO.class);

        evaluateVOList.forEach(item -> {
            if (evaluateCountMap.containsKey(item.getId())) {
                item.setQuestionCount(evaluateCountMap.get(item.getId()));
            }

            if (userCountMap.containsKey(item.getId())) {
                item.setUserCount(userCountMap.get(item.getId()));
            }
        });

        // 获取健康测评
        List<HealthEvaluateVO> healthEvaluateVOS = evaluateVOList.stream().filter(item -> HealthEvaluateTypeEnum.HEALTH.getCode().equals(item.getHealthEvaluateType())).collect(Collectors.toList());

        // 获取心理测评
        List<HealthEvaluateVO> psychologyEvaluateVOS = evaluateVOList.stream().filter(item -> HealthEvaluateTypeEnum.PSYCHOLOGY.getCode().equals(item.getHealthEvaluateType())).collect(Collectors.toList());

        indexPageVO.setHealthList(healthEvaluateVOS);
        indexPageVO.setPsychologyList(psychologyEvaluateVOS);

        // redis 放入访问记录
        String health_evaluate_access = RedisKey.generate("hmc", "health_evaluate_access", String.valueOf(currentUser.getCurrentUserId()));
        redisService.set(health_evaluate_access, DateUtil.date().getTime());

        return Result.success(indexPageVO);
    }

    public static void main(String[] args) {
        long x = DateUtil.currentSeconds();
        System.out.println(x);
        long time = DateUtil.date().getTime();
        System.out.println(time);
        DateTime date = DateUtil.date(time);
        System.out.println(date);
    }

    @ApiOperation(value = "测评详情")
    @GetMapping("healthEvaluateDetail")
    public Result<HealthEvaluateVO> healthEvaluateDetail(@ApiParam(value = "健康测评ID", required = true) @RequestParam("id") Long id) {
        HealthEvaluateDTO evaluateDTO = healthEvaluateApi.getHealthEvaluateById(id);
        HealthEvaluateVO evaluateVO = PojoUtils.map(evaluateDTO, HealthEvaluateVO.class);
        if (StrUtil.isNotBlank(evaluateVO.getCoverImage())) {
            String url = fileService.getUrl(evaluateVO.getCoverImage(), FileTypeEnum.HEALTH_COVER_IMAGE);
            evaluateVO.setCoverImage(url);
        }

        // 2、获取测评题目
        List<HealthEvaluateQuestionDTO> questionDTOList = healthEvaluateQuestionApi.getFullQuestionsByHealthEvaluateId(id);
        if (Objects.isNull(questionDTOList)) {
            return Result.failed("未获取到健康测评题目");
        }
        evaluateVO.setQuestionCount((long) questionDTOList.size());

        // 3、完成测评人数
        List<HealthEvaluateUserDTO> evaluateUserDTOList = healthEvaluateApi.getUserByEvaluateIdList(Collections.singletonList(id));
        evaluateVO.setFinishCount((long) evaluateUserDTOList.size());
        return Result.success(evaluateVO);
    }

    @ApiOperation(value = "开始测评")
    @GetMapping("startEvaluate")
    public Result<StartHealthEvaluateVO> startEvaluate(@CurrentUser CurrentUserInfo currentUser, @ApiParam(value = "健康测评ID", required = true) @RequestParam("id") Long id) {
        // 1、开始测评
        UserStartEvaluateRequest request = new UserStartEvaluateRequest();
        request.setHealthEvaluateId(id);
        request.setOpUserId(currentUser.getCurrentUserId());
        Long startEvaluateId = healthEvaluateApi.startEvaluate(request);

        // 2、获取测评题目
        List<HealthEvaluateQuestionDTO> questionDTOList = healthEvaluateQuestionApi.getFullQuestionsByHealthEvaluateId(id);
        if (Objects.isNull(questionDTOList)) {
            return Result.failed("未获取到健康测评题目");
        }
        HealthEvaluateDTO evaluateDTO = healthEvaluateApi.getHealthEvaluateById(id);
        List<HealthEvaluateQuestionVO> questionVOList = PojoUtils.map(questionDTOList, HealthEvaluateQuestionVO.class);
        StartHealthEvaluateVO startHealthEvaluateVO = new StartHealthEvaluateVO();
        startHealthEvaluateVO.setStartEvaluateId(startEvaluateId);
        startHealthEvaluateVO.setQuestionList(questionVOList);
        startHealthEvaluateVO.setHealthEvaluateName(evaluateDTO.getHealthEvaluateName());

        return Result.success(startHealthEvaluateVO);
    }


    @ApiOperation(value = "计算BMI结果")
    @PostMapping("getBMIResult")
    @Log(title = "计算BMI结果", businessType = BusinessTypeEnum.OTHER)
    public Result<BigDecimal> getBMIResult(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid GetBMIResultForm form) {
        BigDecimal height2 = form.getHeight().multiply(form.getHeight()).divide(BigDecimal.valueOf(10000));
        BigDecimal bmiResult = form.getWeight().divide(height2, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        return Result.success(bmiResult);
    }

    @ApiOperation(value = "提交测评结果")
    @PostMapping("submitEvaluateResult")
    @Log(title = "提交测评结果", businessType = BusinessTypeEnum.INSERT)
    public Result<HealthEvaluateResultVO> submitEvaluateResult(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid SubmitEvaluateResultForm form) {

        // 获取健康测评
        HealthEvaluateDTO healthEvaluate = healthEvaluateApi.getHealthEvaluateById(form.getHealthEvaluateId());

        SubmitEvaluateResultRequest request = PojoUtils.map(form, SubmitEvaluateResultRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        SubmitHealthEvaluateResultDTO evaluateResultDTO = healthEvaluateApi.submitEvaluateResult(request);
        HealthEvaluateResultVO resultVO = PojoUtils.map(evaluateResultDTO, HealthEvaluateResultVO.class);

        if (CollUtil.isNotEmpty(resultVO.getMarketGoodsList())) {
            List<Long> standardIdList = resultVO.getMarketGoodsList().stream().map(HealthEvaluateMarketGoodsVO::getStandardId).collect(Collectors.toList());
            List<StandardGoodsDTO> standardGoodsList = standardGoodsApi.getStandardGoodsByIds(standardIdList);
            Map<Long, StandardGoodsDTO> goodsDTOMap = standardGoodsList.stream().collect(Collectors.toMap(StandardGoodsDTO::getId, Function.identity()));
            for (HealthEvaluateMarketGoodsVO marketGoodsVO : resultVO.getMarketGoodsList()) {
                if (goodsDTOMap.containsKey(marketGoodsVO.getStandardId())) {
                    marketGoodsVO.setGoodsName(goodsDTOMap.get(marketGoodsVO.getStandardId()).getName());
                    String picUrl;
                    if (StrUtil.isBlank(goodsDTOMap.get(marketGoodsVO.getStandardId()).getPic())) {
                        picUrl = fileService.getUrl(GoodsConstant.GOODS_DEFAULT_PIC, FileTypeEnum.GOODS_PICTURE);
                    } else {
                        picUrl = fileService.getUrl(goodsDTOMap.get(marketGoodsVO.getStandardId()).getPic(), FileTypeEnum.GOODS_PICTURE);
                    }
                    marketGoodsVO.setPicUrl(picUrl);
                }
            }
        }
        if (CollUtil.isNotEmpty(resultVO.getMarketAdviceList())) {
            for (HealthEvaluateMarketAdviceVO marketAdviceVO : resultVO.getMarketAdviceList()) {
                if (StrUtil.isNotBlank(marketAdviceVO.getPic())) {
                    marketAdviceVO.setPic(fileService.getUrl(marketAdviceVO.getPic(), FileTypeEnum.HEALTH_EVALUATE));
                }
            }
        }

        // 获取其他测评人数最多的三个量表(去除自身)
        List<HealthEvaluateDTO> healthEvaluateDTOList = healthEvaluateApi.getIndexPage();
        healthEvaluateDTOList = healthEvaluateDTOList.stream().filter(item -> !item.getId().equals(form.getHealthEvaluateId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(healthEvaluateDTOList)) {
            List<Long> healthEvaluateIdList = healthEvaluateDTOList.stream().map(HealthEvaluateDTO::getId).collect(Collectors.toList());
            Map<Long, Long> userCountMap = healthEvaluateApi.getTotalUserByEvaluateIdList(healthEvaluateIdList);
            Map<Long, Long> evaluateCountMap = healthEvaluateApi.getTotalQuestionByEvaluateIdList(healthEvaluateIdList);
            List<HealthEvaluateVO> evaluateVOList = PojoUtils.map(healthEvaluateDTOList, HealthEvaluateVO.class);

            for (HealthEvaluateVO item : evaluateVOList) {
                if (!evaluateCountMap.containsKey(item.getId())) {
                    continue;
                }
                item.setQuestionCount(evaluateCountMap.get(item.getId()));
                if (userCountMap.containsKey(item.getId())) {
                    item.setUserCount(userCountMap.get(item.getId()));
                }
            }

            // 其他测评
            List<HealthEvaluateVO> sorted = evaluateVOList.stream().sorted(Comparator.comparing(HealthEvaluateVO::getUserCount).reversed()).collect(Collectors.toList());
            if (evaluateVOList.size() > 3) {
                List<HealthEvaluateVO> otherEvaluateList = sorted.subList(0, 3);
                resultVO.setOtherEvaluateList(otherEvaluateList);
            } else {
                resultVO.setOtherEvaluateList(sorted);
            }
        }

        // 推荐医生 -> 多个科室，取问诊量排名靠前的两名医生
        if (CollUtil.isNotEmpty(healthEvaluate.getDeptIdList())) {
            List<Integer> deptList = healthEvaluate.getDeptIdList().stream().map(Long::intValue).collect(Collectors.toList());
            List<RecommendDoctorDTO> recommendDoctorList = doctorApi.queryRecommendDoctor(deptList);
            if (CollUtil.isNotEmpty(recommendDoctorList)) {
                List<RecommendDoctorVO> doctorList = PojoUtils.map(recommendDoctorList, RecommendDoctorVO.class);

                resultVO.setRecommendDoctorList(doctorList);

                // 推荐医生更多跳转链接
                RecommendDoctorVO doctor = doctorList.stream().sorted(Comparator.comparing(RecommendDoctorVO::getDiagnosticCount)).collect(Collectors.toList()).get(0);
                resultVO.setDepartmentId(doctor.getDepartmentParentId());
                resultVO.setDepartmentParentId(doctor.getDepartmentParentId());
            }
        }

        return Result.success(resultVO);
    }

    @ApiOperation(value = "是否关注公众号")
    @PostMapping("hasUserSubscribeHmcGZH")
    public Result<Boolean> hasUserSubscribeHmcGZH(@CurrentUser CurrentUserInfo currentUser) {
        // 校验是否关注健康管理中心公众号
        boolean flag = hmcGzhApi.hasUserSubscribeHmcGZH(currentUser.getCurrentUserId());
        log.info("[hasUserSubscribeHmcGZH]返回flag:{}", flag);
        return Result.success(flag);
    }

    @ApiOperation(value = "生成测评海报")
    @GetMapping("/getEvaluateImage")
    @Log(title = "生成活动海报", businessType = BusinessTypeEnum.OTHER)
    public Result<String> getEvaluateImage(@CurrentUser CurrentUserInfo userInfo, @ApiParam(value = "健康测评ID", required = true) @RequestParam("id") Long id) {
        HealthEvaluateDTO healthEvaluate = healthEvaluateApi.getHealthEvaluateById(id);
        if (Objects.isNull(healthEvaluate)) {
            return Result.failed("根据健康测评id未获取到健康测评信息");
        }
        if (StrUtil.isBlank(healthEvaluate.getBackImage())) {
            return Result.failed("请先配置海报背景图");
        }
        String url = fileService.getUrl(healthEvaluate.getBackImage(), FileTypeEnum.HEALTH_BACK_IMAGE);
        if (StrUtil.isBlank(url)) {
            return Result.failed("根据活动id未获取到测评海报信息");
        }
        return Result.success(hmcGzhApi.createEvaluateShareImage(userInfo.getCurrentUserId(), id, url));
    }


}