package com.yiling.dataflow.statistics.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.google.common.collect.Lists;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockForecastApi;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.dataflow.statistics.dto.*;
import com.yiling.dataflow.statistics.dto.request.StockForecastInfoRequest;
import com.yiling.dataflow.statistics.dto.request.StockReferenceTimeRequest;
import com.yiling.dataflow.statistics.enums.FlowAnalyseStockStatusEnum;
import com.yiling.dataflow.statistics.service.FlowAnalyseDaySaleQuantityService;
import com.yiling.dataflow.statistics.service.FlowAnalyseDayStockQuantityService;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;
import com.yiling.dataflow.statistics.service.FlowAnalyseGoodsService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.order.order.api.OrderApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@DubboService
@Slf4j
public class FlowAnalyseStockForecastApiImpl implements FlowAnalyseStockForecastApi {

    @Resource
    private FlowAnalyseGoodsService            flowAnalyseGoodsService;
    @Resource
    private FlowAnalyseDayStockQuantityService flowAnalyseDayStockQuantityService;
    @Resource
    private FlowAnalyseDaySaleQuantityService  flowAnalyseDaySaleQuantityService;
    @DubboReference
    private OrderApi                           orderApi;
    @DubboReference
    private PopGoodsApi                        popGoodsApi;
    @Resource
    private CrmEnterpriseService crmEnterpriseService;
    @Resource
    private CrmSupplierService crmSupplierService;

    @Override
    public StockForecastInfoDTO stockForecastInfoVo(StockForecastInfoRequest request) {
        //替换
        log.info("销售预测查询stockForecastInfoVo:",request);
        List<Long> crmIds = getEidByParmam(request);
        // 参考时间1的详情计算值
        StockReferenceTimeInfoDTO timeInfoDTO1 = buildStockReferenceTimeInfoDTO(1, request,crmIds);
        // 参考时间1的详情计算值
        StockReferenceTimeInfoDTO timeInfoDTO2 = buildStockReferenceTimeInfoDTO(2, request,crmIds);
        List<StockForecastIconDTO> iconDTOList = getIconDtoList(request, timeInfoDTO1, timeInfoDTO2,crmIds);
        // 补货页面数据对象处理
        StockForecastInfoDTO stockForecastInfoDTO = buildStockForecastInfoDTO(timeInfoDTO1, timeInfoDTO2, request);
        stockForecastInfoDTO.setStockForecastIconVoList(iconDTOList);
        //近90天的日均销量
        Long avgSale90=flowAnalyseDaySaleQuantityService.getAvg90SaleQuantity(request,crmIds);
        stockForecastInfoDTO.setReferenceAbleSaleDays90(avgSale90+"");
        // 调用POP的获取近5天的出库记录;
        List<Long> eidList=(crmIds!=null&&crmIds.size()>0)?crmEnterpriseService.listById(crmIds):Lists.newArrayList();
        List<GoodsListItemBO> goodsListItemList=(eidList!=null&&eidList.size()>0)? popGoodsApi.findGoodsBySpecificationIdAndEids(request.getSpecificationId(),eidList):Lists.newArrayList();
        //转化商品ID
        List<Long> goodsIdList=goodsListItemList.stream().filter(goodsItem->goodsItem.getId() !=null).map(GoodsListItemBO::getId).collect(Collectors.toList());
        //根据供应商Eid集合和商品ID集合 查询商家的出库信息
        //if(goodsIdList)
        stockForecastInfoDTO.setDeliveryQuantity(0L);
        if( CollectionUtil.isNotEmpty(goodsIdList)&& goodsIdList.size()>0){
            Boolean deliveryFiveDayTips = orderApi.getDeliveryFiveDayTips(goodsIdList);
            stockForecastInfoDTO.setDeliveryQuantity(deliveryFiveDayTips?1L:0L);
        }
        return stockForecastInfoDTO;
    }

    @Override
    public List<StockForecastSaleIconDTO> getSaleData(StockForecastInfoRequest request) {
        log.info("销售预测查询:",request);
        List<Long> crmIds = getEidByParmam(request);
        // 参考时间的 日均销量
        List<StockForecastSaleIconDTO> saleData = flowAnalyseDaySaleQuantityService.getSaleData(request, crmIds);
        //近90天的日均销量
       Long avg90= flowAnalyseDaySaleQuantityService.getAvg90SaleQuantity(request, crmIds);
       saleData.stream().forEach(s->s.setRedValue(avg90));
        return saleData;
    }

    @Override
    public StockReferenceTimeDTO getStockReferenceTimeInfoByParam(StockReferenceTimeRequest request) {
        Assert.notNull(request.getSpecificationId());
        Assert.notNull(request);
        StockForecastInfoRequest infoRequest= PojoUtils.map(request, StockForecastInfoRequest.class);

        StockReferenceTimeInfoDTO infoDTO=   buildStockReferenceTimeInfoDTOJob(0,infoRequest, Arrays.asList(request.getCrmEnterpriseId()));
        StockReferenceTimeDTO dto=new StockReferenceTimeDTO();
        PojoUtils.map(infoDTO,dto);
        //设置可销售天数
        String referenceAbleSaleDays= calReferenceAbleSaleDays(new BigDecimal(dto.getCurStockQuantity()), new BigDecimal(dto.getAverageDailySales()));
       dto.setReferenceAbleSaleDays(referenceAbleSaleDays);
        return dto;
    }

//    @Override
//    public Long getCurStockQuantity(StockReferenceTimeRequest request) {
//        Assert.notNull(request.getSpecificationId());
//        Assert.notNull(request.getEid());
//        StockForecastInfoRequest infoRequest= PojoUtils.map(request, StockForecastInfoRequest.class);
//        return flowAnalyseDayStockQuantityService.getCurStockQuantity(infoRequest,Arrays.asList(request.getEid()));
//    }

    private List<StockForecastIconDTO> getIconDtoList(StockForecastInfoRequest request, StockReferenceTimeInfoDTO timeInfoDTO1, StockReferenceTimeInfoDTO timeInfoDTO2, List<Long> eidList) {
        //获取近10天的库存数据，时间减1天，不包括当天；
        long start=System.currentTimeMillis();

        log.info("eidList date:{}",(System.currentTimeMillis()-start));
        List<StockForecastIconDTO> near10StockQuantityList = flowAnalyseDayStockQuantityService.getNear10StockQuantity(request,eidList);
        // 初始化未来补货天数天的数据，包含今天数据
        List<StockForecastIconDTO> nexStockQuantityList = initNextStockQuantityList(request, timeInfoDTO1, timeInfoDTO2);
        //list合并按照时间降序
        near10StockQuantityList.addAll(nexStockQuantityList);
        //降序
        near10StockQuantityList.stream().sorted(Comparator.comparing(StockForecastIconDTO::getPointTime).reversed());

        return near10StockQuantityList;
    }

    /**
     * 初始化未来补货天数的对象
     */
    private List<StockForecastIconDTO> initNextStockQuantityList(StockForecastInfoRequest request, StockReferenceTimeInfoDTO timeInfoDTO1, StockReferenceTimeInfoDTO timeInfoDTO2) {
        // 当前库存
       // log.info("request:{};timeInfoDTO1:{};timeInfoDTO2:{}", request, timeInfoDTO1, timeInfoDTO2);
        Long curStock = timeInfoDTO1.getCurStockQuantity();
        List<StockForecastIconDTO> next15StockQuantityList = new ArrayList<>();
        //天数
        long nextDay = 1L;
        for (int i = 0; i < request.getReplenishDays(); i++, nextDay++) {
            StockForecastIconDTO dto = new StockForecastIconDTO();
            //DateUtil.
            Date now = DateUtil.beginOfDay(new Date());
            Date next = DateUtil.offsetDay(now, i);
            //时间戳
            dto.setPointTime(next);
            dto.setSpecificationId(Long.valueOf(request.getSpecificationId()));
            dto.setNearStockQuantity(0l);
            //预测库存1
            dto.setStockForecastQuantity1(calNextStockQuantity(curStock, (timeInfoDTO1.getAverageDailySales() * nextDay)));
            //预测库存1
            dto.setStockForecastQuantity2(calNextStockQuantity(curStock, (timeInfoDTO2.getAverageDailySales() * nextDay)));
            //补货数量1
            long rep1 = calReplenishStockQuantity(timeInfoDTO1.getAverageDailySales(), nextDay, curStock, timeInfoDTO1.getSafeStockQuantity());
            long rep2 = calReplenishStockQuantity(timeInfoDTO2.getAverageDailySales(), nextDay, curStock, timeInfoDTO2.getSafeStockQuantity());
            dto.setReplenish1(rep1 <= 0 ? 0l : rep1);
            //补货数量2
            dto.setReplenish2(rep2 < 0L ? 0L : rep2);
            next15StockQuantityList.add(dto);
        }
        return next15StockQuantityList;
    }

    //  0处理判断
    private static Long calNextStockQuantity(long curStock, Long nextAvgSale) {
        if (curStock - nextAvgSale > 0l) {
            return curStock - nextAvgSale;
        }
        return 0L;
    }

    private StockForecastInfoDTO buildStockForecastInfoDTO(StockReferenceTimeInfoDTO timeInfoDTO1, StockReferenceTimeInfoDTO timeInfoDTO2, StockForecastInfoRequest request) {

        StockForecastInfoDTO stockForecastInfoDTO = new StockForecastInfoDTO();
        String referenceAbleSaleDays1 = calReferenceAbleSaleDays(new BigDecimal(timeInfoDTO1.getCurStockQuantity()), new BigDecimal(timeInfoDTO1.getAverageDailySales()));
        String referenceAbleSaleDays2 = calReferenceAbleSaleDays(new BigDecimal(timeInfoDTO2.getCurStockQuantity()), new BigDecimal(timeInfoDTO2.getAverageDailySales()));
        //特殊情况处理，如果查询和计算结果都0的情况下
        if(timeInfoDTO1.getAverageDailySales().longValue()==0L&&timeInfoDTO1.getSafeStockQuantity().longValue()==0L&&timeInfoDTO2.getAverageDailySales().longValue()==0L&&timeInfoDTO2.getSafeStockQuantity().longValue()==0L){
            stockForecastInfoDTO.setStatus(FlowAnalyseStockStatusEnum.NO_SALE.getCode());
        }else {
            stockForecastInfoDTO.setStatus(calStockStatus(Double.valueOf(referenceAbleSaleDays1), Double.valueOf(referenceAbleSaleDays2)));//计算库存状态
        }
        //参考时间1的详情计算结果
        stockForecastInfoDTO.setReferenceTimeInfoVo1(timeInfoDTO1);
        //参考时间2的详情计算结果
        stockForecastInfoDTO.setReferenceTimeInfoVo2(timeInfoDTO2);
        //补货天数从查询条件获取
        stockForecastInfoDTO.setReplenishDays(request.getReplenishDays());
        // 参考时间1的可销售天数
        stockForecastInfoDTO.setReferenceAbleSaleDays1(referenceAbleSaleDays1);
        // 参考时间2的可销售天数
        stockForecastInfoDTO.setReferenceAbleSaleDays2(referenceAbleSaleDays2);
        //支撑X天销售的总数量上下限 A*X 和 B*X
        //补货建议补货量上限
        Long rep1= timeInfoDTO1.getReplenishStockQuantity()<=0?0l:timeInfoDTO1.getReplenishStockQuantity();
        Long rep2= timeInfoDTO2.getReplenishStockQuantity()<=0?0l:timeInfoDTO2.getReplenishStockQuantity();
        stockForecastInfoDTO.setReplenishQuantityMax(Math.max(rep1,rep2));
        //补货建议补货量下限DIH-75-Q-1
        stockForecastInfoDTO.setReplenishQuantityMin(Math.min(rep1,rep2));

        //图标信息封装
        return stockForecastInfoDTO;
    }

    /**
     * 详情时间内Ojbect
     */
    private StockReferenceTimeInfoDTO buildStockReferenceTimeInfoDTO(Integer referenceTimeFlag, StockForecastInfoRequest request, List<Long> crmIds) {
        StockForecastInfoRequest referenceTimeRequest = setReferenceTimeFiledValue(referenceTimeFlag, request);
        //根据查询条件查询时间段内分析详情
        //查询某个时间内的每天的库存和销量;
        long start=System.currentTimeMillis();

        List<PeriodDaySaleQuantityBO> saleQuantityList = flowAnalyseDaySaleQuantityService.getPeriodDaySaleQuantity(referenceTimeRequest,crmIds);
        log.info("saleQuantityList date:{}",(System.currentTimeMillis()-start));
        //建议安全库存-D 要把这一天减掉，应该是库存数量之和/59 ，去掉布0数据
        Long safeStockQuantity = flowAnalyseDayStockQuantityService.safeStockQuantity(referenceTimeRequest,crmIds);
        log.info("safeStockQuantity date:{}",(System.currentTimeMillis()-start));
        safeStockQuantity = (safeStockQuantity == null || safeStockQuantity == 0l) ? 0L : safeStockQuantity;
        //日均销量-A
        LongSummaryStatistics longSummaryStatistics = saleQuantityList.stream().mapToLong((bo) -> bo.getSoQuantity()).summaryStatistics();
        //强制转化丢弃小数点
        Long averageDailySales = Math.max(0L, (long) longSummaryStatistics.getAverage());
        //补货天数X
        Long replenishDays = referenceTimeRequest.getReplenishDays();
        //当前库存数量-C 获取前一天的数据，如果没有默认0;
        Long curStockQuantity = flowAnalyseDayStockQuantityService.getCurStockQuantity(request,crmIds);
        log.info("curStockQuantity date:{}",(System.currentTimeMillis()-start));
        curStockQuantity = curStockQuantity == null ? 0L : curStockQuantity;
        //建议补货量
        // 计算公式:A*X-(C-D) a-日均销量，X补货天数，C当前库存数量，D-
        Long replenishStockQuantity = calReplenishStockQuantity(averageDailySales, replenishDays, curStockQuantity, safeStockQuantity);


        StockReferenceTimeInfoDTO timeInfoDTO = new StockReferenceTimeInfoDTO();
        //补货天数
        timeInfoDTO.setReferenceTime(referenceTimeRequest.getReferenceTime());
        // 当前库存数量
        timeInfoDTO.setCurStockQuantity(curStockQuantity);
        //建议安全库存
        timeInfoDTO.setSafeStockQuantity(safeStockQuantity);
        timeInfoDTO.setReplenishDays(referenceTimeRequest.getReplenishDays());
        //日均销量
        timeInfoDTO.setAverageDailySales(averageDailySales);
        // 建议补货量
        timeInfoDTO.setReplenishStockQuantity(replenishStockQuantity);
        referenceTimeRequest = null;
        log.info("end date:{}",(System.currentTimeMillis()-start));
        return timeInfoDTO;
    }

    /**
     * 设置参数时间属性重制对象
     */
    private StockForecastInfoRequest setReferenceTimeFiledValue(Integer referenceTimeFlag, StockForecastInfoRequest request) {
        StockForecastInfoRequest referenceTimeRequest = PojoUtils.map(request, StockForecastInfoRequest.class);
        //默认使用referenceTime此时间
        if (referenceTimeFlag == 1) {
            referenceTimeRequest.setReferenceTime(request.getReferenceTime1());
        } else if(referenceTimeFlag == 2) {
            referenceTimeRequest.setReferenceTime(request.getReferenceTime2());
        }

        return referenceTimeRequest;
    }

    /**
     * 可销售天数计算
     */
    private String calReferenceAbleSaleDays(BigDecimal c, BigDecimal a) {
        //RoundingMode.DOWN:直接省略多余的小数
        return a.compareTo(BigDecimal.ZERO) == 0 ? "0.0" : (c.divide(a, 1, RoundingMode.DOWN).toString());
    }

    /**
     * 计算公式:A*X-(C-D) a-日均销量，X补货天数，C当前库存数量，D-
     *
     * @return
     */
    private static Long calReplenishStockQuantity(Long a, Long x, Long c, Long d) {
        return (a * x) - (c - d);
    }

    /**
     * 计算补货建议补货量上限和下限公式 日均销量 *补货天数 X
     */
    private Long calReplenishQuantityAB(long ab, long x) {
        return ab * x;
    }

    /**
     * 计算库存状态
     * （1）如果有任何一个值小于等于7，则库存状态为紧张。红色字体展示。
     * （2）如果有任何一个值大于等于60天，则库存状态为充裕。 绿色字体展示。
     * （3）其他情况下，库存状态为正常。 蓝色字体展示
     *
     * @return
     */
    private static Integer calStockStatus(double a, double b) {
        //按照套件优先级顺序执行
        if (a <= 7 || b <= 7) {
            return FlowAnalyseStockStatusEnum.NERVOUS.getCode();
        } else if (a >= 60 || b >= 60) {
            return FlowAnalyseStockStatusEnum.AMPLE.getCode();
        } else {
            return FlowAnalyseStockStatusEnum.NORMAL.getCode();
        }
    }
    private StockReferenceTimeInfoDTO buildStockReferenceTimeInfoDTOJob(Integer referenceTimeFlag, StockForecastInfoRequest request, List<Long> crmIds) {
        StockForecastInfoRequest referenceTimeRequest = setReferenceTimeFiledValue(referenceTimeFlag, request);
        //根据查询条件查询时间段内分析详情
        //查询某个时间内的每天的库存和销量;
        long start=System.currentTimeMillis();
        //日均销量-A
        Long averageDailySales =Math.max(0L, request.getAleSaleDays());;
        //建议安全库存-D 要把这一天减掉，应该是库存数量之和/59 ，去掉布0数据
        Long safeStockQuantity = request.getSafeStockQuantity();
        safeStockQuantity = (safeStockQuantity == null || safeStockQuantity == 0l) ? 0L : safeStockQuantity;
        //补货天数X
        Long replenishDays = referenceTimeRequest.getReplenishDays();
        //当前库存数量-C 获取前一天的数据，如果没有默认0;
        Long curStockQuantity = request.getCurStockQuantity();
        log.info("curStockQuantity date:{}",(System.currentTimeMillis()-start));
        curStockQuantity = curStockQuantity == null ? 0L : curStockQuantity;
        //建议补货量
        // 计算公式:A*X-(C-D) a-日均销量，X补货天数，C当前库存数量，D-
        Long replenishStockQuantity = calReplenishStockQuantity(averageDailySales, replenishDays, curStockQuantity, safeStockQuantity);


        StockReferenceTimeInfoDTO timeInfoDTO = new StockReferenceTimeInfoDTO();
        //补货天数
        timeInfoDTO.setReferenceTime(referenceTimeRequest.getReferenceTime());
        // 当前库存数量
        timeInfoDTO.setCurStockQuantity(curStockQuantity);
        //建议安全库存
        timeInfoDTO.setSafeStockQuantity(safeStockQuantity);
        timeInfoDTO.setReplenishDays(referenceTimeRequest.getReplenishDays());
        //日均销量
        timeInfoDTO.setAverageDailySales(averageDailySales);
        // 建议补货量
        timeInfoDTO.setReplenishStockQuantity(replenishStockQuantity);
        referenceTimeRequest = null;
        log.info("end date:{}",(System.currentTimeMillis()-start));
        return timeInfoDTO;
    }
    private List<Long> getEidByParmam(StockForecastInfoRequest request){
        List<Long> list=new ArrayList<>();
        log.info("getEidByParmam.request->{}",request);
        List<Long> listByLevelAndGroupEidList= ListUtil.empty();

        if((Objects.nonNull(request.getBusinessSystem())||Objects.nonNull(request.getSupplierLevel()))){
            List<Long> crmEnterIds = crmSupplierService.listByLevelAndGroup(request.getSupplierLevel(), request.getBusinessSystem());
            //在部分权限的时候获取2个list的交集
            List<CrmEnterpriseDTO> crmEnterpriseListById = crmEnterpriseService.getCrmEnterpriseListById(crmEnterIds);
            listByLevelAndGroupEidList =crmEnterpriseListById.stream().filter(m->m.getId()>0).map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
            listByLevelAndGroupEidList.add(-1L);
        }
        log.info("listByLevelAndGroupEidList->{},eid->{}",listByLevelAndGroupEidList,request.getCrmEnterpriseId());
        if(Objects.nonNull(request.getCrmEnterpriseId())&& request.getCrmEnterpriseId().intValue()>0&&CollUtil.isEmpty(listByLevelAndGroupEidList)||(CollUtil.isNotEmpty(listByLevelAndGroupEidList)&&listByLevelAndGroupEidList.contains(request.getCrmEnterpriseId()))){
            list.add(request.getCrmEnterpriseId());
        }
        if(Objects.nonNull(request.getCrmEnterpriseId())&& request.getCrmEnterpriseId().intValue()>0&&(CollUtil.isNotEmpty(listByLevelAndGroupEidList)&&!listByLevelAndGroupEidList.contains(request.getCrmEnterpriseId()))){
            list.add(-1L);
        }
        if(Objects.isNull(request.getCrmEnterpriseId())&&CollUtil.isNotEmpty(listByLevelAndGroupEidList)){
            list.addAll(listByLevelAndGroupEidList);
        }

        //permit
        if(OrgDatascopeEnum.PORTION==OrgDatascopeEnum.getFromCode(request.getSjmsUserDatascopeBO().getOrgDatascope())){
            QueryDataScopeRequest request1=new QueryDataScopeRequest();
            request1.setSjmsUserDatascopeBO(request.getSjmsUserDatascopeBO());
            List<CrmEnterpriseDTO>  permitCrmEnterList= crmEnterpriseService.getCrmEnterpriseListByDataScope(request1);
            List<Long> eIdsPermitList= Optional.ofNullable(permitCrmEnterList.stream().filter(m->m.getId()>0).map(CrmEnterpriseDTO::getId).collect(Collectors.toList())).orElse(Lists.newArrayList());
            List<Long> intersection=eIdsPermitList.stream().filter(item->list.contains(item)).collect(Collectors.toList());
            //本身查新条件查询的eid时空的情况下
            if(!list.isEmpty()){
                if(Objects.nonNull(request.getBusinessSystem())||Objects.nonNull(request.getSupplierLevel())){
                    intersection.add(-1L);
                }
                if(intersection.isEmpty()){
                    intersection.add(-1L);
                }
                return  intersection;
            }
           return  eIdsPermitList;
        }
        return list;
    }
    public static void main(String[] args) {
        System.out.println(CollectionUtil.isNotEmpty(Lists.newArrayList()));
    }

}
