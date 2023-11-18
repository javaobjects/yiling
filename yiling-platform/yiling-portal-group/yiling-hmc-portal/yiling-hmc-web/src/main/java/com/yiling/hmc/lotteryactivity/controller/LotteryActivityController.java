package com.yiling.hmc.lotteryactivity.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.lotteryactivity.form.SaveLotteryActivityReceiptInfoForm;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityExpressCashVO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashStatusEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.user.system.bo.CurrentUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.gzh.api.HmcGzhApi;
import com.yiling.hmc.lotteryactivity.form.QueryJoinDetailPageForm;
import com.yiling.hmc.lotteryactivity.form.ReduceLotteryTimesForm;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityDetailPageVO;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityHitVO;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityJoinDetailVO;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityMyRewardVO;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityRewardSettingVO;
import com.yiling.hmc.lotteryactivity.vo.LotteryActivityRuleVO;
import com.yiling.hmc.lotteryactivity.vo.LotteryResultVO;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityJoinDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryResultBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityHitRandomGenerateDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.ReduceLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SignAddTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityJoinRuleStageEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityProgressEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * C端抽奖活动 Controller
 *
 * @author: lun.yu
 * @date: 2022-09-14
 */
@Slf4j
@RestController
@RequestMapping("/lotteryActivity")
@Api(tags = "C端抽奖活动接口")
public class LotteryActivityController extends BaseController {

    @DubboReference
    LotteryActivityApi lotteryActivityApi;
    @DubboReference
    LotteryActivityJoinDetailApi lotteryActivityJoinDetailApi;
    @DubboReference
    LotteryActivityTimesApi lotteryActivityTimesApi;
    @DubboReference
    HmcGzhApi hmcGzhApi;

    @Autowired
    FileService fileService;

    /**
     * 公众号服务类
     */
    @Autowired
    WxMpService wxMpService;

    @Autowired
    RedisService redisService;

    @ApiOperation(value = "获取抽奖活动页信息")
    @GetMapping("/getActivityPage")
    @Log(title = "获取抽奖活动页信息", businessType = BusinessTypeEnum.OTHER)
    public Result<LotteryActivityDetailPageVO> getActivityPage(@CurrentUser CurrentUserInfo staffInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        LotteryActivityDetailBO activityDetailBO = lotteryActivityApi.get(id, LotteryActivityPlatformEnum.ZGH);

        // 设置奖品图片
        activityDetailBO.getActivityRewardSettingList().forEach(rewardSettingBO -> {
            if (LotteryActivityRewardTypeEnum.REAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())
                    || LotteryActivityRewardTypeEnum.VIRTUAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())) {
                if (StrUtil.isNotEmpty(rewardSettingBO.getRewardImg())) {
                    rewardSettingBO.setRewardImg(fileService.getUrl(rewardSettingBO.getRewardImg(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
                }
            }
        });

        LotteryActivityDetailPageVO detailPageVO = PojoUtils.map(activityDetailBO.getLotteryActivityBasic(), LotteryActivityDetailPageVO.class);
        detailPageVO.setActivityRewardSettingList(PojoUtils.map(activityDetailBO.getActivityRewardSettingList(), LotteryActivityRewardSettingVO.class));
        detailPageVO.setSignGive(activityDetailBO.getActivityJoinRule().getSignGive());
        detailPageVO.setInviteGive(activityDetailBO.getActivityJoinRule().getInviteGive());
        // 活动进度
        Date now = new Date();
        if (activityDetailBO.getLotteryActivityBasic().getStatus().equals(EnableStatusEnum.DISABLED.getCode())) {
            detailPageVO.setProgress(LotteryActivityProgressEnum.END.getCode());
        } else {
            if (detailPageVO.getStartTime().after(now)) {
                detailPageVO.setProgress(LotteryActivityProgressEnum.UNDO.getCode());
            } else if (detailPageVO.getStartTime().before(now) && detailPageVO.getEndTime().after(now)) {
                detailPageVO.setProgress(LotteryActivityProgressEnum.GOING.getCode());
            } else if (detailPageVO.getEndTime().before(now)) {
                detailPageVO.setProgress(LotteryActivityProgressEnum.END.getCode());
            }
        }

        // 检查是否为当日第一次访问，第一次访问需要增加抽奖次数
        lotteryActivityApi.checkTodayAccess(id, 2, staffInfo.getCurrentUserId());

        // 剩余抽奖次数
        Integer times = lotteryActivityTimesApi.getAvailableTimes(id, 2, staffInfo.getCurrentUserId());
        detailPageVO.setLotteryTimes(times);

        /*
         * 中奖名单规则：
         *  如果中奖次数不超过20次，则按中奖时间倒序展示20个中奖记录。 如果中奖次数超过20次，则按奖品等级、中奖时间排序取20条中奖记录倒序排列，滚动展示。
         *  如一等奖有2次，二等奖有8次，三等奖有20次。 则展示一等奖2次+二等奖8次+三等奖10次，按中奖时间倒序排列展示。
         */
        List<LotteryActivityJoinDetailDTO> joinDetailDTOList = lotteryActivityJoinDetailApi.queryHitList(id, 20);
        List<LotteryActivityHitVO> activityHitVOList = PojoUtils.map(joinDetailDTOList, LotteryActivityHitVO.class);

        List<LotteryActivityHitVO> allHitVOList = ListUtil.toList();
        // 获取当前时段随机生成的中奖用户
        List<LotteryActivityHitRandomGenerateDTO> randomGenerateDTOList = lotteryActivityApi.getCurrentGenerate(id);
        if (CollUtil.isNotEmpty(randomGenerateDTOList)) {
            allHitVOList.addAll(PojoUtils.map(randomGenerateDTOList, LotteryActivityHitVO.class));
            // 剩下要添加真实的
            int size = 20 - randomGenerateDTOList.size();
            if (size > 0 && activityHitVOList.size() > 0) {
                if (activityHitVOList.size() <= size) {
                    allHitVOList.addAll(activityHitVOList);
                } else {
                    List<LotteryActivityHitVO> hitVOList = activityHitVOList.subList(0, size);
                    allHitVOList.addAll(hitVOList);
                }

            }
        } else {
            allHitVOList.addAll(activityHitVOList);
        }
        // 脱敏用户名
        if (CollUtil.isNotEmpty(allHitVOList)) {
            allHitVOList.forEach(lotteryActivityHitVO -> lotteryActivityHitVO.setUname(this.protectedName(lotteryActivityHitVO.getUname())));
        }
        detailPageVO.setActivityHitList(allHitVOList);

        // 校验今日是否已经签到
        boolean todaySignFlag = lotteryActivityApi.checkTodaySign(id, 2, staffInfo.getCurrentUserId());
        detailPageVO.setTodaySignFlag(todaySignFlag);
        // 返回当前服务器时间
        detailPageVO.setCurrentTime(new Date());


        // redis 放入访问记录
        String lottery_activity_access = RedisKey.generate("hmc", "lottery_activity_access", String.valueOf(staffInfo.getCurrentUserId()));
        redisService.set(lottery_activity_access, DateUtil.date().getTime());

        return Result.success(detailPageVO);
    }

    @ApiOperation(value = "获取活动规则")
    @GetMapping("/getActivityRule")
    public Result<LotteryActivityRuleVO> getActivityRule(@CurrentUser CurrentUserInfo staffInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        LotteryActivityDetailBO activityDetailBO = lotteryActivityApi.get(id, LotteryActivityPlatformEnum.ZGH);
        LotteryActivityRuleVO activityRuleVO = new LotteryActivityRuleVO();
        activityRuleVO.setActivityName(activityDetailBO.getLotteryActivityBasic().getActivityName());
        activityRuleVO.setStartTime(activityDetailBO.getLotteryActivityBasic().getStartTime());
        activityRuleVO.setEndTime(activityDetailBO.getLotteryActivityBasic().getEndTime());
        activityRuleVO.setContent(activityDetailBO.getContent());
        return Result.success(activityRuleVO);
    }

    @ApiOperation(value = "我的奖品分页列表")
    @PostMapping("/getMyRewardListPage")
    public Result<Page<LotteryActivityMyRewardVO>> getMyRewardListPage(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid QueryJoinDetailPageForm form) {
        QueryJoinDetailPageRequest request = PojoUtils.map(form, QueryJoinDetailPageRequest.class);
        request.setPlatformType(2);
        request.setNotInRewardTypeList(ListUtil.toList(LotteryActivityRewardTypeEnum.EMPTY.getCode()));
        request.setUid(staffInfo.getCurrentUserId());
        Page<LotteryActivityJoinDetailDTO> joinDetailDTOPage = lotteryActivityJoinDetailApi.queryJoinDetailPage(request);
        Map<Long, Boolean> showAcceptButtonMap = MapUtil.newHashMap();
        Map<Long, Boolean> showUpdateAcceptButtonMap = MapUtil.newHashMap();
        joinDetailDTOPage.getRecords().forEach(lotteryActivityJoinDetailDTO -> {
            if (Objects.isNull(lotteryActivityJoinDetailDTO.getLotteryActivityReceiptInfo()) && lotteryActivityJoinDetailDTO.getRewardType().equals(LotteryActivityRewardTypeEnum.REAL_GOODS.getCode())
                    && lotteryActivityJoinDetailDTO.getStatus().equals(LotteryActivityCashStatusEnum.UN_CASH.getCode())) {
                showAcceptButtonMap.put(lotteryActivityJoinDetailDTO.getId(), true);
            } else {
                showAcceptButtonMap.put(lotteryActivityJoinDetailDTO.getId(), false);
            }
        });

        joinDetailDTOPage.getRecords().forEach(lotteryActivityJoinDetailDTO -> {
            if (Objects.nonNull(lotteryActivityJoinDetailDTO.getLotteryActivityReceiptInfo()) && lotteryActivityJoinDetailDTO.getRewardType().equals(LotteryActivityRewardTypeEnum.REAL_GOODS.getCode())
                    && lotteryActivityJoinDetailDTO.getStatus().equals(LotteryActivityCashStatusEnum.UN_CASH.getCode())) {
                showUpdateAcceptButtonMap.put(lotteryActivityJoinDetailDTO.getId(), true);
            } else {
                showUpdateAcceptButtonMap.put(lotteryActivityJoinDetailDTO.getId(), false);
            }
        });

        Page<LotteryActivityMyRewardVO> myRewardVOPage = PojoUtils.map(joinDetailDTOPage, LotteryActivityMyRewardVO.class);
        myRewardVOPage.getRecords().forEach(lotteryActivityMyRewardVO -> {
            // 是否展示选择收货地址按钮处理
            lotteryActivityMyRewardVO.setShowAcceptFlag(showAcceptButtonMap.get(lotteryActivityMyRewardVO.getId()));
            // 是否展示修改收货地址按钮
            lotteryActivityMyRewardVO.setShowUpdateAcceptFlag(showUpdateAcceptButtonMap.get(lotteryActivityMyRewardVO.getId()));
            // 是否展示查看快递
            lotteryActivityMyRewardVO.setShowExpressFlag(lotteryActivityMyRewardVO.getRewardType().equals(LotteryActivityRewardTypeEnum.REAL_GOODS.getCode())
                    && lotteryActivityMyRewardVO.getStatus().equals(LotteryActivityCashStatusEnum.HAD_CASH.getCode()));
            // 是否展示查看兑换码
            lotteryActivityMyRewardVO.setShowCardFlag(lotteryActivityMyRewardVO.getRewardType().equals(LotteryActivityRewardTypeEnum.VIRTUAL_GOODS.getCode())
                    && lotteryActivityMyRewardVO.getStatus().equals(LotteryActivityCashStatusEnum.HAD_CASH.getCode()));
        });

        return Result.success(myRewardVOPage);
    }

    @ApiOperation(value = "添加收货地址")
    @PostMapping("/addReceiptInfo")
    @Log(title = "添加收货地址", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> addReceiptInfo(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid SaveLotteryActivityReceiptInfoForm form) {
        UpdateLotteryActivityReceiptInfoRequest request = PojoUtils.map(form, UpdateLotteryActivityReceiptInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        lotteryActivityJoinDetailApi.addReceiptInfo(request);
        return Result.success();
    }

    @ApiOperation(value = "修改收货地址")
    @PostMapping("/updateReceiptInfo")
    @Log(title = "修改收货地址", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateReceiptInfo(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid SaveLotteryActivityReceiptInfoForm form) {
        UpdateLotteryActivityReceiptInfoRequest request = PojoUtils.map(form, UpdateLotteryActivityReceiptInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        lotteryActivityJoinDetailApi.updateReceiptInfo(request);
        return Result.success();
    }

    @ApiOperation(value = "查看奖品详情")
    @GetMapping("/getRewardDetail")
    public Result<LotteryActivityJoinDetailVO> getRewardDetail(@CurrentUser CurrentUserInfo staffInfo, @ApiParam(value = "参与详情ID", required = true) @RequestParam("joinDetailId") Long joinDetailId) {
        LotteryActivityJoinDetailBO joinDetailBO = lotteryActivityJoinDetailApi.getRewardDetail(joinDetailId);
        return Result.success(PojoUtils.map(joinDetailBO, LotteryActivityJoinDetailVO.class));
    }

    @ApiOperation(value = "校验是否需要关注公众号")
    @GetMapping("/checkSubscribeHmcGZH")
    @Log(title = "校验是否需要关注公众号", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> checkSubscribeHmcGZH(@CurrentUser CurrentUserInfo staffInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("lotteryActivityId") Long lotteryActivityId) {
        // 校验活动参与门槛
        LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityApi.getByLotteryActivityId(lotteryActivityId);
        if (Objects.nonNull(joinRuleDTO) && LotteryActivityJoinRuleStageEnum.getByCode(joinRuleDTO.getJoinStage()) == LotteryActivityJoinRuleStageEnum.SUBSCRIBE) {
            // 校验是否关注健康管理中心公众号
            boolean flag = hmcGzhApi.hasUserSubscribeHmcGZH(staffInfo.getCurrentUserId());
            log.info("[hasUserSubscribeHmcGZH]返回flag:{}", flag);
            if (!flag) {
                return Result.success(true);
            }
        }
        return Result.success(false);
    }

    @ApiOperation(value = "进行抽奖", notes = "返回错误码：20019（您还未关注健康管理中心公众号），需要弹出关注二维码页")
    @PostMapping("/executeLottery")
    @Log(title = "进行抽奖", businessType = BusinessTypeEnum.INSERT)
    public Result<LotteryResultVO> executeLottery(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid ReduceLotteryTimesForm form) {
        // 校验活动参与门槛
        LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityApi.getByLotteryActivityId(form.getLotteryActivityId());
        if (Objects.nonNull(joinRuleDTO) && LotteryActivityJoinRuleStageEnum.getByCode(joinRuleDTO.getJoinStage()) == LotteryActivityJoinRuleStageEnum.SUBSCRIBE) {
            // 校验是否关注健康管理中心公众号
            boolean flag = hmcGzhApi.hasUserSubscribeHmcGZH(staffInfo.getCurrentUserId());
            if (!flag) {
                return Result.failed(20019, "您还未关注健康管理中心公众号");
            }
        }

        ReduceLotteryTimesRequest request = PojoUtils.map(form, ReduceLotteryTimesRequest.class);
        request.setUid(staffInfo.getCurrentUserId());
        request.setPlatformType(2);
        request.setOpUserId(staffInfo.getCurrentUserId());
        LotteryResultBO lotteryResultBO = lotteryActivityTimesApi.executeLottery(request);
        // 设置奖品图片
        if (StrUtil.isNotEmpty(lotteryResultBO.getRewardImg()) && (LotteryActivityRewardTypeEnum.getByCode(lotteryResultBO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS ||
                LotteryActivityRewardTypeEnum.getByCode(lotteryResultBO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS)) {
            lotteryResultBO.setRewardImg(fileService.getUrl(lotteryResultBO.getRewardImg(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
        }
        return Result.success(PojoUtils.map(lotteryResultBO, LotteryResultVO.class));
    }

    @ApiOperation(value = "签到（返回剩余的抽奖次数）")
    @GetMapping("/sign")
    @Log(title = "签到", businessType = BusinessTypeEnum.UPDATE)
    public Result<Integer> sign(@CurrentUser CurrentUserInfo staffInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        SignAddTimesRequest request = new SignAddTimesRequest();
        request.setLotteryActivityId(id);
        request.setPlatformType(2);
        request.setUid(staffInfo.getCurrentUserId());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(lotteryActivityTimesApi.sign(request));
    }

    @ApiOperation(value = "生成活动海报")
    @GetMapping("/getActivityImage")
    @Log(title = "生成活动海报", businessType = BusinessTypeEnum.UPDATE)
    public Result<String> getActivityImage(@CurrentUser CurrentUserInfo userInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        LotteryActivityDTO lotteryActivityDTO = lotteryActivityApi.getById(id);
        if (Objects.isNull(lotteryActivityDTO)) {
            return Result.failed("根据活动id未获取到活动信息");
        }
        if (StrUtil.isBlank(lotteryActivityDTO.getBgPicture())) {
            return Result.failed("请先配置海报背景图");
        }
        String url = fileService.getUrl(lotteryActivityDTO.getBgPicture(), FileTypeEnum.LOTTERY_ACTIVITY_FILE);
        if (StrUtil.isBlank(url)) {
            return Result.failed("根据活动id未获取到活动海报信息");
        }
        return Result.success(hmcGzhApi.createActivityShareImage(userInfo.getCurrentUserId(), id, url));
    }

    @ApiOperation(value = "生成活动二维码")
    @GetMapping("/getQrImage")
    @Log(title = "生成活动海报", businessType = BusinessTypeEnum.UPDATE)
    public Result<String> getQrImage(@ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) throws WxErrorException {
        String sceneStr = String.format("qt:20_actId:%s", id);
        WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(sceneStr);
        String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
        return Result.success(url);
    }

    @ApiOperation(value = "查看快递信息/兑换码")
    @GetMapping("/getExpressOrCash")
    public Result<LotteryActivityExpressCashVO> getExpressOrCash(@CurrentUser CurrentUserInfo userInfo, @ApiParam(value = "参与详情ID", required = true) @RequestParam("joinDetailId") Long joinDetailId) {
        LotteryActivityExpressCashBO expressCashBO = lotteryActivityJoinDetailApi.getExpressOrCash(joinDetailId);
        return Result.success(PojoUtils.map(expressCashBO, LotteryActivityExpressCashVO.class));
    }

    public String protectedName(String userName) {
        userName = userName.trim();
        char[] r = userName.toCharArray();
        String resultName = "";
        if (r.length == 2) {
            resultName = r[0] + "*";
        }
        if (r.length > 2) {
            String star = "";
            for (int i = 0; i < r.length - 2; i++) {
                star = star + "*";
            }
            resultName = r[0] + star + r[r.length - 1];
        }
        return resultName;
    }

}
