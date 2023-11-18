package com.yiling.admin.b2b.strategy.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.strategy.form.QueryStrategyActivityRecordPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityRecordVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.api.StrategyActivityRecordApi;
import com.yiling.marketing.strategy.api.StrategyGiftApi;
import com.yiling.marketing.strategy.dto.StrategyActivityRecordDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftFailDTO;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordPageRequest;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 营销活动记录表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-09-06
 */
@Api(tags = "策略满赠-营销活动记录")
@RestController
@RequestMapping("/strategy/activityRecord")
public class StrategyActivityRecordController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    StrategyGiftApi strategyGiftApi;

    @DubboReference
    StrategyActivityRecordApi strategyActivityRecordApi;

    @DubboReference
    CouponActivityApi couponActivityApi;

    @DubboReference
    MemberOrderApi memberOrderApi;

    @DubboReference
    MemberBuyStageApi memberBuyStageApi;

    @DubboReference
    LotteryActivityApi lotteryActivityApi;

    @DubboReference
    OrderApi orderApi;

    @ApiOperation(value = "分页列表营销活动参与明细-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyActivityRecordVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategyActivityRecordPageForm form) {
        QueryStrategyActivityRecordPageRequest request = PojoUtils.map(form, QueryStrategyActivityRecordPageRequest.class);
        Page<StrategyActivityRecordDTO> dtoPage = strategyActivityRecordApi.pageList(request);
        Page<StrategyActivityRecordVO> voPage = PojoUtils.map(dtoPage, StrategyActivityRecordVO.class);
        for (StrategyActivityRecordVO record : voPage.getRecords()) {
            record.setImplementResult(record.getSendStatus());
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(record.getEid());
            if (Objects.nonNull(enterpriseDTO)) {
                record.setEname(enterpriseDTO.getName());
            }
            StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.getByType(record.getStrategyType());
            if (strategyTypeEnum == StrategyTypeEnum.ORDER_AMOUNT) {
                OrderDTO orderInfo = orderApi.getOrderInfo(record.getOrderId());
                if (Objects.nonNull(orderInfo)) {
                    record.setCondition("订单号：" + orderInfo.getOrderNo());
                }
                StrategyAmountLadderDTO strategyAmountLadderDTO = strategyActivityApi.queryAmountLadderById(record.getLadderId());

                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(record.getMarketingStrategyId(), record.getLadderId());
                List<String> strategyResult = getStrategyResultByStrategyGiftDTOList(strategyGiftDTOList);
                record.setStrategyResult(strategyResult);
            } else if (strategyTypeEnum == StrategyTypeEnum.SIGN_DAYS) {
            } else if (strategyTypeEnum == StrategyTypeEnum.CYCLE_TIME) {
                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(record.getMarketingStrategyId(), record.getLadderId());
                List<String> strategyResult = getStrategyResultByStrategyGiftDTOList(strategyGiftDTOList);
                record.setStrategyResult(strategyResult);
            } else if (strategyTypeEnum == StrategyTypeEnum.PURCHASE_MEMBER) {
                MemberOrderDTO memberOrderDTO = memberOrderApi.getMemberOrderByOrderId(record.getOrderId());
                if (Objects.nonNull(memberOrderDTO)) {
                    MemberBuyStageDTO memberBuyStageDTO = memberBuyStageApi.getById(memberOrderDTO.getBuyStageId());
                    record.setCondition("购买" + memberOrderDTO.getMemberName() + memberBuyStageDTO.getName() + ",实付" + memberOrderDTO.getPayAmount());
                }
                List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(record.getMarketingStrategyId(), null);
                List<String> strategyResult = getStrategyResultByStrategyGiftDTOList(strategyGiftDTOList);
                record.setStrategyResult(strategyResult);
            }
            if (2 == record.getSendStatus()) {
                List<StrategyGiftFailDTO> giftFailDTOList = strategyActivityRecordApi.listByRecordId(record.getId());
                if (CollUtil.isNotEmpty(giftFailDTOList)) {
                    StringBuilder remarkSb = new StringBuilder();
                    for (StrategyGiftFailDTO strategyGiftFailDTO : giftFailDTOList) {
                        if (1 == strategyGiftFailDTO.getType() || 2 == strategyGiftFailDTO.getType()) {
                            remarkSb.append("优惠券ID").append(strategyGiftFailDTO.getGiftId()).append(strategyGiftFailDTO.getRemark()).append(",有").append(strategyGiftFailDTO.getCount()).append("张未发;");
                        }
                        if (3 == strategyGiftFailDTO.getType()) {
                            remarkSb.append("抽奖活动ID").append(strategyGiftFailDTO.getGiftId()).append(strategyGiftFailDTO.getRemark()).append(";");
                        }
                    }
                    record.setRemark(remarkSb.toString());
                }
            }
        }
        return Result.success(voPage);
    }

    private List<String> getStrategyResultByStrategyGiftDTOList(List<StrategyGiftDTO> strategyGiftDTOList) {
        List<String> strategyResult = new ArrayList<>();
        for (StrategyGiftDTO strategyGiftDTO : strategyGiftDTOList) {
            if (1 == strategyGiftDTO.getType() || 2 == strategyGiftDTO.getType()) {
                CouponActivityDetailDTO couponActivityDetailDTO = couponActivityApi.getActivityCouponById(strategyGiftDTO.getGiftId());
                if (Objects.isNull(couponActivityDetailDTO)) {
                    continue;
                }
                String memberType = (null != couponActivityDetailDTO.getMemberType() && couponActivityDetailDTO.getMemberType() == 1) ? "商品优惠券" : "会员优惠券";
                strategyResult.add("【" + memberType + "】" + couponActivityDetailDTO.getName() + " * " + strategyGiftDTO.getCount());

            }
            if (3 == strategyGiftDTO.getType()) {
                LotteryActivityDTO lotteryActivityDTO = lotteryActivityApi.getById(strategyGiftDTO.getGiftId());
                if (Objects.isNull(lotteryActivityDTO)) {
                    continue;
                }
                strategyResult.add("【抽奖次数】" + lotteryActivityDTO.getActivityName() + " * " + strategyGiftDTO.getCount());
            }
        }
        return strategyResult;
    }
}
