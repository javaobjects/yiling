package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportStrategyRecordBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/9/21
 */
@Service("strategyRecordExportService")
@Slf4j
public class StrategyRecordExportServiceImpl implements BaseExportQueryDataService<QueryStrategyActivityRecordPageRequest> {

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

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("createTime", "时间");
        FIELD.put("marketingStrategyId", "促销活动ID");
        FIELD.put("marketingStrategyName", "促销活动名称");
        FIELD.put("eid", "买家企业ID");
        FIELD.put("ename", "买家企业名称");
        FIELD.put("condition", "生效条件");
        FIELD.put("strategyResult", "促销结果");
        FIELD.put("implementResult", "执行结果");
        FIELD.put("remark", "备注");
    }

    @Override
    public QueryExportDataDTO queryData(QueryStrategyActivityRecordPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<StrategyActivityRecordDTO> page;
        int current = 1;
        log.info("【营销活动参与明细导出】运营后台营销活动参与明细导出，请求数据为:[{}]", request);
        do {
            request.setCurrent(current);
            request.setSize(500);
            //  查询导出的数据填入data
            page = strategyActivityRecordApi.pageList(request);
            for (StrategyActivityRecordDTO record : page.getRecords()) {
                ExportStrategyRecordBO recordBO = PojoUtils.map(record, ExportStrategyRecordBO.class);
                recordBO.setImplementResult(1 == record.getSendStatus() ? "成功" : "失败");
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(record.getEid());
                if (Objects.nonNull(enterpriseDTO)) {
                    recordBO.setEname(enterpriseDTO.getName());
                }
                StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.getByType(record.getStrategyType());
                if (strategyTypeEnum == StrategyTypeEnum.ORDER_AMOUNT) {
                    OrderDTO orderInfo = orderApi.getOrderInfo(record.getOrderId());
                    if (Objects.nonNull(orderInfo)) {
                        recordBO.setCondition("订单号：" + orderInfo.getOrderNo());
                    }
                    StrategyAmountLadderDTO strategyAmountLadderDTO = strategyActivityApi.queryAmountLadderById(record.getLadderId());

                    List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(record.getMarketingStrategyId(), record.getLadderId());
                    List<String> strategyResult = getStrategyResultByStrategyGiftDTOList(strategyGiftDTOList);
                    recordBO.setStrategyResult(strategyResult);
                } else if (strategyTypeEnum == StrategyTypeEnum.SIGN_DAYS) {
                } else if (strategyTypeEnum == StrategyTypeEnum.CYCLE_TIME) {
                    List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(record.getMarketingStrategyId(), record.getLadderId());
                    List<String> strategyResult = getStrategyResultByStrategyGiftDTOList(strategyGiftDTOList);
                    recordBO.setStrategyResult(strategyResult);
                } else if (strategyTypeEnum == StrategyTypeEnum.PURCHASE_MEMBER) {
                    MemberOrderDTO memberOrderDTO = memberOrderApi.getMemberOrderByOrderId(record.getOrderId());
                    if (Objects.nonNull(memberOrderDTO)) {
                        MemberBuyStageDTO memberBuyStageDTO = memberBuyStageApi.getById(memberOrderDTO.getBuyStageId());
                        recordBO.setCondition("购买" + memberOrderDTO.getMemberName() + memberBuyStageDTO.getValidTime() + "天,实付" + memberOrderDTO.getPayAmount());
                    }
                    List<StrategyGiftDTO> strategyGiftDTOList = strategyGiftApi.listGiftByActivityIdAndLadderId(record.getMarketingStrategyId(), null);
                    List<String> strategyResult = getStrategyResultByStrategyGiftDTOList(strategyGiftDTOList);
                    recordBO.setStrategyResult(strategyResult);
                }
                if (2 == recordBO.getSendStatus()) {
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
                        recordBO.setRemark(remarkSb.toString());
                    }
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(recordBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("营销活动参与明细导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryStrategyActivityRecordPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryStrategyActivityRecordPageRequest.class);
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
