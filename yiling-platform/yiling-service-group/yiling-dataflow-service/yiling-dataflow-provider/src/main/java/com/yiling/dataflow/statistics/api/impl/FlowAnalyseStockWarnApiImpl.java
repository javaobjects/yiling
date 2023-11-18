package com.yiling.dataflow.statistics.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.enums.CrmSupplierLevelEnum;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.statistics.api.FlowAnalyseStockWarnApi;
import com.yiling.dataflow.statistics.dto.FlowAnalyseGoodsDTO;
import com.yiling.dataflow.statistics.dto.StockForecastIconDTO;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.StockWarnIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.dataflow.statistics.enums.FlowAnalyseStockStatusEnum;
import com.yiling.dataflow.statistics.service.FlowAnalyseCalculationResultService;
import com.yiling.dataflow.statistics.service.FlowAnalyseGoodsService;
import com.yiling.framework.common.log.Log;
import org.apache.dubbo.config.annotation.DubboService;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@DubboService
public class FlowAnalyseStockWarnApiImpl implements FlowAnalyseStockWarnApi {
    @Resource
    private FlowAnalyseCalculationResultService flowAnalyseCalculationResultService;
    @Resource
    private FlowAnalyseGoodsService flowAnalyseGoodsService;
    @Resource
    private CrmEnterpriseService crmEnterpriseService;
    @Resource
    private CrmSupplierService crmSupplierService;

    @Override
    public Page<StockWarnDTO> getStockWarnPage(StockWarnPageRequest request) {
        // 查询eid，ename,商品基础数据，根据查询条件
        Page<StockWarnDTO> stockWarnDTOPage = flowAnalyseCalculationResultService.getStockWarnPage(request);
       // request.setEidsList(null);
        request.setCrmIdList(null);
        if(CollUtil.isEmpty(stockWarnDTOPage.getRecords())){
            return stockWarnDTOPage;
        }
        List<Long> crmIds = stockWarnDTOPage.getRecords().stream().distinct().filter(m -> Objects.nonNull(m.getCrmEnterpriseId())).map(StockWarnDTO::getCrmEnterpriseId).collect(Collectors.toList());
       // request.setEidsList(eids);
        request.setCrmIdList(crmIds);
        List<String> specificationIds = stockWarnDTOPage.getRecords().stream().distinct().filter(m -> Objects.nonNull(m.getSpecificationId())).map(StockWarnDTO::getSpecificationId).collect(Collectors.toList());
        //补充3天和30的数据 和库存数量
        List<StockWarnDTO> nearStockWarnDtoList = CollUtil.isNotEmpty(stockWarnDTOPage.getRecords())?flowAnalyseCalculationResultService.getSaleDaysIconWarnList(request, specificationIds): ListUtil.empty();
        Map<String, StockWarnDTO> nearStockWarnDtoMap = nearStockWarnDtoList.stream().collect(Collectors.toMap(this::converMapKey, m -> m, (a, b) -> a));
        //查询企业名称，商品名称
        List<FlowAnalyseGoodsDTO> flowAnalyseGoodsDTOS =CollUtil.isNotEmpty(specificationIds)?flowAnalyseGoodsService.getGoodsNameBySpecificationIds(specificationIds):ListUtil.empty();
        Map<String, FlowAnalyseGoodsDTO> flowAnalyseGoodsDTOMap = flowAnalyseGoodsDTOS.stream().collect(Collectors.toMap(FlowAnalyseGoodsDTO::getSpecificationId, m -> m, (a, b) -> a));
        List<CrmEnterpriseDTO> crmEnterpriseDTOS = crmEnterpriseService.listByIdsAndName(crmIds,null);
        //经销商等级
        List<Long> crmEnterIds = crmEnterpriseDTOS.stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
        List<CrmSupplierDTO> supplierInfoByCrmEnterId = crmSupplierService.getSupplierInfoByCrmEnterId(crmEnterIds);
        //扩展信息
        Map<Long, CrmSupplierDTO> crmSupplierDTOMap = supplierInfoByCrmEnterId.stream().collect(Collectors.toMap(CrmSupplierDTO::getCrmEnterpriseId, m -> m, (a, b) -> a));
        //基本信息转化
        Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = crmEnterpriseDTOS.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, m -> m, (a, b) -> a));
        //处理数据
        stockWarnDTOPage.getRecords().stream().forEach(m3 -> {
            //供应商名称
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(m3.getCrmEnterpriseId());
            m3.setEname(Objects.nonNull(crmEnterpriseDTO) ? crmEnterpriseDTO.getName() : m3.getCrmEnterpriseId() + "");
            //经销商等级
            CrmSupplierDTO crmSupplierDTO = Objects.nonNull(crmEnterpriseDTO) ? crmSupplierDTOMap.get(crmEnterpriseDTO.getId()) : null;
            m3.setEnameLevel(CrmSupplierLevelEnum.getNameByCode(Objects.nonNull(crmSupplierDTO) ? crmSupplierDTO.getSupplierLevel() : null));
            FlowAnalyseGoodsDTO flowAnalyseGoodsDTO = flowAnalyseGoodsDTOMap.get(m3.getSpecificationId());
            m3.setGoodsName(Objects.nonNull(flowAnalyseGoodsDTO) ? flowAnalyseGoodsDTO.getGoodsName() : m3.getSpecificationId());
            //商品名称
            String near3Key = String.format("%s-%s-%s", m3.getCrmEnterpriseId(), m3.getSpecificationId(), 3);
            String near30Key = String.format("%s-%s-%s", m3.getCrmEnterpriseId(), m3.getSpecificationId(), 30);
            StockWarnDTO dto3 = nearStockWarnDtoMap.get(near3Key)==null?defaultStockWarnDTO():nearStockWarnDtoMap.get(near3Key);
            StockWarnDTO dto30 = nearStockWarnDtoMap.get(near30Key)==null?defaultStockWarnDTO():nearStockWarnDtoMap.get(near3Key);
            //可销售天数
            toStockWarnDTOFiled(m3, dto3, dto30);
        });

        //合并近30天的数据并

        return stockWarnDTOPage;
    }

    public void toStockWarnDTOFiled(StockWarnDTO m3, StockWarnDTO dto3, StockWarnDTO dto30) {
        m3.setNear3AbleSaleDays(dto3.getSupportDay());
        m3.setNear30AbleSaleDays(dto30.getSupportDay());
        //日均销量
        m3.setNear3AverageDailySales(dto3.getSaleAvg());
        m3.setNear30AverageDailySales(dto30.getSaleAvg());
        //前一天的库存
        m3.setStockQuantity(dto3.getStockQuantity());
        //0问题处理
        m3.setStatus(calStockStatus(dto3.getSupportDay().doubleValue(), dto30.getSupportDay().doubleValue()));
        long rep1 = dto3.getSupplementQuantity() <= 0 ? 0 : dto3.getSupplementQuantity();
        long rep2 = dto30.getSupplementQuantity() <= 0 ? 0 : dto30.getSupplementQuantity();
        m3.setReplenishQuantityMax(Math.max(rep1, rep2));
        m3.setReplenishQuantityMin(Math.min(rep1, rep2));
    }

    public String converMapKey(StockWarnDTO m) {
        return String.format("%s-%s-%s", m.getCrmEnterpriseId(), m.getSpecificationId(), m.getDay()).toString();
    }

    @Override
    public Page<StockWarnIconDTO> getSaleDaysIconWarn(StockWarnIconRequest request) {
        //
        Page<StockWarnIconDTO> dtoPage = flowAnalyseCalculationResultService.getSaleDaysIconWarn(request);
        if(CollUtil.isEmpty(dtoPage.getRecords())){
            return dtoPage;
        }
        //request.setEidsList(null);
        request.setCrmIdList(null);
        List<Long> crmIds = dtoPage.getRecords().stream().filter(m -> Objects.nonNull(m.getCrmEnterpriseId())).map(StockWarnIconDTO::getCrmEnterpriseId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
//        request.setEidsList(eids);
        request.setCrmIdList(crmIds);
        List<String> specificationIds = dtoPage.getRecords().stream().filter(m -> Objects.nonNull(m.getSpecificationId())).map(StockWarnIconDTO::getSpecificationId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        //查询企业名称，商品名称
        List<FlowAnalyseGoodsDTO> flowAnalyseGoodsDTOS = flowAnalyseGoodsService.getGoodsNameBySpecificationIds(specificationIds);
        Map<String, FlowAnalyseGoodsDTO> flowAnalyseGoodsDTOMap = flowAnalyseGoodsDTOS.stream().collect(Collectors.toMap(FlowAnalyseGoodsDTO::getSpecificationId, m -> m, (a, b) -> a));
        List<CrmEnterpriseDTO> crmEnterpriseDTOS = crmEnterpriseService.listByIdsAndName(crmIds,null);
        //经销商等级
        List<Long> crmEnterIds = crmEnterpriseDTOS.stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
        List<CrmSupplierDTO> supplierInfoByCrmEnterId = crmSupplierService.getSupplierInfoByCrmEnterId(crmEnterIds);
        //扩展信息
        Map<Long, CrmSupplierDTO> crmSupplierDTOMap = supplierInfoByCrmEnterId.stream().collect(Collectors.toMap(CrmSupplierDTO::getCrmEnterpriseId, m -> m, (a, b) -> a));
        Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = crmEnterpriseDTOS.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, m -> m, (a, b) -> a));
        dtoPage.getRecords().stream().forEach(m3 -> {
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(m3.getCrmEnterpriseId());
            m3.setEname(Objects.nonNull(crmEnterpriseDTO) ? crmEnterpriseDTO.getName() : m3.getCrmEnterpriseId() + "");
            //经销商等级
            CrmSupplierDTO crmSupplierDTO = Objects.nonNull(crmEnterpriseDTO) ? crmSupplierDTOMap.get(crmEnterpriseDTO.getId()) : null;
            m3.setEnameLevel(CrmSupplierLevelEnum.getNameByCode(Objects.nonNull(crmSupplierDTO) ? crmSupplierDTO.getSupplierLevel() : null));
            FlowAnalyseGoodsDTO flowAnalyseGoodsDTO = flowAnalyseGoodsDTOMap.get(m3.getSpecificationId());
            m3.setGoodsName(Objects.nonNull(flowAnalyseGoodsDTO) ? flowAnalyseGoodsDTO.getGoodsName() : m3.getSpecificationId());

        });
        return dtoPage;
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
    private Integer calStockStatus(double a, double b) {
        if (a <= 7 || b <= 7) {
            return FlowAnalyseStockStatusEnum.NERVOUS.getCode();
        } else if (a >= 60 || b >= 60) {
            return FlowAnalyseStockStatusEnum.AMPLE.getCode();
        } else {
            return FlowAnalyseStockStatusEnum.NORMAL.getCode();
        }
    }

    public static void main(String[] args) {
        List<StockForecastIconDTO> next15StockQuantityList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StockForecastIconDTO dto = new StockForecastIconDTO();
            //DateUtil.
            Date now = DateUtil.beginOfDay(new Date());
            Date next = DateUtil.offsetDay(now, i);
            System.out.println(next);
            //时间戳
            //  dto.setPointTime(next.getTime());
        }
    }
    private StockWarnDTO defaultStockWarnDTO(){
        return  new StockWarnDTO().setSupportDay(BigDecimal.ZERO).setSaleAvg(0L).setStockQuantity(0L).setSupplementQuantity(0L);
    }

}
