package com.yiling.b2b.app.lotteryactivity.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.yiling.b2b.app.lotteryactivity.form.AddLotteryActivityReceiptInfoForm;
import com.yiling.b2b.app.lotteryactivity.form.QueryJoinDetailPageForm;
import com.yiling.b2b.app.lotteryactivity.form.ReduceLotteryTimesForm;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityDetailPageVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityHitVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityJoinDetailVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityListItemVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityMyRewardVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityRewardSettingVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryActivityRuleVO;
import com.yiling.b2b.app.lotteryactivity.vo.LotteryResultVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityGetApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityTimesApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityJoinDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryResultBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.ReduceLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGetTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityProgressEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.user.integral.api.IntegralUseRuleApi;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
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
 * b2b-app抽奖活动 Controller
 *
 * @author: lun.yu
 * @date: 2022-09-05
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
    LotteryActivityTimesApi lotteryActivityTimesApi;
    @DubboReference
    UserIntegralApi userIntegralApi;
    @DubboReference
    IntegralUseRuleApi integralUseRuleApi;
    @DubboReference
    LotteryActivityGetApi lotteryActivityGetApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "查询抽奖活动分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<LotteryActivityListItemVO>> queryListPage(@CurrentUser CurrentStaffInfo staffInfo) {
        QueryLotteryActivityPageRequest request = new QueryLotteryActivityPageRequest();
        request.setStatus(EnableStatusEnum.ENABLED.getCode());
        Page<LotteryActivityItemBO> activityItemBOPage = lotteryActivityApi.queryListPage(request);

        return Result.success(PojoUtils.map(activityItemBOPage, LotteryActivityListItemVO.class));
    }

    @ApiOperation(value = "查询抽奖活动名称列表")
    @GetMapping("/getNameList")
    public Result<CollectionObject<String>> getNameList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<String> nameList = lotteryActivityApi.getNameList(4);
        return Result.success(new CollectionObject<>(nameList));
    }

    @ApiOperation(value = "获取抽奖活动页信息")
    @GetMapping("/getActivityPage")
    public Result<LotteryActivityDetailPageVO> getActivityPage(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        LotteryActivityDetailBO activityDetailBO = lotteryActivityApi.get(id, LotteryActivityPlatformEnum.B2B);

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
        // 检查是否为第一次访问该活动，第一次访问需要增加抽奖次数
        lotteryActivityApi.checkActivityAccess(id, 1, staffInfo.getCurrentEid());

        // 剩余抽奖次数
        Integer times = lotteryActivityTimesApi.getAvailableTimes(id, 1, staffInfo.getCurrentEid());
        detailPageVO.setLotteryTimes(times);

        /**
         * 中奖名单规则：
         *  如果中奖次数不超过20次，则按中奖时间倒序展示20个中奖记录。 如果中奖次数超过20次，则按奖品等级、中奖时间排序取20条中奖记录倒序排列，滚动展示。
         *  如一等奖有2次，二等奖有8次，三等奖有20次。 则展示一等奖2次+二等奖8次+三等奖10次，按中奖时间倒序排列展示。
         */
        List<LotteryActivityJoinDetailDTO> joinDetailDTOList = lotteryActivityJoinDetailApi.queryHitList(id, 20);
        List<LotteryActivityHitVO> activityHitVOList = PojoUtils.map(joinDetailDTOList, LotteryActivityHitVO.class);
        List<LotteryActivityHitVO> hitVOList = this.hiddenName(activityHitVOList);
        detailPageVO.setActivityHitList(hitVOList);
        // 返回当前服务器时间
        detailPageVO.setCurrentTime(new Date());

        {
            //  积分兑换抽奖次数时需要的字段：用户积分总数；抽奖次数兑换规则；用户剩余兑换次数
            Integer userIntegral = userIntegralApi.getUserIntegralByUid(staffInfo.getCurrentUserId(), IntegralRulePlatformEnum.B2B.getCode());
            detailPageVO.setIntegralValue(userIntegral);

            IntegralLotteryConfigDTO integralLotteryConfigDTO = integralUseRuleApi.getRuleByLotteryActivityId(id);
            if (Objects.nonNull(integralLotteryConfigDTO)) {
                detailPageVO.setIsConfigIntegral(true);
                detailPageVO.setUseIntegralValue(integralLotteryConfigDTO.getUseIntegralValue());

                Integer everyDayTimes = integralLotteryConfigDTO.getEveryDayTimes();
                Integer useSumTimes = integralLotteryConfigDTO.getUseSumTimes();

                QueryLotteryActivityGetCountRequest request = new QueryLotteryActivityGetCountRequest();
                request.setLotteryActivityId(id);
                request.setUid(staffInfo.getCurrentEid());
                request.setGetType(LotteryActivityGetTypeEnum.INTEGRAL.getCode());
                request.setPlatformType(1);

                request.setIsToday(true);
                Integer todayUserGetTimes = lotteryActivityGetApi.countByUidAndGetType(request);
                int todayUserCanGetTimes = everyDayTimes.compareTo(todayUserGetTimes);


                request.setIsToday(false);
                Integer allUserGetTimes = lotteryActivityGetApi.countByUidAndGetType(request);
                int allUserCanGetTimes = useSumTimes.compareTo(allUserGetTimes);

                detailPageVO.setExchangeTimesLimit(Math.min(todayUserCanGetTimes, allUserCanGetTimes));
            } else {
                detailPageVO.setIsConfigIntegral(false);
            }
        }

        return Result.success(detailPageVO);
    }

    /**
     * 隐藏中奖名单的企业名称
     *  需要改为隐藏中间信息，只展示企业名称的前3个字 和 最后2个字。 中间全部改为5个星号。如：北京市*****药店。
     *  如果某个企业名称小于或等于5个字，则只展示前1个字 和 最后1个字。 如张三药店就展示为：张*****店。
     *
     * @param activityHitVOList 中奖名单
     */
    private List<LotteryActivityHitVO> hiddenName(List<LotteryActivityHitVO> activityHitVOList) {
        if (CollUtil.isNotEmpty(activityHitVOList)) {
            activityHitVOList.forEach(lotteryActivityHitVO -> {
                int length = lotteryActivityHitVO.getUname().length();
                String prefix;
                String suffix;
                if (length > 5) {
                    prefix = lotteryActivityHitVO.getUname().substring(0, 3);
                    suffix = lotteryActivityHitVO.getUname().substring(length - 2, length);
                } else {
                    prefix = lotteryActivityHitVO.getUname().substring(0, 1);
                    suffix = lotteryActivityHitVO.getUname().substring(length - 1, length);
                }
                lotteryActivityHitVO.setUname(prefix + "*****" + suffix);
            });
        }
        return activityHitVOList;
    }

    @ApiOperation(value = "获取活动规则")
    @GetMapping("/getActivityRule")
    public Result<LotteryActivityRuleVO> getActivityRule(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "抽奖活动ID", required = true) @RequestParam("id") Long id) {
        LotteryActivityDetailBO activityDetailBO = lotteryActivityApi.get(id, LotteryActivityPlatformEnum.B2B);
        LotteryActivityRuleVO activityRuleVO = PojoUtils.map(activityDetailBO, LotteryActivityRuleVO.class);
        PojoUtils.map(activityDetailBO.getLotteryActivityBasic(), activityRuleVO);

        return Result.success(activityRuleVO);
    }

    @ApiOperation(value = "我的奖品分页列表")
    @PostMapping("/getMyRewardListPage")
    public Result<Page<LotteryActivityMyRewardVO>> getMyRewardListPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryJoinDetailPageForm form) {
        QueryJoinDetailPageRequest request = PojoUtils.map(form, QueryJoinDetailPageRequest.class);
        request.setPlatformType(1);
        request.setNotInRewardTypeList(ListUtil.toList(LotteryActivityRewardTypeEnum.EMPTY.getCode()));
        request.setUid(staffInfo.getCurrentEid());
        Page<LotteryActivityJoinDetailDTO> joinDetailDTOPage = lotteryActivityJoinDetailApi.queryJoinDetailPage(request);
        Map<Long, Boolean> showAcceptButtonMap = MapUtil.newHashMap();
        joinDetailDTOPage.getRecords().forEach(lotteryActivityJoinDetailDTO -> {
            if (Objects.isNull(lotteryActivityJoinDetailDTO.getLotteryActivityReceiptInfo()) && lotteryActivityJoinDetailDTO.getRewardType().equals(LotteryActivityRewardTypeEnum.REAL_GOODS.getCode())) {
                showAcceptButtonMap.put(lotteryActivityJoinDetailDTO.getId(), true);
            } else {
                showAcceptButtonMap.put(lotteryActivityJoinDetailDTO.getId(), false);
            }
        });

        Page<LotteryActivityMyRewardVO> myRewardVOPage = PojoUtils.map(joinDetailDTOPage, LotteryActivityMyRewardVO.class);
        myRewardVOPage.getRecords().forEach(lotteryActivityMyRewardVO -> {
            // 是否展示选择收货地址按钮处理
            lotteryActivityMyRewardVO.setShowAcceptFlag(showAcceptButtonMap.get(lotteryActivityMyRewardVO.getId()));
            // 是否展示查询详情按钮：真实物品和虚拟物品才展示查看详情按钮
            lotteryActivityMyRewardVO.setShowDetailFlag(lotteryActivityMyRewardVO.getRewardType().equals(LotteryActivityRewardTypeEnum.REAL_GOODS.getCode())
                    || lotteryActivityMyRewardVO.getRewardType().equals(LotteryActivityRewardTypeEnum.VIRTUAL_GOODS.getCode()));
            // 奖品图片
            if (StrUtil.isNotEmpty(lotteryActivityMyRewardVO.getRewardImg()) && (LotteryActivityRewardTypeEnum.getByCode(lotteryActivityMyRewardVO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS ||
                    LotteryActivityRewardTypeEnum.getByCode(lotteryActivityMyRewardVO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS)) {
                lotteryActivityMyRewardVO.setRewardImg(fileService.getUrl(lotteryActivityMyRewardVO.getRewardImg(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
            }
        });

        return Result.success(myRewardVOPage);
    }

    @ApiOperation(value = "添加收货地址")
    @PostMapping("/addReceiptInfo")
    @Log(title = "添加收货地址", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> addReceiptInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddLotteryActivityReceiptInfoForm form) {
        AddLotteryActivityReceiptInfoRequest request = PojoUtils.map(form, AddLotteryActivityReceiptInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        lotteryActivityJoinDetailApi.addToBReceiptInfo(request);
        return Result.success();
    }

    @ApiOperation(value = "查看奖品详情")
    @GetMapping("/getRewardDetail")
    public Result<LotteryActivityJoinDetailVO> getRewardDetail(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "参与详情ID", required = true) @RequestParam("joinDetailId") Long joinDetailId) {
        LotteryActivityJoinDetailBO joinDetailBO = lotteryActivityJoinDetailApi.getRewardDetail(joinDetailId);
        if (StrUtil.isNotEmpty(joinDetailBO.getRewardImg()) && (LotteryActivityRewardTypeEnum.getByCode(joinDetailBO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS ||
                LotteryActivityRewardTypeEnum.getByCode(joinDetailBO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS)) {
            joinDetailBO.setRewardImg(fileService.getUrl(joinDetailBO.getRewardImg(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
        }
        LotteryActivityJoinDetailVO joinDetailVO = PojoUtils.map(joinDetailBO, LotteryActivityJoinDetailVO.class);
        // 虚拟物品查看卡号密码
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailBO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            LotteryActivityExpressCashBO expressOrCash = lotteryActivityJoinDetailApi.getExpressOrCash(joinDetailId);
            if (Objects.nonNull(expressOrCash)) {
                joinDetailVO.setCardInfoList(expressOrCash.getCardInfoList());
            }
        }

        return Result.success(joinDetailVO);
    }

    @ApiOperation(value = "进行抽奖")
    @PostMapping("/executeLottery")
    @Log(title = "进行抽奖", businessType = BusinessTypeEnum.INSERT)
    public Result<LotteryResultVO> executeLottery(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid ReduceLotteryTimesForm form) {
        ReduceLotteryTimesRequest request = PojoUtils.map(form, ReduceLotteryTimesRequest.class);
        request.setUid(staffInfo.getCurrentEid());
        request.setPlatformType(1);
        request.setOpUserId(staffInfo.getCurrentUserId());
        LotteryResultBO lotteryResultBO = lotteryActivityTimesApi.executeLottery(request);

        // 设置奖品图片
        if (StrUtil.isNotEmpty(lotteryResultBO.getRewardImg()) && (LotteryActivityRewardTypeEnum.getByCode(lotteryResultBO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS || LotteryActivityRewardTypeEnum.getByCode(lotteryResultBO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS)) {
            lotteryResultBO.setRewardImg(fileService.getUrl(lotteryResultBO.getRewardImg(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
        }
        return Result.success(PojoUtils.map(lotteryResultBO, LotteryResultVO.class));
    }

}
