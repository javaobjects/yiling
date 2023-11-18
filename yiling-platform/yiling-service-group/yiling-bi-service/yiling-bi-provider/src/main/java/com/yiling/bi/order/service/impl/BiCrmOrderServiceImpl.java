package com.yiling.bi.order.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.bi.goods.service.InputGoodsRelationShipService;
import com.yiling.bi.order.bo.DwsStjxInventoryBO;
import com.yiling.bi.order.bo.OdsStjxcJddyhoutBO;
import com.yiling.bi.order.dao.BiCrmOrderMapper;
import com.yiling.bi.order.entity.BiCrmOrderDO;
import com.yiling.bi.order.entity.BiOrderBackupDO;
import com.yiling.bi.order.entity.BiOrderBackupTaskDO;
import com.yiling.bi.order.excel.BiCrmOrderExcel;
import com.yiling.bi.order.service.BiCrmOrderService;
import com.yiling.bi.order.service.BiOrderBackupService;
import com.yiling.bi.order.service.BiOrderBackupTaskService;
import com.yiling.bi.order.service.DwsStjxcService;
import com.yiling.bi.order.service.OdsStjxcJddyhoutService;
import com.yiling.bi.order.utils.CollectionUtils;
import com.yiling.bi.order.utils.CombinationUtil;
import com.yiling.bi.order.utils.Levenshtein;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/9/21
 */
@Service
@Slf4j
public class BiCrmOrderServiceImpl extends BaseServiceImpl<BiCrmOrderMapper, BiCrmOrderDO> implements BiCrmOrderService {

    @Autowired
    private DwsStjxcService dwsStjxcService;

    @Autowired
    private OdsStjxcJddyhoutService odsStjxcJddyhoutService;

    @Autowired
    private BiOrderBackupService biOrderBackupService;

    @Autowired
    private BiOrderBackupTaskService biOrderBackupTaskService;

    @Autowired
    private InputGoodsRelationShipService inputGoodsRelationShipService;

    private static final List<String> CSV_PROPERTIES = Arrays.asList(
            "soTime", "soYear", "soMonth", "sellerEcode", "sellerEname", "sellerEid", "buyerEname", "terminalArea", "terminalDept", "businessType", "satrapNo", "satrapName", "representativeNo", "representativeName",
            "chainRole", "province", "city", "county", "variety", "goodsId", "goodsName", "goodsSpec", "goodsPrice", "quantity", "salesVolume", "spec", "batchNo", "flowType", "lockFlag", "remark"
    );


    @Override
    public void handleFtpCrmOrderData() {
        //  连接ftp
        Ftp ftp = getConnectFtp();

        //  读取并将文件写入本地
        List<File> fileList = null;
        try {
            fileList = generateFileList(ftp);
        } catch (Exception e) {
            log.error("[BI-CRM流向数据入库] 文件下载失败！");
            e.printStackTrace();
        }
        try {
            if (!CollUtil.isEmpty(fileList)) {    // ftp文件处理
                fileParsing(ftp, fileList);
            }
        } catch (Exception e) {
            log.error("[BI-CRM流向数据入库] 文件解析失败！");
            e.printStackTrace();
        } finally {
            // 关闭ftp连接
            try {
                ftp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<BiCrmOrderDO> biCrmOrderDOList = getNoMatchedBiCrmOrder();
        if (CollUtil.isEmpty(biCrmOrderDOList)) {
            return;
        }

        //  比对bi_order_backup的数据
        // 获取商品映射表
        List<InputGoodsRelationShipDTO> inputGoodsRelationShipDTOList = inputGoodsRelationShipService.getInputGoodsRelationShipAll();

        //  crm订单按照月份分组
        Map<String, List<BiCrmOrderDO>> crmMonthListMap = biCrmOrderDOList.stream().collect(Collectors.groupingBy(BiCrmOrderDO::getSoMonth, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<String, List<BiCrmOrderDO>> entry : crmMonthListMap.entrySet()) {
            String monthStr = entry.getKey();
            List<BiCrmOrderDO> crmMonthList = entry.getValue();

            List<BiOrderBackupDO> noMatchedB2bList = getB2BNoMatchedList(monthStr, inputGoodsRelationShipDTOList);

            //  匹配crm订单和b2b订单
            List<BiOrderBackupDO> updateB2BOrderList = new ArrayList<>();
            matchCiOrderAndCrmOrder(updateB2BOrderList, noMatchedB2bList, crmMonthList, inputGoodsRelationShipDTOList);

            // 匹配库存
            matchCrmInventory(monthStr, crmMonthList);
            // 入库
            saveOrUpdateBatch(crmMonthList);
            biOrderBackupService.saveOrUpdateBatch(updateB2BOrderList);
        }

    }

    @Override
    public List<BiCrmOrderDO> getNoMatchedBiCrmOrder() {
        List<BiCrmOrderDO> result = new ArrayList<>();

        LambdaQueryWrapper<BiCrmOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BiCrmOrderDO::getMatchFlag, 0);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            return result;
        }

        long current = 1L;
        long size = 1000L;
        while ((current - 1) * size < count) {
            Page<BiCrmOrderDO> page = new Page<>(current, size);
            Page<BiCrmOrderDO> pageResult = baseMapper.selectPage(page, wrapper);
            result.addAll(pageResult.getRecords());
            current ++;
        }

        return result;
    }

    @Transactional
    @Override
    public void batchInsert(List<BiCrmOrderDO> biCrmOrderDOList) {
        List<List<BiCrmOrderDO>> lists = CollectionUtils.subList(biCrmOrderDOList);
        for (List<BiCrmOrderDO> list : lists) {
            baseMapper.batchInsert(list);
        }
    }

    private void fileParsing(Ftp ftp, List<File> fileList) {
        //  解析文件
        Map<File, List<BiCrmOrderExcel>> fileDataMap = new LinkedHashMap<>();
        for (File file : fileList) {
            List<BiCrmOrderExcel> biCrmOrderExcelList = new ArrayList<>();

            if (file.getName().endsWith(".csv")) {      //  csv文件解析
                handleCsvFile(file, biCrmOrderExcelList);
            }

            if (file.getName().endsWith(".xlsx")) {     //  excel文件解析
                handleExcelFile(file, biCrmOrderExcelList);
            }

            fileDataMap.put(file, biCrmOrderExcelList);
        }

        //  数据入库
        for (Map.Entry<File, List<BiCrmOrderExcel>> entry : fileDataMap.entrySet()) {
            File file = entry.getKey();
            List<BiCrmOrderExcel> biCrmOrderExcelList = entry.getValue();

            // 数据转换
            List<BiCrmOrderDO> biCrmOrderDOList = new ArrayList<>();

            try {
                for (BiCrmOrderExcel biCrmOrderExcel : biCrmOrderExcelList) {
                    BiCrmOrderDO biCrmOrderDO = covertExcelDataToBiCrmOrderDO(biCrmOrderExcel);
                    biCrmOrderDOList.add(biCrmOrderDO);
                }
            } catch (Exception e) {
                //  删除本地文件
                if (file.exists()) {
                    file.delete();
                }
                log.error("[BI-CRM流向数据入库] 解析文件 " + file.getAbsolutePath() + "/" + file.getName() + " 数据存在格式问题！");
                e.printStackTrace();
                continue;
            }

            // 插入
            batchInsert(biCrmOrderDOList);

            //  将ftp文件移至backup文件夹中
            if (!ftp.exist("bakup")) {
                ftp.mkdir("bakup");
            }
            ftp.upload("/bakup", file);
            ftp.cd("/");
            ftp.delFile(file.getName());

            //  删除本地文件
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private List<BiOrderBackupDO> getB2BNoMatchedList(String monthStr, List<InputGoodsRelationShipDTO> inputGoodsRelationShipDTOList) {
        List<Long> b2bGoodsIdList = new ArrayList<>();
        //  获取未匹配的crm订单的商品id列表
        List<Long> crmGoodsIdList = baseMapper.getNoMatchedGoodsIdList();
        for (InputGoodsRelationShipDTO i : inputGoodsRelationShipDTOList) {
            if (StringUtils.isEmpty(i.getCrmGoodsid()) || StringUtils.isEmpty(i.getB2bGoodsid())) {
                continue;
            }
            if (!Levenshtein.isNumeric(i.getCrmGoodsid()) || !Levenshtein.isNumeric(i.getB2bGoodsid())) {
                continue;
            }
            if (crmGoodsIdList.contains(Long.parseLong(i.getCrmGoodsid()))) {
                b2bGoodsIdList.add(Long.parseLong(i.getB2bGoodsid()));
            }
        }

        //  查询b2b订单 monthStr当月或以前月份的所有未匹配的数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date monthDate = null;
        try {
            monthDate = sdf.parse(monthStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date date = DateUtil.offsetMonth(monthDate, 1);

        List<BiOrderBackupDO> biOrderBackupDOList = biOrderBackupService.getNoMatchedBiOrderBackup(date, b2bGoodsIdList);
        return biOrderBackupDOList;
    }

    private void matchCiOrderAndCrmOrder(List<BiOrderBackupDO> updateB2BOrderList, List<BiOrderBackupDO> noMatchedB2bList, List<BiCrmOrderDO> biCrmOrderDOList, List<InputGoodsRelationShipDTO> inputGoodsRelationShipDTOList) {

//        List<Long> b2bGoodsIdList = new ArrayList<>();
//        //  获取未匹配的crm订单的商品id列表
//        List<Long> crmGoodsIdList = baseMapper.getNoMatchedGoodsIdList();
//        for (InputGoodsRelationShipDTO i : inputGoodsRelationShipDTOList) {
//            if (StringUtils.isEmpty(i.getCrmGoodsid()) || StringUtils.isEmpty(i.getB2bGoodsid())) {
//                continue;
//            }
//            if (!Levenshtein.isNumeric(i.getCrmGoodsid()) || !Levenshtein.isNumeric(i.getB2bGoodsid())) {
//                continue;
//            }
//            if (crmGoodsIdList.contains(Long.parseLong(i.getCrmGoodsid()))) {
//                b2bGoodsIdList.add(Long.parseLong(i.getB2bGoodsid()));
//            }
//        }
//
//        //  查询b2b所有未匹配的数据
//        List<BiOrderBackupDO> biOrderBackupDOList = biOrderBackupService.getNoMatchedBiOrderBackup(b2bGoodsIdList);

        //  按 买家_卖家 分组
        Map<String, List<BiCrmOrderDO>> biCrmOrderMap = biCrmOrderDOList.stream().collect(Collectors.groupingBy(b -> b.getSellerEname().trim() + "_" + b.getBuyerEname().trim()));
        Map<String, List<BiOrderBackupDO>> biOrderBackupMap = noMatchedB2bList.stream().collect(Collectors.groupingBy(b -> b.getSellerEname().trim() + "_" + b.getBuyerEname().trim()));

        //  获取映射表
        Map<String, String> goodsIdMap = inputGoodsRelationShipDTOList.stream().collect(Collectors.toMap(InputGoodsRelationShipDTO::getCrmGoodsid, InputGoodsRelationShipDTO::getB2bGoodsid, (a, b) -> a));

        for (Map.Entry<String, List<BiCrmOrderDO>> entry : biCrmOrderMap.entrySet()) {
            // 以crm数据为主，匹配b2b订单数据
            String key = entry.getKey();
            List<BiCrmOrderDO> biCrmOrderList = entry.getValue();

            if (!biOrderBackupMap.containsKey(key)) {       // 买家和买家的信息在b2b订单上无匹配
                biCrmOrderList.forEach(b -> {
                    b.setMatchFlag(1);  // 已匹配
                    b.setSalesChannel(1);   // 自建渠道
                });
                continue;
            }

            List<BiOrderBackupDO> biOrderBackupList = biOrderBackupMap.get(key);
            List<Long> matchedB2BIdList = new ArrayList<>();    // 已经匹配过的b2bId
            for (BiCrmOrderDO b : biCrmOrderList) {
//                // 查询crm销售日期以前的b2b订单
//                biOrderBackupList = biOrderBackupList.stream().filter(b2bOrder -> !b2bOrder.getOrderTime().after(b.getSoTime())).collect(Collectors.toList());
//                if (CollUtil.isEmpty(biOrderBackupList)) {  // 没有符合条件的订单
//                    b.setMatchFlag(1);  // 已匹配
//                    b.setSalesChannel(1);   // 自建渠道
//                    continue;
//                }

                if (!goodsIdMap.containsKey(String.valueOf(b.getGoodsId()))
                        || Long.parseLong(goodsIdMap.get(String.valueOf(b.getGoodsId()))) == 0) {      // 映射表没有映射b2b商品id
                    b.setMatchFlag(1);  // 已匹配
                    b.setSalesChannel(1);   // 自建渠道
                    continue;
                }
                // 映射b2b平台的goods_id
                Long b2bGoodsId = Long.parseLong(goodsIdMap.get(String.valueOf(b.getGoodsId())));
                List<BiOrderBackupDO> b2bGoodsOrderList = biOrderBackupList.stream().filter(o -> o.getGoodsId().equals(b2bGoodsId)).collect(Collectors.toList());
                if (CollUtil.isEmpty(b2bGoodsOrderList)) {  // b2b订单中无映射的商品
                    b.setMatchFlag(1);  // 已匹配
                    b.setSalesChannel(1);   // 自建渠道
                    continue;
                }

                // 1对1 以及 1对多匹配

                // 获取所有排列组合
                boolean isMatch = false;
                Map<String, Integer> combinationMap = generateCombinationMap(b2bGoodsOrderList);
                outer : for (Map.Entry<String, Integer> e : combinationMap.entrySet()) {   // 对比排列组合的数量
                    String ids = e.getKey();
                    Integer quantitySum  = e.getValue();

                    //  判断数量
                    String[] b2bIds = ids.split(",");
                    if (b.getQuantity().equals(quantitySum)) {
                        for (String b2bIdStr : b2bIds) {
                            Long b2bId = Long.parseLong(b2bIdStr);
                            //  对比b2bId是否已匹配，若已匹配，则跳过该组合
                            if (matchedB2BIdList.stream().anyMatch(id -> id.equals(b2bId))) {
                                continue outer;
                            }
                        }

                        //  添加b2b order信息 至 修改列表
                        for (BiOrderBackupDO order : b2bGoodsOrderList) {
                            if (Arrays.stream(b2bIds).noneMatch(id -> id.equals(String.valueOf(order.getId())))) {
                                continue;
                            }
                            order.setMatchCrmId(String.valueOf(b.getId()));
                            order.setMatchFlag(1);
                            updateB2BOrderList.add(order);
                            matchedB2BIdList.add(order.getId());
                        }

                        b.setMatchFlag(1);  // 已匹配
                        b.setSalesChannel(0);   // 大运河渠道
                        if (b2bIds.length == 1) {
                            b.setMatchType(0); // 1对1
                        } else {
                            b.setMatchType(1); // 1对多
                        }
                        //  crm匹配上b2b的订单号
                        StringBuilder sb = new StringBuilder();
                        for (String b2bId : b2bIds) {
                            String orderNo = b2bGoodsOrderList.stream()
                                    .filter(bo -> bo.getId().equals(Long.parseLong(b2bId)))
                                    .map(BiOrderBackupDO::getOrderNo).findAny().orElse(null);
                            sb.append(orderNo).append(",");
                        }

                        b.setOrderNo(sb.substring(0, sb.lastIndexOf(",")));
                        isMatch = true;
                        break;
                    }
                }

                if (!isMatch) {
                    b.setMatchFlag(1);  // 已匹配
                    b.setSalesChannel(1);   // 自建渠道
                }
            }
        }

    }


    private void matchCrmInventory(String monthStr, List<BiCrmOrderDO> updateCrmOrderList) {
//        //  根据月份分组
//        Map<String, List<BiCrmOrderDO>> map = updateCrmOrderList.stream().collect(Collectors.groupingBy(BiCrmOrderDO::getSoMonth));
//
//        for (Map.Entry<String, List<BiCrmOrderDO>> entry : map.entrySet()) {

//            String monthStr = entry.getKey();
//            List<BiCrmOrderDO> biCrmOrderDOList = entry.getValue();

        // 根据销售渠道排序 大运河在前，自建渠道在后
        updateCrmOrderList.sort(Comparator.comparing(BiCrmOrderDO::getSalesChannel));

        //  先获取库存数据, 取上个月的期末库存
        Date monthDate = DateUtil.parse(monthStr, "yyyy-MM");
        Date lastMonth = DateUtil.offsetMonth(monthDate, -1);
        String dyear = String.valueOf(DateUtil.year(lastMonth));
        String dmonth = String.valueOf(DateUtil.month(lastMonth) + 1);
        List<DwsStjxInventoryBO> dwsStjxInventoryBOList = dwsStjxcService.getDwsStjxInventoryList(dyear, dmonth);
        Map<String, BigDecimal> b2bInventoryMap = dwsStjxInventoryBOList.stream().collect(Collectors.toMap(d -> d.getCustomer() + "_" + d.getCrmGoodsid(), DwsStjxInventoryBO::getB2bInventory, (a, b) -> a));
        Map<String, BigDecimal> jdInventoryMap = dwsStjxInventoryBOList.stream().collect(Collectors.toMap(d -> d.getCustomer() + "_" + d.getCrmGoodsid(), DwsStjxInventoryBO::getJdInventory, (a, b) -> a));

        //  取当期的购进数据
        List<OdsStjxcJddyhoutBO> odsStjxcJddyhoutBOList = odsStjxcJddyhoutService.getByYearAndMonth(dyear, dmonth);
        for (OdsStjxcJddyhoutBO odsStjxcJddyhoutBO : odsStjxcJddyhoutBOList) {

            String key = odsStjxcJddyhoutBO.getCustomer() + "_" + odsStjxcJddyhoutBO.getCrmGoodsid();
            if ("dyhout".equals(odsStjxcJddyhoutBO.getDataSource())) {
                // 当期购进数量
                BigDecimal total = odsStjxcJddyhoutBO.getWlNum();
                if (b2bInventoryMap.containsKey(key)) {
                    BigDecimal inventory = b2bInventoryMap.get(key);
                    total = inventory.add(total);
                }
                b2bInventoryMap.put(key, total);
            }
            if ("jdout".equals(odsStjxcJddyhoutBO.getDataSource())) {
                // 当期购进数量
                BigDecimal total = odsStjxcJddyhoutBO.getWlNum();
                if (jdInventoryMap.containsKey(key)) {
                    BigDecimal inventory = jdInventoryMap.get(key);
                    total = inventory.add(total);
                }
                jdInventoryMap.put(key, total);
            }
        }


        //  将负的销售数量先加到库存中
        for (BiCrmOrderDO crmOrderDO : updateCrmOrderList) {
            if (crmOrderDO.getQuantity() >= 0) {
                continue;
            }
            String key = crmOrderDO.getSellerEname() + "_" + crmOrderDO.getGoodsId();

            if (b2bInventoryMap.containsKey(key) && b2bInventoryMap.get(key).compareTo(BigDecimal.valueOf(0)) > 0) {
                b2bInventoryMap.put(key, b2bInventoryMap.get(key).subtract(BigDecimal.valueOf(crmOrderDO.getQuantity())));
                crmOrderDO.setEffectiveFlag(0);     // 有效
                crmOrderDO.setPurchaseChannel(0);   // 大运河购进渠道
                continue;
            }
            if (jdInventoryMap.containsKey(key) && jdInventoryMap.get(key).compareTo(BigDecimal.valueOf(0)) > 0) {
                jdInventoryMap.put(key, jdInventoryMap.get(key).subtract(BigDecimal.valueOf(crmOrderDO.getQuantity())));
                crmOrderDO.setEffectiveFlag(0);     // 有效
                crmOrderDO.setPurchaseChannel(1);   // 京东购进渠道
                continue;
            }
            b2bInventoryMap.put(key, BigDecimal.valueOf(-crmOrderDO.getQuantity()));
            crmOrderDO.setEffectiveFlag(0);     // 有效
            crmOrderDO.setPurchaseChannel(0);   // 大运河购进渠道
        }

        //  遍历updateCrmOrderList，匹配库存
        List<BiCrmOrderDO> systemCrmOrderList = new ArrayList<>();
        for (BiCrmOrderDO crmOrderDO : updateCrmOrderList) {
            if (crmOrderDO.getQuantity() < 0) {
                continue;
            }

            String key = crmOrderDO.getSellerEname() + "_" + crmOrderDO.getGoodsId();

            //  取大运河和京东的库存数量
            int b2bInventory = 0;
            if (b2bInventoryMap.get(key) != null) {
                b2bInventory = Math.max(b2bInventoryMap.get(key).intValue(), 0);
            }

            int jdInventory = 0;
            if (jdInventoryMap.get(key) != null) {
                jdInventory = Math.max(jdInventoryMap.get(key).intValue(), 0);
            }

            //  如果库存不足，则直接标记为库存不足
            if (b2bInventory == 0 && jdInventory == 0) {
                // 标记库存不足
                crmOrderDO.setOrderType(0);     // 原始单据
                crmOrderDO.setEffectiveFlag(1);     // 库存不足
                continue;
            }

            //  先扣减大运河
            if (b2bInventory > 0) {     // 大运河库存大于0
                int b2bRemainInventory = b2bInventory - crmOrderDO.getQuantity();      // 剩余的库存
                if (b2bRemainInventory >= 0) {
                    crmOrderDO.setOrderType(0);     // 原始单据
                    crmOrderDO.setEffectiveFlag(0);     // 有效
                    crmOrderDO.setPurchaseChannel(0);   // 大运河购进
                    b2bInventoryMap.put(key, BigDecimal.valueOf(b2bRemainInventory));  // 大运河扣减
                    continue;
                }

                crmOrderDO.setOrderType(0);     // 原始单据
                crmOrderDO.setEffectiveFlag(0);     // 有效
                crmOrderDO.setPurchaseChannel(0);   // 大运河购进

                // 系统生成一条为负数的对冲订单
                BiCrmOrderDO crmOrder = generateNewCrmOrder(crmOrderDO, 0, b2bRemainInventory,  0);
                systemCrmOrderList.add(crmOrder);

                // 大运河扣减至0
                b2bInventoryMap.put(key, BigDecimal.valueOf(0));

                //  再扣减京东库存
                if (jdInventory <= 0) {    // 若京东库存已无，则系统直接生成库存不足单据
                    BiCrmOrderDO noInventoryOrder = generateNoInventoryOrder(crmOrderDO, -b2bRemainInventory);
                    systemCrmOrderList.add(noInventoryOrder);
                    continue;
                }

                int jbRemainInventory = jdInventory - (-b2bRemainInventory);
                if (jbRemainInventory >= 0) {
                    // 系统生成一条的京东的扣减订单
                    BiCrmOrderDO jdCrmOrder = generateNewCrmOrder(crmOrderDO, 1,  -b2bRemainInventory,  0);
                    systemCrmOrderList.add(jdCrmOrder);

                    // 京东扣减
                    jdInventoryMap.put(key, BigDecimal.valueOf(jbRemainInventory));
                } else {
                    // 生成一条京东的扣减订单
                    BiCrmOrderDO jdCrmOrder = generateNewCrmOrder(crmOrderDO, 1,  jdInventory,  0);
                    systemCrmOrderList.add(jdCrmOrder);

                    // 生成库存不足订单
                    BiCrmOrderDO noInventoryOrder = generateNoInventoryOrder(crmOrderDO, -jbRemainInventory);
                    systemCrmOrderList.add(noInventoryOrder);

                    // 京东扣减至0
                    jdInventoryMap.put(key, BigDecimal.valueOf(0));
                }

            } else {
                int jbRemainInventory = jdInventory - crmOrderDO.getQuantity();
                if (jbRemainInventory >= 0) {
                    // 系统生成一条的京东的扣减订单
                    crmOrderDO.setOrderType(0);     // 原始单据
                    crmOrderDO.setEffectiveFlag(0);     // 有效
                    crmOrderDO.setPurchaseChannel(1);   // 京东购进
                    jdInventoryMap.put(key, BigDecimal.valueOf(jbRemainInventory));  // 京东扣减

                } else {
                    crmOrderDO.setOrderType(0);     // 原始单据
                    crmOrderDO.setEffectiveFlag(0);     // 有效
                    crmOrderDO.setPurchaseChannel(1);   // 京东购进

                    // 生成一条京东的对冲订单
                    BiCrmOrderDO jdCounterOrder = generateNewCrmOrder(crmOrderDO, 1,  jbRemainInventory,  0);
                    systemCrmOrderList.add(jdCounterOrder);

                    // 生成一条库存不足的订单
                    BiCrmOrderDO noInventoryOrder = generateNoInventoryOrder(crmOrderDO, -jbRemainInventory);
                    systemCrmOrderList.add(noInventoryOrder);

                    // 京东扣减至0
                    jdInventoryMap.put(key, BigDecimal.valueOf(0));
                }
            }

        }
        updateCrmOrderList.addAll(systemCrmOrderList);
    }

    private BiCrmOrderDO generateNoInventoryOrder(BiCrmOrderDO crmOrderDO, Integer quantity) {
        return generateNewCrmOrder(crmOrderDO, null, quantity, 1);
    }

    private BiCrmOrderDO generateNewCrmOrder(BiCrmOrderDO crmOrderDO, Integer purchaseChannel, Integer quantity, Integer effectiveFlag) {
        BiCrmOrderDO newOrder = new BiCrmOrderDO();
        PojoUtils.map(crmOrderDO, newOrder);
        newOrder.setId(null);
        newOrder.setPurchaseChannel(purchaseChannel);
        newOrder.setQuantity(quantity);
        newOrder.setOrderType(1);   // 系统生成
        newOrder.setEffectiveFlag(effectiveFlag);   // 0-有效， 1-库存不足
        return newOrder;
    }


    private Map<String, Integer> generateCombinationMap(List<BiOrderBackupDO> biOrderBackupList) {
        Map<String, Integer> resultMap = new LinkedHashMap<>();

        List<CombinationUtil.Combination> targetList = biOrderBackupList.stream().map(bo -> {
            CombinationUtil.Combination combination = new CombinationUtil.Combination();
            combination.setId(bo.getId());
            combination.setQuantity(bo.getDeliveryQuantity() - bo.getReturnQuantity());     // 订单数量为 发货数量-退货数量
            return combination;
        }).collect(Collectors.toList());

        // 排列组合最大为5个
        for (int i = 1; i <= Math.min(targetList.size(), 5); i++) {
            CombinationUtil util = new CombinationUtil();
            util.combination(targetList, 0,  i);
            resultMap.putAll(util.getResultMap());
        }

        return resultMap;
    }


    private String upperCaseFirst(String val) {
        char[] arr = val.toCharArray();
        arr[0] = Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    private Ftp getConnectFtp() {
        FtpConfig ftpConfig = new FtpConfig();
        ftpConfig.setUser("yiling_Finance");
        ftpConfig.setPassword("yiling_Finance3A21");

        ftpConfig.setHost("8.142.87.52");
        ftpConfig.setPort(12121);

        ftpConfig.setSoTimeout(1000 * 60 * 60);
        ftpConfig.setConnectionTimeout(1000 * 60 * 60);
        ftpConfig.setCharset(Charset.forName("GBK"));
        return new Ftp(ftpConfig, FtpMode.Passive);
    }

    private List<File> generateFileList(Ftp ftp) {
        List<File> fileList = new ArrayList<>();

        File fileDir =  FileUtil.getTmpDir();
        if (!fileDir.isDirectory()) {
            fileDir.mkdirs();
        }

        FTPFile[] ftpFiles = ftp.lsFiles("/");
        for (FTPFile ftpFile : ftpFiles) {
            String ftpFileName = ftpFile.getName();

            if (!ftpFile.isFile()) {
                continue;
            }
            if (!ftpFileName.endsWith(".xlsx") && !ftpFile.getName().endsWith(".csv")) {
                continue;
            }
            //  获取文件名中的时间信息
            if (!ftpFileName.contains("-")) {
                continue;
            }
            String monthStr = ftpFileName.substring(0, ftpFileName.indexOf("-"));

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//
//            Date monthDate = null;
//            try {
//                monthDate = sdf.parse(monthStr);
//            } catch (ParseException e) {    // 文件格式有误
//                continue;
//            }

            Date monthDate = DateUtil.parse(monthStr, "yyyyMM");
            List<BiOrderBackupTaskDO> biOrderBackupTaskDOList = biOrderBackupTaskService.getTaskByMonth(DateUtil.format(monthDate, "yyyy-MM"));
            if (CollUtil.isEmpty(biOrderBackupTaskDOList) || biOrderBackupTaskDOList.stream().noneMatch(b -> b.getBackupStatus() == 2)) {
                // 如果该月份的b2b订单数据还未备份，则不做处理
                continue;
            }


            File file = FileUtil.newFile(fileDir.getPath() + File.separator + ftpFile.getName());
            //  download至本地
            if (file.exists()) {
                FileUtil.del(file);
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            ftp.download("/", ftpFile.getName(), file);
            fileList.add(file);
        }
        return fileList;
    }

    private void handleCsvFile(File file, List<BiCrmOrderExcel> biCrmOrderExcelList) {
        Reader reader = null;
        CSVReader csvReader = null;

        try {
            reader = new InputStreamReader(Files.newInputStream(file.toPath()), "GBK");
            csvReader = new CSVReaderBuilder(reader).build();

            Iterator<String[]> iterator = csvReader.iterator();
            boolean isTitleLine = true;
            while (iterator.hasNext()) {
                String[] valueArr = iterator.next();
                if (isTitleLine) {
                    isTitleLine = false;
                    continue;
                }

                BiCrmOrderExcel biCrmOrderExcel = covertFlowCsvData(valueArr);
                biCrmOrderExcelList.add(biCrmOrderExcel);
            }
        } catch (Exception e) {
            log.error("[BI-CRM流向数据入库] 解析csv文件 " + file.getName() + " 失败！");
            e.printStackTrace();
        } finally {
            //  关闭流
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleExcelFile(File file, List<BiCrmOrderExcel> biCrmOrderExcelList) {
        InputStream is = null;
        try {
            is = Files.newInputStream(file.toPath());
            ImportParams params = new ImportParams();
            params.setNeedVerify(true);
            params.setHeadRows(1);
//            params.setKeyIndex(0);
            ExcelImportResult<BiCrmOrderExcel> result = new ExcelImportService().importExcelByIs(is, BiCrmOrderExcel.class, params, true);
            if (CollUtil.isNotEmpty(result.getFailList())) {
                log.warn("[BI-CRM流向数据入库]文件 " + file.getName() + " 解析失败条数：" + result.getFailList().size());
            }
            if (CollUtil.isNotEmpty(result.getList())) {
                biCrmOrderExcelList.addAll(result.getList());
            }
        } catch (Exception e) {
            log.error("[BI-CRM流向数据入库] 解析excel文件 " + file.getName() + " 失败！");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private BiCrmOrderExcel covertFlowCsvData(String[] valueArr) {
        Class<BiCrmOrderExcel> clazz = BiCrmOrderExcel.class;
        Method[] methods = clazz.getDeclaredMethods();

        BiCrmOrderExcel biCrmOrderExcel = null;
        try {
            biCrmOrderExcel = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int index = 0;
        for (String property : CSV_PROPERTIES) {
            String methodName = "set" + upperCaseFirst(property);
            if (Arrays.stream(methods).noneMatch(m -> m.getName().equals(methodName))) {
                continue;
            }
            Method method = null;
            try {
                method = clazz.getMethod(methodName, String.class);
                method.invoke(biCrmOrderExcel, valueArr.length > index ? valueArr[index] : "");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            index++;
        }
        return biCrmOrderExcel;
    }

    private String excelDoubleToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        if (strDate.length() == 5) {
            try {
                Date tDate = doubleToDate(Double.parseDouble(strDate));
                return DateUtil.format(tDate, "yyyy-MM-dd");
            } catch (Exception e) {
                return strDate;
            }
        }
        return strDate;
    }

    private Date doubleToDate(Double dVal) {
        Date tDate = new Date();
        long localOffset = tDate.getTimezoneOffset() * 60000; //系统时区偏移 1900/1/1 到 1970/1/1 的 25569 天
        tDate.setTime((long) ((dVal - 25569) * 24 * 3600 * 1000 + localOffset));
        return tDate;
    }

    private BiCrmOrderDO covertExcelDataToBiCrmOrderDO(BiCrmOrderExcel biCrmOrderExcel) {
        BiCrmOrderDO biCrmOrderDO = new BiCrmOrderDO();
        // 设置时间
        String soDate = biCrmOrderExcel.getSoTime().replaceAll("/", "-");
        biCrmOrderDO.setSoTime(DateUtil.parse(excelDoubleToDate(soDate)));

        // 设置年份
        String year = biCrmOrderExcel.getSoYear();
        if (year.contains("年")) {
            year = year.trim().replace("年", "");
        }
        biCrmOrderDO.setSoYear(Integer.parseInt(year));

        // 设置月份
        String month = biCrmOrderExcel.getSoMonth();
        if (month.contains("月")) {
            month = month.trim().replace("月", "");
        }
        LocalDate localDate = LocalDate.of(biCrmOrderDO.getSoYear(), Integer.parseInt(month), 1);
        ZoneId zoneId = ZoneId.systemDefault();
        Date date = Date.from(localDate.atStartOfDay().atZone(zoneId).toInstant());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        biCrmOrderDO.setSoMonth(sdf.format(date));

        biCrmOrderDO.setSellerEcode(biCrmOrderExcel.getSellerEcode());
        biCrmOrderDO.setSellerEid(Long.parseLong(biCrmOrderExcel.getSellerEid()));
        biCrmOrderDO.setSellerEname(biCrmOrderExcel.getSellerEname() != null ? biCrmOrderExcel.getSellerEname().trim() : "");
        biCrmOrderDO.setBuyerEname(biCrmOrderExcel.getBuyerEname() != null ? biCrmOrderExcel.getBuyerEname().trim() : "");
        biCrmOrderDO.setTerminalArea(biCrmOrderExcel.getTerminalArea());
        biCrmOrderDO.setTerminalDept(biCrmOrderExcel.getTerminalDept());
        //                biCrmOrderDO.setBusinessType();
        biCrmOrderDO.setSatrapNo(biCrmOrderExcel.getSatrapNo());
        biCrmOrderDO.setSatrapName(biCrmOrderExcel.getSatrapName());
        biCrmOrderDO.setRepresentativeNo(biCrmOrderExcel.getRepresentativeNo());
        biCrmOrderDO.setRepresentativeName(biCrmOrderExcel.getRepresentativeName());
        biCrmOrderDO.setChainRole(biCrmOrderExcel.getChainRole());
        biCrmOrderDO.setProvince(biCrmOrderExcel.getProvince());
        biCrmOrderDO.setCity(biCrmOrderExcel.getCity());
        biCrmOrderDO.setCounty(biCrmOrderExcel.getCounty());
        biCrmOrderDO.setVariety(biCrmOrderExcel.getVariety());
        biCrmOrderDO.setGoodsId(Long.parseLong(biCrmOrderExcel.getGoodsId()));
        biCrmOrderDO.setGoodsName(biCrmOrderExcel.getGoodsName());
        biCrmOrderDO.setGoodsSpec(biCrmOrderExcel.getGoodsSpec());

        String goodsPrice = StringUtils.isEmpty(biCrmOrderExcel.getGoodsPrice()) || "-".equals(biCrmOrderExcel.getGoodsPrice().trim()) ? "0" : biCrmOrderExcel.getGoodsPrice();
        String quantity = StringUtils.isEmpty(biCrmOrderExcel.getQuantity()) || "-".equals(biCrmOrderExcel.getQuantity().trim()) ? "0" : biCrmOrderExcel.getQuantity();
        String salesVolume = StringUtils.isEmpty(biCrmOrderExcel.getSalesVolume()) || "-".equals(biCrmOrderExcel.getSalesVolume().trim()) ? "0" : biCrmOrderExcel.getSalesVolume();
        biCrmOrderDO.setGoodsPrice(new BigDecimal(goodsPrice.replace(",", "")));
        biCrmOrderDO.setQuantity(Integer.parseInt(quantity.replace(",", "")));
        biCrmOrderDO.setSalesVolume(new BigDecimal(salesVolume.replace(",", "")));
        biCrmOrderDO.setSpec(biCrmOrderExcel.getSpec());
        biCrmOrderDO.setBatchNo(biCrmOrderExcel.getBatchNo());
        biCrmOrderDO.setFlowType(biCrmOrderExcel.getFlowType());
        biCrmOrderDO.setLockFlag(biCrmOrderExcel.getLockFlag());
        biCrmOrderDO.setRemark(biCrmOrderExcel.getRemark());
        biCrmOrderDO.setOrderType(0);   // 原始单据

        return biCrmOrderDO;
    }

    private String getPreMonthStr() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, -1);
        Date preMonthDate = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(preMonthDate);
    }


}
