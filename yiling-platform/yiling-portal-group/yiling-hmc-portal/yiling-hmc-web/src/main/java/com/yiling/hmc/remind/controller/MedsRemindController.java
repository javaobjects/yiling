package com.yiling.hmc.remind.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.hmc.remind.api.MedsRemindApi;
import com.yiling.hmc.remind.dto.MedsRemindDTO;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import com.yiling.hmc.remind.dto.request.*;
import com.yiling.hmc.remind.enums.HmcRemindTimePhaseEnum;
import com.yiling.hmc.remind.form.*;
import com.yiling.hmc.remind.vo.*;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 用药提醒控制器
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/5/30
 */
@Slf4j
@RestController
@RequestMapping("/meds/remind")
@Api(tags = "用药提醒控制器")
public class MedsRemindController {

    @DubboReference
    MedsRemindApi medsRemindApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Autowired
    private SpringAsyncConfig asyncConfig;

    @ApiOperation(value = "01、选择药品")
    @PostMapping("queryStandardGoodsPage")
    @Log(title = "选择药品", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<StandardGoodsVO>> queryStandardSpecificationPage(@RequestBody @Valid StandardSpecificationPageForm form) {
        StandardGoodsInfoRequest request = new StandardGoodsInfoRequest();
        PojoUtils.map(form, request);
        Page<StandardGoodsInfoDTO> specificationDTOPage = standardGoodsApi.getStandardGoodsInfo(request);
        if (form.getCurrent() == 1) {
            StandardGoodsInfoDTO inputObj = new StandardGoodsInfoDTO();
            inputObj.setName(form.getName());
            specificationDTOPage.getRecords().add(0, inputObj);
        }
        Page<StandardGoodsVO> specificationVOPage = PojoUtils.map(specificationDTOPage, StandardGoodsVO.class);
        return Result.success(specificationVOPage);
    }

    @ApiOperation(value = "02、获取药品用法用量")
    @PostMapping("queryUseAge")
    @Log(title = "获取药品用法用量", businessType = BusinessTypeEnum.OTHER)
    public Result<String> queryUseAge(@RequestBody @Valid QueryStandardGoodsUsageForm form) {
        if (Objects.isNull(form) || form.getId() <= 0) {
            return Result.failed("参数无效");
        }
        StandardGoodsAllInfoDTO goodsInfo = standardGoodsApi.getStandardGoodsById(form.getId());
        String usageDosage = Optional.ofNullable(goodsInfo).map(StandardGoodsAllInfoDTO::getGoodsInstructionsInfo).map(StandardInstructionsGoodsDTO::getUsageDosage).orElse(null);
        return Result.success(usageDosage);
    }

    @ApiOperation(value = "03、保存更新用药提醒")
    @PostMapping("saveOrUpdateMedsRemind")
    @Log(title = "保存用药提醒", businessType = BusinessTypeEnum.OTHER)
    public Result<MedsRemindVO> saveOrUpdateMedsRemind(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid SaveMedsRemindForm form) {
        log.info("[saveOrUpdateMedsRemind]保存更新用药提醒,入参：{}", JSONUtil.toJsonStr(form));
        form.check();
        SaveMedsRemindRequest saveMedsRemindRequest = PojoUtils.map(form, SaveMedsRemindRequest.class);
        saveMedsRemindRequest.setOpUserId(currentUser.getCurrentUserId());
        MedsRemindDTO medsRemindDTO = medsRemindApi.saveOrUpdateMedsRemind(saveMedsRemindRequest);
        MedsRemindVO medsRemindVO = PojoUtils.map(medsRemindDTO, MedsRemindVO.class);
        log.info("[saveOrUpdateMedsRemind]保存更新用药提醒,返参：{}", medsRemindVO);
        return Result.success(medsRemindVO);
    }

    @ApiOperation(value = "04、全部用药")
    @PostMapping("getAllMedsRemind")
    @Log(title = "全部用药", businessType = BusinessTypeEnum.OTHER)
    public Result<List<MedsRemindVO>> getAllMedsRemind(@CurrentUser CurrentUserInfo currentUser) {
        if (Objects.isNull(currentUser)) {
            log.info("用户未登录，跳过处理");
            return Result.success(Lists.newArrayList());
        }
        List<MedsRemindDTO> medsRemindDTOList = medsRemindApi.getAllMedsRemind(currentUser.getCurrentUserId());
        List<MedsRemindVO> medsRemindVOList = PojoUtils.map(medsRemindDTOList, MedsRemindVO.class);
        log.info("[getAllMedsRemind]全部用药,返参：{}", medsRemindVOList);
        return Result.success(medsRemindVOList);
    }

    @ApiOperation(value = "05、用药详情")
    @PostMapping("getMedsRemindDetail")
    @Log(title = "用药详情", businessType = BusinessTypeEnum.OTHER)
    public Result<MedsRemindVO> getMedsRemindDetail(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid QueryMedsRemindForm form) {
        MedsRemindDTO medsRemindDTO = medsRemindApi.getMedsRemindDetail(Optional.ofNullable(currentUser).map(CurrentUserInfo::getCurrentUserId).orElse(0L), form.getId());
        MedsRemindVO medsRemindVO = PojoUtils.map(medsRemindDTO, MedsRemindVO.class);
        log.info("[getMedsRemindDetail]用药详情,返参：{}", medsRemindVO);
        return Result.success(medsRemindVO);
    }

    @ApiOperation(value = "06、停止提醒")
    @PostMapping("stopMedsRemind")
    @Log(title = "停止提醒", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> stopMedsRemind(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid StopMedsRemindForm form) {
        boolean result = medsRemindApi.stopMedsRemind(form.getId());
        log.info("[stopMedsRemind]停止提醒,返参：{}", result);
        return Result.success(result);
    }

    @ApiOperation(value = "07、设为已用/未用")
    @PostMapping("checkMedsRemind")
    @Log(title = "设为已用/未用", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> checkMedsRemind(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid CheckMedsRemindForm form) {
        CheckMedsRemindRequest request = PojoUtils.map(form, CheckMedsRemindRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        boolean result = medsRemindApi.checkMedsRemind(request);
        log.info("[checkMedsRemind]设为已用/未用,返参：{}", result);
        return Result.success(result);
    }

    @ApiOperation(value = "08、接受提醒")
    @PostMapping("acceptRemind")
    @Log(title = "接受提醒", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> acceptRemind(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid AcceptMedsRemindForm form) {
        AcceptMedsRemindRequest request = PojoUtils.map(form, AcceptMedsRemindRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        boolean result = medsRemindApi.acceptRemind(request);
        log.info("[acceptRemind]接受提醒,返参：{}", result);
        return Result.success(result);
    }

    @ApiOperation(value = "09、取消提醒")
    @PostMapping("cancelRemind")
    @Log(title = "取消提醒", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> cancelRemind(@CurrentUser CurrentUserInfo currentUser, @RequestBody @Valid MedsRemindBaseForm form) {
        MedsRemindBaseRequest request = PojoUtils.map(form, MedsRemindBaseRequest.class);
        request.setOpUserId(currentUser.getCurrentUserId());
        boolean result = medsRemindApi.cancelRemind(request);
        log.info("[acceptRemind]取消提醒,返参：{}", result);
        return Result.success(result);
    }

    @ApiOperation(value = "10、今日提醒")
    @PostMapping("todayRemind")
    @Log(title = "今日提醒", businessType = BusinessTypeEnum.OTHER)
    public Result<List<TodayMedsRemindTaskVO>> todayRemind(@CurrentUser CurrentUserInfo currentUser) {
        if (Objects.isNull(currentUser)) {
            log.info("用户未登录，跳过处理");
            return Result.success(Lists.newArrayList());
        }
        List<MedsRemindTaskDetailDTO> medsRemindDTOList = medsRemindApi.todayRemind(currentUser.getCurrentUserId());
        if (CollUtil.isEmpty(medsRemindDTOList)) {
            return Result.success(Lists.newArrayList());
        }
        List<MedsRemindTaskDetailVO> medsRemindVOList = PojoUtils.map(medsRemindDTOList, MedsRemindTaskDetailVO.class);
        Map<String, List<MedsRemindTaskDetailVO>> timeMap = medsRemindVOList.stream().collect(Collectors.groupingBy(item -> DateUtil.format(item.getInitSendTime(), "HH:mm")));

        List<TodayMedsRemindTaskVO> result = Lists.newArrayList();
        timeMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(item -> {
            TodayMedsRemindTaskVO todayMedsRemindTaskVO = TodayMedsRemindTaskVO.builder().remindTime(item.getKey()).remindTaskDetailVOList(item.getValue()).build();

            HmcRemindTimePhaseEnum phaseEnum = HmcRemindTimePhaseEnum.getFlagByTime(item.getKey());
            if (Objects.isNull(phaseEnum)) {
                log.info("未获取到时间范围枚举，跳过处理:{}", item.getKey());
                return;
            }
            todayMedsRemindTaskVO.setTimeIconFlag(phaseEnum.getFlag());
            result.add(todayMedsRemindTaskVO);
        });
        log.info("[acceptRemind]今日提醒,返参：{}", JSONUtil.toJsonStr(result));
        return Result.success(result);
    }


    @ApiOperation(value = "11、历史记录")
    @PostMapping("medsHistory")
    @Log(title = "历史记录", businessType = BusinessTypeEnum.OTHER)
    public Result<List<MedsRemindHistoryVO>> medsHistory(@CurrentUser CurrentUserInfo currentUser) {

        List<MedsRemindDTO> medsRemindDTOList = medsRemindApi.medsHistory(currentUser.getCurrentUserId());
        List<MedsRemindHistoryVO> historyVOS = PojoUtils.map(medsRemindDTOList, MedsRemindHistoryVO.class);
        log.info("[medsHistory]历史记录,返参：{}", JSONUtil.toJsonStr(historyVOS));
        return Result.success(historyVOS);
    }

    @ApiOperation(value = "12、清除历史记录")
    @PostMapping("clearHistory")
    @Log(title = "清除历史记录", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> clearHistory(@CurrentUser CurrentUserInfo currentUser) {
        boolean result = medsRemindApi.clearHistory(currentUser.getCurrentUserId());
        log.info("[clearHistory]清除历史记录,返参：{}", JSONUtil.toJsonStr(result));
        return Result.success(result);
    }

    @ApiOperation(value = "13、更新订阅状态")
    @PostMapping("updateMedsRemindSubscribe")
    @Log(title = "更新订阅状态", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> updateMedsRemindSubscribe(@CurrentUser CurrentUserInfo currentUser, @RequestBody String subscribeStr) {
        log.info("[updateMedsRemindSubscribe]更新订阅状态,入参: user：{}，订阅参数:\n {}", currentUser, subscribeStr);
        CompletableFuture.runAsync(() -> medsRemindApi.updateMedsRemindSubscribe(currentUser.getCurrentUserId(), subscribeStr), asyncConfig.getAsyncExecutor());
        return Result.success(Boolean.TRUE);
    }

}
