package com.yiling.dataflow.statistics.api.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.yiling.dataflow.statistics.bo.FlowDailyStatisticsBO;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsPageRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowGoodsBatchDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowGoodsBatchService;
import com.yiling.dataflow.order.service.FlowGoodsSpecMappingService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.statistics.api.FlowBalanceStatisticsApi;
import com.yiling.dataflow.statistics.bo.EnterpriseMonthQuantityBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecInfoBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecNoMatchedBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecQuantityBO;
import com.yiling.dataflow.statistics.bo.GoodsSpecStatisticsBO;
import com.yiling.dataflow.statistics.dto.DeteleFlowBalanceStatisticeRequest;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsDTO;
import com.yiling.dataflow.statistics.dto.FlowBalanceStatisticsMonthDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecInfoDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecNoMatchedDTO;
import com.yiling.dataflow.statistics.dto.GoodsSpecStatisticsDTO;
import com.yiling.dataflow.statistics.dto.request.FlushGoodsSpecIdRequest;
import com.yiling.dataflow.statistics.dto.request.GoodsSpecStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsMonthRequest;
import com.yiling.dataflow.statistics.dto.request.QueryBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.dto.request.RecommendScoreRequest;
import com.yiling.dataflow.statistics.dto.request.SaveFlowBalanceStatisticsRequest;
import com.yiling.dataflow.statistics.entity.FlowBalanceStatisticsDO;
import com.yiling.dataflow.statistics.service.FlowBalanceStatisticsService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsMatchApi;
import com.yiling.goods.medicine.dto.MatchGoodsDTO;
import com.yiling.goods.medicine.dto.MatchedGoodsDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/7/11
 */
@DubboService
@Slf4j
public class FlowBalanceStatisticsApiImpl implements FlowBalanceStatisticsApi {

    @Autowired
    private FlowBalanceStatisticsService flowBalanceStatisticsService;

    @Autowired
    private FlowGoodsBatchDetailService flowGoodsBatchDetailService;

    @Autowired
    private FlowGoodsBatchService flowGoodsBatchService;

    @Autowired
    private FlowPurchaseService flowPurchaseService;

    @Autowired
    private FlowSaleService flowSaleService;

    @Autowired
    private FlowGoodsSpecMappingService flowGoodsSpecMappingService;

    @DubboReference
    private GoodsMatchApi goodsMatchApi;

    @Override
    public Integer deleteFlowBalanceStatistics(DeteleFlowBalanceStatisticeRequest request) {
        return flowBalanceStatisticsService.deleteFlowBalanceStatistics(request);
    }

    @Override
    public List<FlowBalanceStatisticsDTO> getFlowBalanceStatisticsEnterpriseList() {
        List<FlowBalanceStatisticsDTO> flowBalanceStatisticsDTOList = new ArrayList<>();

        List<FlowBalanceStatisticsDO> list = flowBalanceStatisticsService.getFlowBalanceStatisticsEnterpriseList();
        for (FlowBalanceStatisticsDO flowBalanceStatisticsDO : list) {
            FlowBalanceStatisticsDTO flowBalanceStatisticsDTO = new FlowBalanceStatisticsDTO();
            flowBalanceStatisticsDTO.setEid(flowBalanceStatisticsDO.getEid());
            flowBalanceStatisticsDTO.setEname(flowBalanceStatisticsDO.getEname());

            flowBalanceStatisticsDTOList.add(flowBalanceStatisticsDTO);
        }

        return flowBalanceStatisticsDTOList;
    }

    @Override
    public Page<FlowBalanceStatisticsMonthDTO> getMonthPageList(QueryBalanceStatisticsMonthRequest request) {
        // 获取条件中月份的最后的统计时间
        String gbTime = getCurrentMonthEndDateStr(request.getTime());
        request.setGbTime(gbTime);

        Page<FlowBalanceStatisticsDO> page = flowBalanceStatisticsService.getMonthPageList(request);
        if (page.getRecords() == null || page.getRecords().size() == 0) {
            return new Page<>(request.getCurrent(), request.getSize());
        }

        List<Long> eidList = page.getRecords().stream().map(FlowBalanceStatisticsDO::getEid).collect(Collectors.toList());

        // 查询采购未匹配的商品数量
        List<FlowPurchaseDO> flowPurchaseDOList = flowPurchaseService.getCantMatchGoodsQuantityList(eidList, request.getTime());

        // 查询销售未匹配的商品数量
        List<FlowSaleDO> flowSaleDOList = flowSaleService.getCantMatchGoodsQuantityList(eidList, request.getTime());

        // 查询月末库存未匹配的商品数量
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = flowGoodsBatchDetailService.getEndMonthCantMatchGoodsQuantityList(eidList, gbTime);

        // 采购销售上月数量统计
        String lastMonth = getLastMonthStr(request.getTime());
        List<FlowBalanceStatisticsDO> flowBalanceStatisticsDOList = flowBalanceStatisticsService.getEnterpriseMonthFlowList(lastMonth, eidList);

        // 查询企业月初库存量  查询flow_balance_statistics 上个月最后一天的库存
        String beginMonth = request.getTime() + "-01";
        List<EnterpriseMonthQuantityBO> beginMonthQuantityList = flowBalanceStatisticsService.getEnterpriseBeginMonthList(beginMonth, eidList);

        List<FlowBalanceStatisticsMonthDTO> flowBalanceStatisticsMonthDTOList = new ArrayList<>();
        for (FlowBalanceStatisticsDO flowBalanceStatisticsDO : page.getRecords()) {
            FlowBalanceStatisticsMonthDTO flowBalanceStatisticsMonthDTO = PojoUtils.map(flowBalanceStatisticsDO, FlowBalanceStatisticsMonthDTO.class);

            // 设置年月时间
            flowBalanceStatisticsMonthDTO.setTime(request.getTime());

            // 设置月初库存量
            Long eid = flowBalanceStatisticsMonthDTO.getEid();
            BigDecimal beginMonthQuantity = beginMonthQuantityList.stream().filter(b -> b.getEid().equals(eid)).findAny().map(EnterpriseMonthQuantityBO::getQuantity).orElse(new BigDecimal(0));
            flowBalanceStatisticsMonthDTO.setBeginMonthQuantity(beginMonthQuantity.longValue());

            // 设置采购未匹配的商品数量
            BigDecimal noMatchPoQuantity = flowPurchaseDOList.stream().filter(f -> f.getEid().equals(eid)).findAny().map(FlowPurchaseDO::getPoQuantity).orElse(new BigDecimal(0));
            flowBalanceStatisticsMonthDTO.setNoMatchPoQuantity(noMatchPoQuantity.longValue());

            // 设置销售未匹配的商品数量
            BigDecimal noMatchSoQuantity = flowSaleDOList.stream().filter(f -> f.getEid().equals(eid)).findAny().map(FlowSaleDO::getSoQuantity).orElse(new BigDecimal(0));
            flowBalanceStatisticsMonthDTO.setNoMatchSoQuantity(noMatchSoQuantity.longValue());

            // 设置月末库存未匹配商品数量
            BigDecimal noMatchGbQuantity = flowGoodsBatchDetailDOList.stream().filter(f -> f.getEid().equals(eid)).findAny().map(FlowGoodsBatchDetailDO::getGbNumber).orElse(new BigDecimal(0));
            flowBalanceStatisticsMonthDTO.setNoMatchGbQuantity(noMatchGbQuantity.longValue());

            FlowBalanceStatisticsDO flowStatistics = flowBalanceStatisticsDOList.stream().filter(f -> f.getEid().equals(eid)).findAny().orElse(null);
            if (flowStatistics == null) {
                flowStatistics = new FlowBalanceStatisticsDO();
                flowStatistics.setPoQuantity(0L);
                flowStatistics.setSoQuantity(0L);
            }

            // 设置采购增长率
            String poGrowthRate = null;
            if (flowStatistics.getPoQuantity() == 0) {
                poGrowthRate = flowBalanceStatisticsMonthDTO.getPoQuantity() * 100 + "%";
            } else {
                BigDecimal currentPoQuantity = new BigDecimal(flowBalanceStatisticsMonthDTO.getPoQuantity());
                BigDecimal lastPoQuantity = new BigDecimal(flowStatistics.getPoQuantity());
                poGrowthRate = (currentPoQuantity.subtract(lastPoQuantity)).divide(lastPoQuantity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) + "%";
            }
            flowBalanceStatisticsMonthDTO.setPoGrowthRate(poGrowthRate);

            // 设置月末增长率
            String soGrowthRate = null;
            if (flowStatistics.getSoQuantity() == 0) {
                soGrowthRate = flowBalanceStatisticsMonthDTO.getSoQuantity() * 100 + "%";
            } else {
                BigDecimal currentSoQuantity = new BigDecimal(flowBalanceStatisticsMonthDTO.getSoQuantity());
                BigDecimal lastSoQuantity = new BigDecimal(flowStatistics.getSoQuantity());
                soGrowthRate = (currentSoQuantity.subtract(lastSoQuantity)).divide(lastSoQuantity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) + "%";
            }
            flowBalanceStatisticsMonthDTO.setSoGrowthRate(soGrowthRate);

            flowBalanceStatisticsMonthDTOList.add(flowBalanceStatisticsMonthDTO);
        }

        Page<FlowBalanceStatisticsMonthDTO> resultPage = new Page<>(request.getCurrent(), request.getSize(), page.getTotal());
        resultPage.setRecords(flowBalanceStatisticsMonthDTOList);

        return resultPage;
    }

    @Override
    public List<FlowBalanceStatisticsDTO> getFlowBalanceStatisticsList(QueryBalanceStatisticsRequest request) {
        List<FlowBalanceStatisticsDO> flowBalanceStatisticsDOList = flowBalanceStatisticsService.getFlowBalanceStatisticsList(request);
        return PojoUtils.map(flowBalanceStatisticsDOList, FlowBalanceStatisticsDTO.class);
    }

    @Override
    public Page<GoodsSpecStatisticsDTO> getGoodsSpecStatisticsListPage(GoodsSpecStatisticsRequest request) {
        List<GoodsSpecStatisticsDTO> goodsSpecStatisticsDTOList = new ArrayList<>();
        // 月初库存，取选择月份的前一个月最后一天的值
        String beginMonthDateTime = getLastMonthEndDateStr(request.getMonthTime());

        // 月末库存，如果是非当月则查询该月最后一天的库存，如果是当前月则查询当天的库存
        String endMonthDateTime =  getCurrentMonthEndDateStr(request.getMonthTime());

        request.setBeginMonthDateTime(beginMonthDateTime);
        request.setEndMonthDateTime(endMonthDateTime);

        Page<GoodsSpecStatisticsBO> page = flowBalanceStatisticsService.getGoodsSpecStatisticsListPage(request);
        if (page.getRecords() == null || page.getRecords().size() == 0) {
            return new Page<>(page.getCurrent(), page.getSize());
        }

        // 获取商品月初库存和月末库存
        List<Long> specificationIdList = page.getRecords().stream().map(GoodsSpecStatisticsBO::getSpecificationId).collect(Collectors.toList());

        List<GoodsSpecQuantityBO> goodsSpecBeginMonthQuantityBOList = flowGoodsBatchDetailService.findGoodsDateQuantityByEidAndDateTime(request.getEid(), beginMonthDateTime, specificationIdList);

        List<GoodsSpecQuantityBO> goodsSpecEndMonthQuantityBOList =  flowGoodsBatchDetailService.findGoodsDateQuantityByEidAndDateTime(request.getEid(), endMonthDateTime, specificationIdList);

        for (GoodsSpecStatisticsBO goodsSpecStatisticsBO : page.getRecords()) {
            GoodsSpecStatisticsDTO goodsSpecStatisticsDTO = PojoUtils.map(goodsSpecStatisticsBO, GoodsSpecStatisticsDTO.class);
            if (goodsSpecStatisticsDTO.getSpecificationId() == 0) {
                goodsSpecStatisticsDTO.setGoodsName("未匹配商品");
                goodsSpecStatisticsDTO.setSpec("未匹配规格");
            }
            BigDecimal beginMonthQuantity = goodsSpecBeginMonthQuantityBOList.stream()
                    .filter(g -> g.getSpecificationId().equals(goodsSpecStatisticsDTO.getSpecificationId()))
                    .findAny().map(GoodsSpecQuantityBO::getQuantity).orElse(new BigDecimal(0));
            goodsSpecStatisticsDTO.setBeginMonthQuantity(beginMonthQuantity);

            BigDecimal endMonthQuantity = goodsSpecEndMonthQuantityBOList.stream()
                    .filter(g -> g.getSpecificationId().equals(goodsSpecStatisticsDTO.getSpecificationId()))
                    .findAny().map(GoodsSpecQuantityBO::getQuantity).orElse(new BigDecimal(0));
            goodsSpecStatisticsDTO.setBeginMonthQuantity(beginMonthQuantity);
            goodsSpecStatisticsDTO.setEndMonthQuantity(endMonthQuantity);

            goodsSpecStatisticsDTOList.add(goodsSpecStatisticsDTO);
        }
        Page<GoodsSpecStatisticsDTO> resultPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resultPage.setRecords(goodsSpecStatisticsDTOList);
        return resultPage;
    }

    @Override
    public List<GoodsSpecInfoDTO> getGoodsSpecInfoList(Long eid, String monthTime) {
        List<GoodsSpecInfoBO> goodsSpecInfoBOList = flowBalanceStatisticsService.getGoodsSpecInfoList(eid, monthTime);
        return PojoUtils.map(goodsSpecInfoBOList, GoodsSpecInfoDTO.class);
    }

    @Override
    public List<GoodsSpecNoMatchedDTO> goodsSpecNoMatchedList(GoodsSpecStatisticsRequest request) {
        // 月初库存，取选择月份的前一个月最后一天的值
        String beginMonthDateTime = getLastMonthEndDateStr(request.getMonthTime());

        // 月末库存，如果是非当月则查询该月最后一天的库存，如果是当前月则查询当天的库存
        String endMonthDateTime =  getCurrentMonthEndDateStr(request.getMonthTime());

        request.setBeginMonthDateTime(beginMonthDateTime);
        request.setEndMonthDateTime(endMonthDateTime);

        List<GoodsSpecNoMatchedBO> goodsSpecInfoBOList =  flowBalanceStatisticsService.goodsSpecNoMatchedList(request);
        // 去重
        Set<String> set = new HashSet<>();
        List<GoodsSpecNoMatchedDTO> goodsSpecNoMatchedDTOList = new ArrayList<>();
        for (GoodsSpecNoMatchedBO goodsSpecNoMatchedBO : goodsSpecInfoBOList) {
            if (set.contains(goodsSpecNoMatchedBO.getGoodsName() + "_"+ goodsSpecNoMatchedBO.getSpec()+"_"+goodsSpecNoMatchedBO.getManufacturer())) {
                continue;
            }
            GoodsSpecNoMatchedDTO goodsSpecNoMatchedDTO = PojoUtils.map(goodsSpecNoMatchedBO, GoodsSpecNoMatchedDTO.class);
            goodsSpecNoMatchedDTOList.add(goodsSpecNoMatchedDTO);
            set.add(goodsSpecNoMatchedBO.getGoodsName() + "_"+ goodsSpecNoMatchedBO.getSpec()+"_"+goodsSpecNoMatchedBO.getManufacturer());
        }
        return goodsSpecNoMatchedDTOList;
    }

    @Override
    public Integer getRecommendScore(RecommendScoreRequest request) {
        List<MatchGoodsDTO> targets = new ArrayList<>();
        MatchGoodsDTO target = new MatchGoodsDTO();
        target.setName(request.getTargetGoodsName());
        target.setSpecification(request.getTargetSpec());
        target.setManufacturer(request.getTargetManufacturer());
        targets.add(target);

        MatchGoodsDTO matchGoodsDTO = new MatchGoodsDTO();
        matchGoodsDTO.setName(request.getGoodsName());
        matchGoodsDTO.setSpecification(request.getSpec());
        matchGoodsDTO.setManufacturer(request.getManufacturer());
        MatchedGoodsDTO matchedGoods = goodsMatchApi.matchingGoodsWithSpec(matchGoodsDTO,targets);

        return new Double(matchedGoods.getPer() * 100).intValue();
    }


    @Override
    public void flushGoodsSpecificationId(FlushGoodsSpecIdRequest request) {
        flowBalanceStatisticsService.flushGoodsSpecificationId(request);
    }

    @Override
    public Boolean isUsedSpecificationId(Long specificationId) {
        // 查询流向数据是否已存在匹配的specificationId
        if (flowPurchaseService.isUsedSpecificationId(specificationId)) {
            return true;
        }
        if (flowSaleService.isUsedSpecificationId(specificationId)) {
            return true;
        }
        if (flowGoodsBatchService.isUsedSpecificationId(specificationId)) {
            return true;
        }
        if (flowGoodsBatchDetailService.isUsedSpecificationId(specificationId)) {
            return true;
        }

        // 查询映射表是否已存在匹配的specificationId
        if (flowGoodsSpecMappingService.isUsedSpecificationId(specificationId)) {
            return true;
        }

        return false;
    }

    @Override
    public Page<FlowDailyStatisticsBO> page(QueryBalanceStatisticsPageRequest request) {
        return flowBalanceStatisticsService.page(request);
    }

    private String getLastMonthEndDateStr(String monthStr) {
        String monthBeginTime = monthStr + "-01";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginMonthDate = null;
        try {
             beginMonthDate = sdf.parse(monthBeginTime);
        } catch (ParseException e) {
            throw new ServiceException(ResultCode.FAILED);
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(beginMonthDate);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date lastMonthEndDate = calendar.getTime();

        return sdf.format(lastMonthEndDate);
    }


    private String getCurrentMonthEndDateStr(String monthStr) {
        String dateTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

        if (sdf.format(new Date()).equals(monthStr)) {    // 当前月
            dateTime = sdf2.format(new Date());
        } else {    // 非当前月
            Date currentBeginDat = null;
            try {
                currentBeginDat = sdf2.parse(monthStr + "-01");
            } catch (ParseException e) {
                throw new ServiceException(ResultCode.FAILED);
            }

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            calendar.setTime(currentBeginDat);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            dateTime = sdf2.format(calendar.getTime());
        }
        return dateTime;
    }

    private String getLastMonthStr(String monthStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(monthStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        String lastMonth = sdf.format(calendar.getTime());
        return lastMonth;
    }
}
