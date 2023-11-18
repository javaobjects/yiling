package com.yiling.dataflow.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.bi.goods.api.InputGoodsRelationShipApi;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.flow.dto.request.FlowMonthBiTaskRequest;
import com.yiling.dataflow.flow.dto.request.UpdateFlowPurchaseSalesInventoryRequest;
import com.yiling.dataflow.flow.entity.FlowMonthBiTaskDO;
import com.yiling.dataflow.flow.service.FlowMonthBiTaskService;
import com.yiling.dataflow.flow.service.FlowPurchaseSalesInventoryService;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchDetailListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDetailDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowGoodsBatchDetailService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.statistics.dto.ErpClientDataDTO;
import com.yiling.dataflow.statistics.service.FlowStatisticsJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/2/24
 */
@Slf4j
public class FlowReportTest extends BaseTest {

    @Autowired
    private FlowStatisticsJobService          flowStatisticsJobService;
    @Autowired
    private FlowMonthBiTaskService            flowMonthBiTaskService;
    @Autowired
    private FlowPurchaseService               flowPurchaseService;
    @Autowired
    private FlowSaleService                   flowSaleService;
    @Autowired
    private FlowGoodsBatchDetailService       flowGoodsBatchDetailService;
    @Autowired
    private FlowPurchaseSalesInventoryService flowPurchaseSalesInventoryService;
    @DubboReference
    private InputGoodsRelationShipApi         inputGoodsRelationShipApi;


    @Test
    public void Test1() throws Exception {
        // 1270
        List<ErpClientDataDTO> erpClientDataDTOList = new ArrayList<>();
        ErpClientDataDTO erpClientDataDTO = new ErpClientDataDTO();
        erpClientDataDTO.setSuId(1270L);
        erpClientDataDTO.setRkSuId(1270L);
        erpClientDataDTOList.add(erpClientDataDTO);
        flowStatisticsJobService.statisticsFlowJob(erpClientDataDTOList);
    }

    @Test
    public void Test3() throws Exception {
        UpdateFlowPurchaseSalesInventoryRequest request = new UpdateFlowPurchaseSalesInventoryRequest();
//        Date date = DateUtil.offsetDay(DateUtil.parseDate(DateUtil.today()), -1);
        request.setDateTime(new Date());
        request.setEids(Arrays.asList(164L,
                27L,
                4687L,
                18L,
                49L,
                154L,
                50L,
                163L,
                7504L,
                10472L,
                11531L,
                22L,
                11539L,
                1246L,
                1039L,
                1104L,
                192L,
                1331L,
                1680L,
                650L,
                123L,
                148387L,
                613L,
                178283L,
                178282L,
                965L,
                284L,
                178391L,
                349L,
                435L,
                974L,
                765L,
                680L,
                178471L,
                917L,
                617L,
                596L,
                452L,
                676L,
                1255L,
                157L,
                473L,
                1252L,
                776L,
                281L,
                969L,
                1245L,
                158123L,
                468L,
                1229L,
                1232L,
                285L,
                851L,
                1226L,
                60959L,
                602L,
                278L,
                445L,
                691L,
                672L,
                1236L,
                1241L,
                178780L,
                178787L,
                84541L,
                178766L,
                178791L,
                178790L,
                178779L,
                178773L,
                178776L,
                178786L,
                178783L,
                178764L,
                178777L,
                178782L,
                178788L,
                178784L,
                178785L,
                178781L,
                178774L,
                178767L,
                178769L,
                178772L,
                1244L,
                178768L,
                178771L,
                178765L,
                178789L,
                1240L,
                1242L,
                178775L,
                178770L,
                178778L,
                179065L,
                179066L,
                683L,
                179230L,
                179231L,
                179232L,
                179233L,
                179234L,
                179235L,
                179236L,
                179237L,
                179238L,
                553L,
                754L,
                629L,
                335L,
                777L,
                179745L,
                183L,
                1224L,
                1258L,
                179435L,
                179800L,
                607L,
                1253L,
                1262L,
                1263L,
                738L,
                1264L,
                1265L,
                1266L,
                737L,
                741L,
                744L,
                1269L,
                1267L,
                740L,
                743L,
                742L,
                739L,
                769L,
                746L,
                745L,
                736L,
                1337L,
                1335L,
                1334L,
                1333L,
                1340L,
                1338L,
                1336L,
                1339L,
                1299L,
                48L,
                673L,
                1225L,
                23218L,
                857L,
                859L,
                866L,
                836L,
                863L,
                76L,
                176212L,
                459L,
                67L,
                462L,
                321L,
                177L,
                766L,
                346L,
                181380L,
                347L,
                181190L,
                749L,
                181191L,
                181192L,
                181201L,
                181193L,
                181194L,
                896L,
                824L,
                181447L,
                181529L,
                202151L,
                926L,
                348L,
                461L,
                1004L,
                962L,
                984L,
                963L,
                1008L,
                992L,
                991L,
                1049L,
                463L,
                344L,
                146015L,
                451L,
                181676L,
                14821L,
                612L,
                17L,
                447L,
                671L,
                793L,
                181849L,
                181851L,
                1048L,
                1249L,
                611L,
                909L,
                1003L,
                209L,
                603L,
                597L,
                189902L,
                778L,
                189973L,
                794L,
                189972L,
                189974L,
                1010L,
                189971L,
                785L,
                1026L,
                1016L,
                19L,
                1204L,
                73L,
                190022L,
                190023L,
                179168L,
                190007L,
                190009L,
                190008L,
                190010L,
                190011L,
                1254L,
                1303L,
                677L,
                181850L,
                215L,
                189815L,
                222L,
                181696L,
                345L,
                191252L,
                164981L,
                595L,
                189991L,
                1247L,
                598L,
                25130L,
                319L,
                871L,
                472L,
                210L,
                191034L,
                774L,
                100L,
                1235L,
                150L,
                182L,
                193816L,
                193817L,
                193818L,
                193819L,
                265L,
                929L,
                25333L,
                601L,
                192879L,
                192870L,
                192871L,
                56588L,
                192872L,
                192873L,
                192874L,
                192880L,
                192875L,
                192876L,
                192877L,
                192881L,
                782L,
                1268L,
                192773L,
                192771L,
                192772L,
                471L,
                192676L,
                192666L,
                479L,
                192667L,
                678L,
                192854L,
                867L,
                192855L,
                192856L,
                408L,
                402L,
                413L,
                429L,
                283L,
                751L,
                753L,
                735L,
                747L,
                748L,
                755L,
                750L,
                192768L,
                192769L,
                192770L,
                768L,
                193115L,
                1041L,
                688L,
                193656L,
                713L,
                715L,
                1123L,
                1324L,
                1321L,
                193114L,
                90L,
                517L,
                193654L,
                193655L,
                193657L,
                1198L,
                287L,
                193391L,
                274L,
                133L,
                148L,
                124L,
                120L,
                45L,
                28L,
                141L,
                1295L,
                1294L,
                1304L,
                332L,
                193659L,
                193660L,
                550L,
                1473L,
                193661L,
                577L,
                552L,
                193663L,
                192858L,
                482L,
                195215L,
                195236L,
                166050L,
                195241L,
                839L,
                38L,
                195237L,
                195238L,
                83988L,
                158601L,
                317L,
                201236L,
                195250L,
                181L,
                202100L,
                202099L,
                202098L,
                202097L,
                202092L,
                202096L,
                202095L,
                202094L,
                202093L,
                202091L,
                202090L,
                202089L,
                964L,
                967L,
                192767L,
                955L,
                990L,
                968L,
                108L,
                202150L,
                202238L,
                202126L,
                372L,
                20L,
                173899L,
                843L,
                838L,
                5566L,
                33558L,
                202160L,
                58L,
                272L,
                4333L,
                83766L,
                36L,
                220710L,
                217995L,
                222714L,
                222726L,
                222731L,
                229L,
                281756L,
                9958L,
                18667L));
        flowPurchaseSalesInventoryService.updateFlowPurchaseSalesInventoryByJob(request);
    }

    @Test
    public void Test2() throws Exception {
        // 查询月流向企业信息
        String dateTime = "2022-08-01 00:00:00";
        FlowMonthBiTaskRequest request = new FlowMonthBiTaskRequest();
        request.setQueryDate(DateUtil.parseDate(DateUtil.formatDate(DateUtil.endOfMonth(DateUtil.parseDate(dateTime)))));
        List<FlowMonthBiTaskDO> flowMonthBiTaskDOList = flowMonthBiTaskService.getAllFlowEidList(request);
        Map<Long, String> enterpriseMap = flowMonthBiTaskDOList.stream().collect(Collectors.toMap(FlowMonthBiTaskDO::getEid, FlowMonthBiTaskDO::getEname));
        List<Long> eidList = new ArrayList<>(enterpriseMap.keySet());
        // 加载所对应的商品信息
        List<InputGoodsRelationShipDTO> inputGoodsRelationShipDTOList = inputGoodsRelationShipApi.getInputGoodsRelationShipAll();
        inputGoodsRelationShipDTOList = inputGoodsRelationShipDTOList.stream().filter(e -> StrUtil.isNotEmpty(e.getB2bSpecid()) && StrUtil.isNotEmpty(e.getCrmGoodsid())).collect(Collectors.toList());
        inputGoodsRelationShipDTOList = inputGoodsRelationShipDTOList.stream().filter(e -> !e.getCrmGoodsid().equals("0") && !e.getB2bSpecid().equals("0")).collect(Collectors.toList());
        Map<String, InputGoodsRelationShipDTO> inputGoodsRelationShipMap = inputGoodsRelationShipDTOList.stream().collect(Collectors.toMap(InputGoodsRelationShipDTO::getB2bSpecid, Function.identity()));
        List<Long> specIdList = inputGoodsRelationShipMap.keySet().stream().map(e -> Long.parseLong(e)).collect(Collectors.toList());
        Map<Long, List<String>> batchMap = new HashMap<>();
        // 加载当月的销售数据
        List<FlowSaleDO> flowSaleDOList = new ArrayList<>();
        {
            QueryFlowPurchaseListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowPurchaseListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(DateUtil.parseDateTime(dateTime));
            queryFlowPurchaseListPageRequest.setEndTime(DateUtil.endOfMonth(DateUtil.parseDateTime(dateTime)));
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(eidList);

            Page<FlowSaleDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowSaleService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                // 取供应商已对接的采购
                if (page.getRecords().size() < size) {
                    break;
                }
                flowSaleDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowSaleDO>> flowSaleDOMap = flowSaleDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getSoBatchNo()));
        Map<String, BigDecimal> totalSoQuantityMap = new HashMap<>();
        for (String key : flowSaleDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowSaleDO> flowPurchaseList = flowSaleDOMap.get(key);
            BigDecimal totalSoQuantity = flowPurchaseList.stream().map(FlowSaleDO::getSoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalSoQuantityMap.put(key, totalSoQuantity);
        }
        // 加载当月的购进数据
        List<FlowPurchaseDO> flowPurchaseDOList = new ArrayList<>();
        {
            QueryFlowPurchaseListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowPurchaseListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(DateUtil.parseDateTime(dateTime));
            queryFlowPurchaseListPageRequest.setEndTime(DateUtil.endOfMonth(DateUtil.parseDateTime(dateTime)));
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(eidList);

            Page<FlowPurchaseDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowPurchaseService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                // 取供应商已对接的采购
                if (page.getRecords().size() < size) {
                    break;
                }
                flowPurchaseDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowPurchaseDO>> flowPurchaseDOMap = flowPurchaseDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getPoBatchNo()));
        Map<String, BigDecimal> totalPoQuantityMap = new HashMap<>();
        for (String key : flowPurchaseDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowPurchaseDO> flowPurchaseList = flowPurchaseDOMap.get(key);
            BigDecimal totalPoQuantity = flowPurchaseList.stream().map(FlowPurchaseDO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalPoQuantityMap.put(key, totalPoQuantity);
        }
        // 加载当月的最后一天库存数据
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailDOList = new ArrayList<>();
        {
            QueryFlowGoodsBatchDetailListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowGoodsBatchDetailListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(DateUtil.beginOfDay(DateUtil.endOfMonth(DateUtil.parseDateTime(dateTime))));
            queryFlowPurchaseListPageRequest.setEndTime(DateUtil.endOfMonth(DateUtil.parseDateTime(dateTime)));
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(eidList);

            Page<FlowGoodsBatchDetailDO> page;
            int current = 1;
            int size = 20000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowGoodsBatchDetailService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                // 取供应商已对接的采购
                if (page.getRecords().size() < size) {
                    break;
                }
                flowGoodsBatchDetailDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowGoodsBatchDetailDO>> flowGoodsBatchDetailDOMap = flowGoodsBatchDetailDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getGbBatchNo()));
        Map<String, BigDecimal> totalGbQuantityMap = new HashMap<>();
        for (String key : flowGoodsBatchDetailDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailList = flowGoodsBatchDetailDOMap.get(key);
            BigDecimal totalGbQuantity = flowGoodsBatchDetailList.stream().map(FlowGoodsBatchDetailDO::getGbNumber).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalGbQuantityMap.put(key, totalGbQuantity);
        }
        // 加载前一个月的最后一天库存数据
        List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailLastDOList = new ArrayList<>();
        {
            Date lastDate = DateUtil.endOfMonth(DateUtil.offset(DateUtil.parseDateTime(dateTime), DateField.MONTH, -1));
            QueryFlowGoodsBatchDetailListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowGoodsBatchDetailListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(DateUtil.beginOfDay(lastDate));
            queryFlowPurchaseListPageRequest.setEndTime(DateUtil.endOfDay(lastDate));
            queryFlowPurchaseListPageRequest.setSpecificationIdFlag(1);
            queryFlowPurchaseListPageRequest.setEidList(eidList);

            Page<FlowGoodsBatchDetailDO> page;
            int current = 1;
            int size = 10000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowGoodsBatchDetailService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                // 取供应商已对接的采购
                if (page.getRecords().size() < size) {
                    break;
                }
                flowGoodsBatchDetailLastDOList.addAll(page.getRecords());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
        Map<String, List<FlowGoodsBatchDetailDO>> flowGoodsBatchDetailLastDOMap = flowGoodsBatchDetailLastDOList.stream().filter(e -> specIdList.contains(e.getSpecificationId())).collect(Collectors.groupingBy(e -> e.getEid() + "-" + e.getSpecificationId() + "-" + e.getGbBatchNo()));
        Map<String, BigDecimal> totalGbLastQuantityMap = new HashMap<>();
        for (String key : flowGoodsBatchDetailLastDOMap.keySet()) {
            putMap(key, batchMap);
            List<FlowGoodsBatchDetailDO> flowGoodsBatchDetailLastList = flowGoodsBatchDetailLastDOMap.get(key);
            BigDecimal totalGbLastQuantity = flowGoodsBatchDetailLastList.stream().map(FlowGoodsBatchDetailDO::getGbNumber).reduce(BigDecimal.ZERO, BigDecimal::add);
            totalGbLastQuantityMap.put(key, totalGbLastQuantity);
        }
        // 按照商业公司商品信息进行组装计算
        List<FlowExcel> flowExcelList = new ArrayList<>();
        {
            for (Long eid : eidList) {
                for (Long specId : specIdList) {
                    List<String> batchList = batchMap.get(specId);
                    for (String batch : batchList) {
                        BigDecimal totalGbLastQuantity = totalGbLastQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                        BigDecimal totalGbQuantity = totalGbQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                        BigDecimal totalPoQuantity = totalPoQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                        BigDecimal totalSoQuantity = totalSoQuantityMap.getOrDefault(eid + "-" + specId + "-" + batch, BigDecimal.ZERO);
                        if (totalGbLastQuantity.compareTo(BigDecimal.ZERO) == 0 && totalGbQuantity.compareTo(BigDecimal.ZERO) == 0 && totalPoQuantity.compareTo(BigDecimal.ZERO) == 0 && totalSoQuantity.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }
                        FlowExcel flowExcel = new FlowExcel();
                        flowExcel.setYear(String.valueOf(DateUtil.year(DateUtil.parseDate(dateTime))));
                        flowExcel.setMonth(String.valueOf(DateUtil.month(DateUtil.parseDate(dateTime)) + 1));
                        flowExcel.setEname(enterpriseMap.get(eid));
                        flowExcel.setLastNumber(totalGbLastQuantity);
                        flowExcel.setPurchaseNumber(totalPoQuantity);
                        flowExcel.setSaleNumber(totalSoQuantity);
                        flowExcel.setNumber(totalGbQuantity);
                        flowExcel.setCalculationNumber(totalGbLastQuantity.add(totalPoQuantity).subtract(totalSoQuantity));
                        flowExcel.setDiffNumber(flowExcel.getCalculationNumber().subtract(totalGbQuantity));
                        flowExcel.setBatchNo(batch);
                        InputGoodsRelationShipDTO inputGoodsRelationShipDTO = inputGoodsRelationShipMap.getOrDefault(String.valueOf(specId), null);
                        if (inputGoodsRelationShipDTO != null) {
                            flowExcel.setBreed(inputGoodsRelationShipDTO.getBreed());
                            flowExcel.setCrmId(inputGoodsRelationShipDTO.getCrmGoodsid());
                            flowExcel.setSxPrice(inputGoodsRelationShipDTO.getSxPrice());
                            flowExcel.setGoodsName(inputGoodsRelationShipDTO.getGoodsName());
                            flowExcel.setLastNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getLastNumber()));
                            flowExcel.setSaleNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getSaleNumber()));
                            flowExcel.setPurchaseNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getPurchaseNumber()));
                            flowExcel.setSaleNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getSaleNumber()));
                            flowExcel.setNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getNumber()));
                            flowExcel.setCalculationNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getCalculationNumber()));
                            flowExcel.setDiffNumberAmount(flowExcel.getSxPrice().multiply(flowExcel.getDiffNumber()));
                        } else {
                            flowExcel.setBreed(String.valueOf(specId));
                            flowExcel.setCrmId(String.valueOf(specId));
                            flowExcel.setGoodsName(String.valueOf(specId));
                        }
                        flowExcelList.add(flowExcel);
                    }
                }
            }
        }
        //生成excel
        ExportParams params = new ExportParams();
        params.setSheetName("8月进销存明细");
        params.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, FlowExcel.class, flowExcelList);
        File file = FileUtil.newFile("D://8月进销存明细.xlsx");
        OutputStream os = new FileOutputStream(file);
        workbook.write(os);
    }

    public void putMap(String key, Map<Long, List<String>> map) {
        String[] batchList = key.split("-");
        String batch = "";
        if (batchList.length > 2) {
            batch = batchList[2];
        }
        Long specId = Long.parseLong(key.split("-")[1]);
        List<String> list = null;
        if (map.containsKey(specId)) {
            list = map.get(specId);
            if (!list.contains(batch)) {
                list.add(batch);
            }
        } else {
            list = new ArrayList<>();
            list.add(batch);
            map.put(specId, list);
        }
    }

    public static void main(String[] args) {
        String key = "950-1104-";
        String[] batchList = key.split("-");
        String batch = "";
        if (batchList.length > 2) {
            batch = batchList[2];
        }
        System.out.println(batch);
    }
}
