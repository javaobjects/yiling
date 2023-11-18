package com.yiling.admin.b2b.lotteryactivity.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.lotteryactivity.form.QueryJoinDetailPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.QueryLotteryActivityGetPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.QueryLotteryActivityPageForm;
import com.yiling.admin.b2b.lotteryactivity.form.QueryLotteryActivityRulePageForm;
import com.yiling.admin.b2b.lotteryactivity.form.SaveLotteryActivityBasicForm;
import com.yiling.admin.b2b.lotteryactivity.form.SaveLotteryActivitySettingForm;
import com.yiling.admin.b2b.lotteryactivity.form.UpdateCashRewardForm;
import com.yiling.admin.b2b.lotteryactivity.form.UpdateLotteryActivityReceiptInfoForm;
import com.yiling.admin.b2b.lotteryactivity.form.UpdateRewardSettingForm;
import com.yiling.admin.b2b.lotteryactivity.vo.LotteryActivityBasicVO;
import com.yiling.admin.b2b.lotteryactivity.vo.LotteryActivityDetailVO;
import com.yiling.admin.b2b.lotteryactivity.vo.LotteryActivityGetVO;
import com.yiling.admin.b2b.lotteryactivity.vo.LotteryActivityJoinDetailVO;
import com.yiling.admin.b2b.lotteryactivity.vo.LotteryActivityListItemVO;
import com.yiling.admin.b2b.lotteryactivity.vo.LotteryActivityRuleVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityGetApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGetDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityRulePageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityBasicRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivitySettingRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateCashRewardRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityProgressEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽奖活动 Controller
 *
 * @author: lun.yu
 * @date: 2022-08-29
 */
@Slf4j
@RestController
@RequestMapping("/lotteryActivity")
@Api(tags = "抽奖活动接口")
public class LotteryActivityController extends BaseController {

    @DubboReference
    LotteryActivityApi lotteryActivityApi;
    @DubboReference
    LotteryActivityJoinDetailApi lotteryActivityJoinDetailApi;
    @DubboReference
    LotteryActivityGetApi lotteryActivityGetApi;
    @DubboReference(async = true)
    LotteryActivityTimesApi lotteryActivityTimesApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "查询抽奖活动分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<LotteryActivityListItemVO>> queryListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryLotteryActivityPageForm form) {
        QueryLotteryActivityPageRequest request = PojoUtils.map(form, QueryLotteryActivityPageRequest.class);
        if (form.getPlatformType() == 1) {
            request.setPlatform(LotteryActivityPlatformEnum.B2B.getCode());
        } else {
            request.setNotInPlatformList(ListUtil.toList(LotteryActivityPlatformEnum.B2B.getCode()));
        }
        Page<LotteryActivityListItemVO> page = PojoUtils.map(lotteryActivityApi.queryListPage(request), LotteryActivityListItemVO.class);

        page.getRecords().forEach(lotteryActivityListItemVO -> {
            // 如果为停用状态，要展示为已结束
            if (lotteryActivityListItemVO.getStatus().equals(EnableStatusEnum.DISABLED.getCode())) {
                lotteryActivityListItemVO.setProgress(LotteryActivityProgressEnum.END.getCode());
            }

            // 是否展示修改按钮
            lotteryActivityListItemVO.setShowUpdateButton(lotteryActivityListItemVO.getStatus() == 1 && lotteryActivityListItemVO.getProgress().equals(LotteryActivityProgressEnum.UNDO.getCode()));

            // 是否展示停用按钮
            if (lotteryActivityListItemVO.getStatus() == 1 && lotteryActivityListItemVO.getProgress().equals(LotteryActivityProgressEnum.UNDO.getCode())) {
                lotteryActivityListItemVO.setShowStopButton(true);
            } else
                lotteryActivityListItemVO.setShowStopButton(lotteryActivityListItemVO.getStatus() == 1 && lotteryActivityListItemVO.getProgress().equals(LotteryActivityProgressEnum.GOING.getCode()));
        });

        return Result.success(page);
    }

    @ApiOperation(value = "参与规则分页列表")
    @PostMapping("/queryRulePage")
    public Result<Page<LotteryActivityRuleVO>> queryRulePage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryLotteryActivityRulePageForm form) {
        QueryLotteryActivityRulePageRequest request = PojoUtils.map(form, QueryLotteryActivityRulePageRequest.class);
        Page<LotteryActivityRuleDTO> activityRuleDTOPage = lotteryActivityApi.queryRulePage(request);

        return Result.success(PojoUtils.map(activityRuleDTOPage, LotteryActivityRuleVO.class));
    }

    @ApiOperation(value = "查询抽奖活动详情")
    @GetMapping("/get")
    public Result<LotteryActivityDetailVO> get(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        LotteryActivityDetailBO lotteryActivityDetailBO = lotteryActivityApi.get(id, null);
        LotteryActivityDetailVO activityDetailVO = PojoUtils.map(lotteryActivityDetailBO, LotteryActivityDetailVO.class);
        if (StrUtil.isNotEmpty(activityDetailVO.getBgPicture())) {
            activityDetailVO.setBgPictureUrl(fileService.getUrl(lotteryActivityDetailBO.getBgPicture(), FileTypeEnum.LOTTERY_ACTIVITY_FILE));
        }

        LotteryActivityBasicVO activityBasicVO = activityDetailVO.getLotteryActivityBasic();
        Date now = new Date();
        if (activityBasicVO.getStartTime().after(now)) {
            activityBasicVO.setProgress(LotteryActivityProgressEnum.UNDO.getCode());
        } else if (activityBasicVO.getStartTime().before(now) && activityBasicVO.getEndTime().after(now)) {
            activityBasicVO.setProgress(LotteryActivityProgressEnum.GOING.getCode());
        } else if (activityBasicVO.getEndTime().before(now)) {
            activityBasicVO.setProgress(LotteryActivityProgressEnum.END.getCode());
        }
        // 如果为停用状态，要展示为已结束
        if (activityBasicVO.getStatus().equals(EnableStatusEnum.DISABLED.getCode())) {
            activityBasicVO.setProgress(LotteryActivityProgressEnum.END.getCode());
        }

        return Result.success(activityDetailVO);
    }

    @ApiOperation(value = "停用抽奖活动")
    @GetMapping("/stop")
    @Log(title = "停用抽奖活动", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> stop(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        lotteryActivityApi.stopActivity(id, adminInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "添加或修改抽奖活动基本信息")
    @PostMapping("/saveActivityBasic")
    @Log(title = "添加或修改抽奖活动基本信息", businessType = BusinessTypeEnum.INSERT)
    public Result<LotteryActivityBasicVO> saveActivityBasic(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveLotteryActivityBasicForm form) {
        SaveLotteryActivityBasicRequest activityBasicRequest = PojoUtils.map(form, SaveLotteryActivityBasicRequest.class);
        activityBasicRequest.setOpUserId(adminInfo.getCurrentUserId());
        LotteryActivityDTO lotteryActivityDTO = lotteryActivityApi.saveActivityBasic(activityBasicRequest);
        return Result.success(PojoUtils.map(lotteryActivityDTO, LotteryActivityBasicVO.class));
    }

    @ApiOperation(value = "保存抽奖活动设置信息")
    @PostMapping("/saveActivitySetting")
    @Log(title = "新增抽奖活动设置信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> saveActivitySetting(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveLotteryActivitySettingForm form) {
        SaveLotteryActivitySettingRequest activitySettingRequest = PojoUtils.map(form, SaveLotteryActivitySettingRequest.class);
        activitySettingRequest.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityApi.saveActivitySetting(activitySettingRequest);
        return Result.success();
    }

    @ApiOperation(value = "抽奖次数/中奖次数分页列表")
    @PostMapping("/queryJoinDetailPage")
    public Result<Page<LotteryActivityJoinDetailVO>> queryJoinDetailPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryJoinDetailPageForm form) {
        QueryJoinDetailPageRequest request = PojoUtils.map(form, QueryJoinDetailPageRequest.class);
        if (form.getPageType() == 2) {
            request.setNotInRewardTypeList(ListUtil.toList(LotteryActivityRewardTypeEnum.EMPTY.getCode()));
        }
        Page<LotteryActivityJoinDetailDTO> queryJoinDetailPage = lotteryActivityJoinDetailApi.queryJoinDetailPage(request);

        List<LotteryActivityJoinDetailDTO> records = queryJoinDetailPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(PojoUtils.map(queryJoinDetailPage, LotteryActivityJoinDetailVO.class));
        }

        List<LotteryActivityJoinDetailVO> joinDetailVOList = records.stream().map(lotteryActivityJoinDetailDTO -> {
            LotteryActivityJoinDetailVO joinDetailVO =  PojoUtils.map(lotteryActivityJoinDetailDTO, LotteryActivityJoinDetailVO.class);
            if (Objects.nonNull(lotteryActivityJoinDetailDTO.getLotteryActivityReceiptInfo())) {
                PojoUtils.map(lotteryActivityJoinDetailDTO.getLotteryActivityReceiptInfo(), joinDetailVO);
            }
            joinDetailVO.setId(lotteryActivityJoinDetailDTO.getId());
            return joinDetailVO;
        }).collect(Collectors.toList());

        Page<LotteryActivityJoinDetailVO> detailVOPage = PojoUtils.map(queryJoinDetailPage, LotteryActivityJoinDetailVO.class);
        detailVOPage.setRecords(joinDetailVOList);

        return Result.success(detailVOPage);
    }

    @ApiOperation(value = "兑付奖品")
    @PostMapping("/cashReward")
    @Log(title = "兑付奖品", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> cashReward(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateCashRewardForm form) {
        UpdateCashRewardRequest cashRewardRequest = PojoUtils.map(form, UpdateCashRewardRequest.class);
        cashRewardRequest.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityJoinDetailApi.cashReward(cashRewardRequest);
        return Result.success();
    }

    @ApiOperation(value = "修改兑付信息")
    @PostMapping("/updateCashInfo")
    @Log(title = "修改兑付信息", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateCashInfo(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateLotteryActivityReceiptInfoForm form) {
        UpdateLotteryActivityReceiptInfoRequest receiptInfoRequest = PojoUtils.map(form, UpdateLotteryActivityReceiptInfoRequest.class);
        receiptInfoRequest.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityJoinDetailApi.updateCashInfo(receiptInfoRequest);
        return Result.success();
    }

    @ApiOperation(value = "抽奖机会明细分页列表")
    @PostMapping("/queryLotteryGetPage")
    public Result<Page<LotteryActivityGetVO>> queryLotteryGetPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryLotteryActivityGetPageForm form) {
        QueryLotteryActivityGetPageRequest request = PojoUtils.map(form, QueryLotteryActivityGetPageRequest.class);
        Page<LotteryActivityGetDTO> lotteryActivityGetDTOPage = lotteryActivityGetApi.queryPageList(request);

        return Result.success(PojoUtils.map(lotteryActivityGetDTOPage, LotteryActivityGetVO.class));
    }

    @ApiOperation(value = "复制抽奖活动")
    @GetMapping("/copyLottery")
    public Result<Long> copyLottery(@CurrentUser CurrentAdminInfo adminInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        Long newLotteryActivityId = lotteryActivityApi.copyLottery(id, adminInfo.getCurrentUserId());
        return Result.success(newLotteryActivityId);
    }

    @ApiOperation(value = "修改奖品设置信息", notes = "进行中的抽奖活动支持修改概率和每天最大抽中数量")
    @PostMapping("/updateRewardSetting")
    public Result<Page<LotteryActivityGetVO>> updateRewardSetting(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateRewardSettingForm form) {
        UpdateRewardSettingRequest request = PojoUtils.map(form, UpdateRewardSettingRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        lotteryActivityApi.updateRewardSetting(request);
        return Result.success();
    }

}
