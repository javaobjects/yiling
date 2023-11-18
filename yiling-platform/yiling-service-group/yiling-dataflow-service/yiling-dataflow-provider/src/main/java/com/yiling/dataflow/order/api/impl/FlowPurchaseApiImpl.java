package com.yiling.dataflow.order.api.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.dataflow.order.bo.FlowStatisticsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowStatisticesRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.dao.FlowPurchaseMapper;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDetailDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseGoodsDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseMonthDTO;
import com.yiling.dataflow.order.dto.StorageInfoDTO;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseByUnlockRequest;
import com.yiling.dataflow.order.dto.request.DeleteFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseByGoodsInSnRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseExistRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseRequest;
import com.yiling.dataflow.order.dto.request.QueryPurchaseGoodsListRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryTotalPoQuantityRequest;
import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowPurchaseRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseDO;
import com.yiling.dataflow.order.entity.FlowPurchaseDetailDO;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryEnterpriseEnum;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryService;
import com.yiling.dataflow.order.service.FlowPurchaseService;
import com.yiling.dataflow.order.util.FlowCommonUtil;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
@Slf4j
@DubboService
public class FlowPurchaseApiImpl implements FlowPurchaseApi {

    @Autowired
    private FlowPurchaseService flowPurchaseService;
    @Autowired
    FlowPurchaseInventoryService flowPurchaseInventoryService;
    @Autowired
    FlowGoodsRelationService flowGoodsRelationService;
    @Autowired
    FlowPurchaseMapper flowPurchaseMapper;

    public static final int SUB_SIZE = 2000;


    @Override
    public Integer deleteFlowPurchaseByPoIdAndEid(DeleteFlowPurchaseRequest request) {
        return flowPurchaseService.deleteFlowPurchaseByPoIdAndEid(request);
    }

    @Override
    public Integer updateDataTagByIdList(List<Long> idList, Integer dataTag) {
        return flowPurchaseService.updateDataTagByIdList(idList, dataTag);
    }

    @Override
    public Integer deleteByIdList(List<Long> idList) {
        Assert.notEmpty(idList, "参数 idList 不能为空");
        return flowPurchaseService.deleteByIdList(idList);
    }

    @Override
    public List<FlowPurchaseDTO> getFlowPurchaseDTOByPoIdAndEid(QueryFlowPurchaseRequest request) {
        return PojoUtils.map(flowPurchaseService.getFlowPurchaseDTOByPoIdAndEid(request), FlowPurchaseDTO.class);
    }

    @Override
    public Integer updateFlowPurchaseByPoIdAndEid(SaveOrUpdateFlowPurchaseRequest request) {
        return flowPurchaseService.updateFlowPurchaseByPoIdAndEid(request);
    }

    @Override
    public FlowPurchaseDTO insertFlowPurchase(SaveOrUpdateFlowPurchaseRequest request) {
        return PojoUtils.map(flowPurchaseService.insertFlowPurchase(request), FlowPurchaseDTO.class);
    }

    @Override
    public Page<FlowPurchaseDTO> page(QueryFlowPurchaseListPageRequest request) {
        Page<FlowPurchaseDTO> page = PojoUtils.map(flowPurchaseService.page(request), FlowPurchaseDTO.class);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(p -> {
                p.setPoTime(FlowCommonUtil.parseFlowDefaultTime(p.getPoTime()));
                p.setPoProductTime(FlowCommonUtil.parseFlowDefaultTime(p.getPoProductTime()));
                p.setPoEffectiveTime(FlowCommonUtil.parseFlowDefaultTime(p.getPoEffectiveTime()));
            });
        }
        return page;
    }

    @Override
    public List<String> getByEidAndGoodsInSn(QueryFlowPurchaseByGoodsInSnRequest request) {
        return flowPurchaseService.getByEidAndGoodsInSn(request);
    }

    @Override
    public Integer deleteFlowPurchaseBydEidAndPoTime(DeleteFlowPurchaseByUnlockRequest request) {
        return flowPurchaseService.deleteFlowPurchaseBydEidAndPoTime(request);
    }

    @Override
    public Boolean updateFlowPurchaseById(SaveOrUpdateFlowPurchaseRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getId(), "参数 id 不能为空");
        return flowPurchaseService.updateFlowPurchaseById(request);
    }

    @Override
    public Boolean updateFlowPurchaseByIds(List<SaveOrUpdateFlowPurchaseRequest> requestList) {
        return flowPurchaseService.updateFlowPurchaseByIds(requestList);
    }

    @Override
    public List<FlowPurchaseDTO> getFlowPurchaseEnterpriseList(Integer channelId) {
        List<FlowPurchaseDO> flowPurchaseDOList = flowPurchaseService.getFlowPurchaseEnterpriseList(channelId);
        List<FlowPurchaseDTO> flowPurchaseDTOList = new ArrayList<>();
        for (FlowPurchaseDO flowPurchaseDO : flowPurchaseDOList) {
            FlowPurchaseDTO flowPurchaseDTO = new FlowPurchaseDTO();
            flowPurchaseDTO.setEid(flowPurchaseDO.getEid());
            flowPurchaseDTO.setEname(flowPurchaseDO.getEname());
            flowPurchaseDTOList.add(flowPurchaseDTO);
        }
        return flowPurchaseDTOList;
    }

    @Override
    public List<FlowPurchaseDTO> getFlowPurchaseSupplierList(Integer channelId) {
        List<FlowPurchaseDO> flowPurchaseDOList = flowPurchaseService.getFlowPurchaseSupplierList(channelId);
        List<FlowPurchaseDTO> flowPurchaseDTOList = new ArrayList<>();
        for (FlowPurchaseDO flowPurchaseDO : flowPurchaseDOList) {
            FlowPurchaseDTO flowPurchaseDTO = new FlowPurchaseDTO();
            flowPurchaseDTO.setSupplierId(flowPurchaseDO.getSupplierId());
            flowPurchaseDTO.setEnterpriseName(flowPurchaseDO.getEnterpriseName());
            flowPurchaseDTOList.add(flowPurchaseDTO);
        }
        return flowPurchaseDTOList;
    }

    @Override
    public List<FlowPurchaseMonthDTO> getFlowPurchaseMonthList(QueryFlowPurchaseListRequest request) {
        List<Map<String, Object>> mapList = null;

        if (StringUtils.isEmpty(request.getTime())) {
            //  查询数据
            mapList = flowPurchaseService.getFlowPurchaseAllMonthList(request);
        } else {    // 存在时间条件的话，就只取一个月的起始时间
            //  查询数据
            mapList = flowPurchaseService.getFlowPurchaseMonthList(request);
        }
        return covertToFlowPurchaseMonthDTOList(mapList, request);
    }

    @Override
    public List<FlowPurchaseDetailDTO> getFlowPurchaseDetail(Long eid, Long supplierId, String time) {
        List<FlowPurchaseDetailDTO> flowPurchaseDetailList = new ArrayList<>();

        List<FlowPurchaseDetailDO> flowPurchaseDetailDOList = flowPurchaseService.getFlowPurchaseDetail(eid, supplierId, time);
        for (FlowPurchaseDetailDO flowPurchaseDetailDO : flowPurchaseDetailDOList) {
            flowPurchaseDetailList.add(PojoUtils.map(flowPurchaseDetailDO, FlowPurchaseDetailDTO.class));
        }
        return flowPurchaseDetailList;
    }

    @Override
    public List<FlowPurchaseGoodsDTO> getFlowPurchaseGoodsList(QueryPurchaseGoodsListRequest request) {
        List<FlowPurchaseGoodsDTO> flowPurchaseGoodsList = new ArrayList<>();

        List<Map<String, Object>> mapList = flowPurchaseService.getFlowPurchaseGoodsMonthList(request);
        if (mapList.size() == 0) {
            return new ArrayList<>();
        }

        // 纵向（单个月份所有商品 和 合计月份所有商品）统计
        BigDecimal quantitySum = new BigDecimal(0);
        Map<String, BigDecimal> quantityMonthSumMap = new HashMap<>();

        for (Map<String, Object> map : mapList) {
            FlowPurchaseGoodsDTO flowPurchaseGoodsDTO = new FlowPurchaseGoodsDTO();
            flowPurchaseGoodsDTO.setGoodsName((String) map.get("goodsName"));
            flowPurchaseGoodsDTO.setSpecificationId((Long) map.get("specificationId"));
            flowPurchaseGoodsDTO.setSpec((String) map.get("spec"));
            //            flowPurchaseGoodsDTO.setStorageQuantitySum((BigDecimal) map.get("poQuantity"));
            //            flowPurchaseGoodsDTO.setRank(index);

            // 一行的统计数据
            BigDecimal storageQuantitySum = new BigDecimal(0);
            Map<String, StorageInfoDTO> storageInfoMap = new HashMap<>();
            for (String poMonth : request.getPoMonthList()) {
                StorageInfoDTO storageInfoDTO = new StorageInfoDTO();
                storageInfoDTO.setStorageQuantity((BigDecimal) map.get("poQuantity_" + poMonth));
                // 统计一行的数据
                storageQuantitySum = storageQuantitySum.add(storageInfoDTO.getStorageQuantity() != null ? storageInfoDTO.getStorageQuantity() : new BigDecimal(0));

                storageInfoMap.put(poMonth, storageInfoDTO);

                if (!quantityMonthSumMap.containsKey(poMonth)) {
                    quantityMonthSumMap.put(poMonth, new BigDecimal(0));
                }
                BigDecimal quantityMonthSum = quantityMonthSumMap.get(poMonth);
                quantityMonthSum = quantityMonthSum.add(storageInfoDTO.getStorageQuantity() != null ? storageInfoDTO.getStorageQuantity() : new BigDecimal(0));
                quantityMonthSumMap.put(poMonth, quantityMonthSum);
            }
            if (storageQuantitySum.compareTo(new BigDecimal(0)) <= 0) {
                // 若统计的该行入库数据为0，则该行不展示
                continue;
            }

            //  累加一行合计(合计最右侧列的值)
            quantitySum = quantitySum.add(storageQuantitySum);

            flowPurchaseGoodsDTO.setStorageQuantitySum(storageQuantitySum);
            flowPurchaseGoodsDTO.setStorageInfoMap(storageInfoMap);

            flowPurchaseGoodsList.add(flowPurchaseGoodsDTO);
        }

        //  各月份排序并生成排名
        flowPurchaseGoodsList = generateRank(request.getPoMonthList(), flowPurchaseGoodsList);


        // 将纵向统计的数据插入第一行
        FlowPurchaseGoodsDTO flowPurchaseGoodsDTO = new FlowPurchaseGoodsDTO();
        flowPurchaseGoodsDTO.setRank(1);
        flowPurchaseGoodsDTO.setStorageQuantitySum(quantitySum);
        Map<String, StorageInfoDTO> storageInfoMap = new LinkedHashMap<>();
        for (String poMonth : request.getPoMonthList()) {
            StorageInfoDTO storageInfoDTO = new StorageInfoDTO();
            storageInfoDTO.setRank(1);
            storageInfoDTO.setStorageQuantity(quantityMonthSumMap.get(poMonth));
            storageInfoMap.put(poMonth, storageInfoDTO);
        }
        flowPurchaseGoodsDTO.setStorageInfoMap(storageInfoMap);
        flowPurchaseGoodsList.add(0, flowPurchaseGoodsDTO);

        return flowPurchaseGoodsList;
    }

    @Override
    public List<String> getPurchaseGoodsNameList() {
        return flowPurchaseService.getPurchaseGoodsNameList();
    }

    @Override
    public void syncFlowPurchaseSpec() {
        flowPurchaseService.syncFlowPurchaseSpec();
    }

    @Override
    public void statisticsFlowPurchaseTotalQuantity() {
        long start = System.currentTimeMillis();
        log.info("[采购流向采购数量统计]任务执行开始");

        try {
            // 查询以岭品关系
            List<FlowGoodsRelationDO> flowGoodsRelationList = flowGoodsRelationService.statisticsByYlGoodsId();
            if (CollUtil.isEmpty(flowGoodsRelationList)) {
                return;
            }

            Map<Long, List<FlowGoodsRelationDO>> flowGoodsRelationMap = flowGoodsRelationList.stream().filter(o -> ObjectUtil.isNotNull(o.getYlGoodsId()) && 0 != o.getYlGoodsId().intValue() && StrUtil.isNotBlank(o.getGoodsInSn())).collect(Collectors.groupingBy(FlowGoodsRelationDO::getEid));
//            Map<String, FlowGoodsRelationDO> flowGoodsRelationMap = flowGoodsRelationList.stream().filter(O -> ObjectUtil.isNotNull(O.getYlGoodsId()) && 0 != O.getYlGoodsId().intValue() && StrUtil.isNotBlank(O.getGoodsInSn())).collect(Collectors.toMap(o -> o.getEid() + "_" + o.getGoodsInSn().trim(), o -> o, (k1, k2) -> k2));
            for (Long eid : flowGoodsRelationMap.keySet()) {
                List<FlowGoodsRelationDO> flowGoodsRelationDOList = flowGoodsRelationMap.get(eid);
                // 商品内码
                List<List<String>> goodsInSnSubList = new ArrayList<>();
                List<String> goodsInSnList = flowGoodsRelationDOList.stream().map(FlowGoodsRelationDO::getGoodsInSn).distinct().collect(Collectors.toList());
                if(goodsInSnList.size()> 10){
                    goodsInSnSubList = Lists.partition(goodsInSnList, 10);
                } else {
                    goodsInSnSubList.add(goodsInSnList);
                }
                // 以岭品id
                Map<String, FlowGoodsRelationDO> ylGoodsIdMap = flowGoodsRelationDOList.stream().collect(Collectors.toMap(o -> o.getEid() + "_" + o.getGoodsInSn().trim(), o -> o, (k1, k2) -> k2));

                //                FlowGoodsRelationDO flowGoodsRelationDO = flowGoodsRelationMap.get(eidAnGoodsInSn);
//                Long eid = flowGoodsRelationDO.getEid();
//                String goodsInSn = flowGoodsRelationDO.getGoodsInSn().trim();
//                Long ylGoodsId = flowGoodsRelationDO.getYlGoodsId();
                // 待新增/更新采购库存
                Map<String, SaveFlowPurchaseInventoryTotalPoQuantityRequest> totalPoQuantityMap = new HashMap<>();
                // 查询采购
                for (List<String> goodsInSns : goodsInSnSubList) {
                    QueryFlowPurchaseListPageRequest request = new QueryFlowPurchaseListPageRequest();
                    request.setEidList(ListUtil.toList(eid));
                    request.setGoodsInSnList(goodsInSns);
                    // 根据供应商名称筛选，仅统计：河北大运河医药物流有限公司、泰州市京东医药有限责任公司
                    request.setSupplierIdList(FlowPurchaseInventoryEnterpriseEnum.getEnterpriseSupplierIdList());
                    request.setStartTime(DateUtil.parse("2022-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                    Page<FlowPurchaseDO> page;
                    int current = 1;
                    int size = SUB_SIZE;

                    request.setSize(size);
                    do {
                        request.setCurrent(current);
                        page = flowPurchaseMapper.page(request.getPage(), request);
                        if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                            break;
                        }

                        // 采购来源转换：1-大运河 2-京东
                        List<FlowPurchaseDO> flowPurchaseList = new ArrayList<>();
                        page.getRecords().forEach(o -> {
                            FlowPurchaseInventoryEnterpriseEnum purchaseInventorySource = FlowPurchaseInventoryEnterpriseEnum.getByName(o.getEnterpriseName().trim());
                            if (ObjectUtil.isNotNull(purchaseInventorySource)) {
                                o.setPoSource(purchaseInventorySource.getCode().toString());
                                flowPurchaseList.add(o);
                            }
                        });
                        // 按照 eid + goods_goods_in_sn + po_source 分组统计采购总数量
                        Map<String, List<FlowPurchaseDO>> purchaseMap = flowPurchaseList.stream().collect(Collectors.groupingBy(o -> o.getEid() + "_" + o.getGoodsInSn().trim() + "_" + o.getPoSource()));
                        for (String key : purchaseMap.keySet()) {
                            List<FlowPurchaseDO> flowPurchaseDOS = purchaseMap.get(key);
                            FlowPurchaseDO flowPurchaseOld = flowPurchaseDOS.get(0);
                            Long ylGoodsId = 0L;
                            FlowGoodsRelationDO flowGoodsRelationDO = ylGoodsIdMap.get(flowPurchaseOld.getEid() + "_" + flowPurchaseOld.getGoodsInSn().trim());
                            if(ObjectUtil.isNotNull(flowGoodsRelationDO)){
                                ylGoodsId = flowGoodsRelationDO.getYlGoodsId();
                            }
                            // 更新采购库存
                            BigDecimal totalPoQuantity = flowPurchaseDOS.stream().map(FlowPurchaseDO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
                            SaveFlowPurchaseInventoryTotalPoQuantityRequest purchaseTotalPoQuantity = totalPoQuantityMap.get(key);
                            if (ObjectUtil.isNull(purchaseTotalPoQuantity)) {
                                SaveFlowPurchaseInventoryTotalPoQuantityRequest purchaseInventoryRequest = new SaveFlowPurchaseInventoryTotalPoQuantityRequest();
                                purchaseInventoryRequest.setEid(flowPurchaseOld.getEid());
                                purchaseInventoryRequest.setGoodsInSn(flowPurchaseOld.getGoodsInSn());
                                purchaseInventoryRequest.setYlGoodsId(ylGoodsId);
                                purchaseInventoryRequest.setPoSource(Integer.parseInt(flowPurchaseOld.getPoSource()));
                                purchaseInventoryRequest.setTotalPoQuantity(totalPoQuantity);
                                purchaseInventoryRequest.setEname(flowPurchaseOld.getEname());
                                purchaseInventoryRequest.setGoodsName(flowPurchaseOld.getGoodsName());
                                purchaseInventoryRequest.setGoodsSpecifications(flowPurchaseOld.getPoSpecifications());
                                totalPoQuantityMap.put(key, purchaseInventoryRequest);
                            } else {
                                BigDecimal totalPoQuantityOld = purchaseTotalPoQuantity.getTotalPoQuantity();
                                BigDecimal totalPoQuantityNew = totalPoQuantityOld.add(totalPoQuantity);
                                purchaseTotalPoQuantity.setTotalPoQuantity(totalPoQuantityNew);
                                totalPoQuantityMap.put(key, purchaseTotalPoQuantity);
                            }
                        }

                        if(page.getRecords().size() < size){
                            break;
                        }
                        current = current + 1;
                    } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
                }

                // 采购库存
                if (MapUtil.isNotEmpty(totalPoQuantityMap)) {
                    for (String totalPoQuantityKey : totalPoQuantityMap.keySet()) {
                        SaveFlowPurchaseInventoryTotalPoQuantityRequest flowPurchaseInventoryRequest = totalPoQuantityMap.get(totalPoQuantityKey);
                        flowPurchaseInventoryService.saveOrUpdateTotalPoQuantity(flowPurchaseInventoryRequest);
                    }
                }
            }
        } catch (Exception e) {
            log.error("[采购流向采购数量统计]异常, exception:{}", e.getMessage());
            e.printStackTrace();
            return;
        }
        log.info("[采购流向采购数量统计]任务执行结束, 耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public void updateFlowSaleCrmGoodsSign(List<Long> eids) {
        flowPurchaseService.updateFlowPurchaseCrmGoodsSign(eids);
    }

    @Override
    public Date getMaxPoTimeByEid(Long eid) {
        return flowPurchaseService.getMaxPoTimeByEid(eid);
    }

    @Override
    public boolean isHaveDataByEidAndPoTime(QueryFlowPurchaseExistRequest request) {
        return flowPurchaseService.isHaveDataByEidAndPoTime(request);
    }

    @Override
    public List<FlowStatisticsBO> getFlowPurchaseStatistics(QueryFlowStatisticesRequest request) {
        return flowPurchaseService.getFlowPurchaseStatistics(request);
    }


    private List<FlowPurchaseGoodsDTO> generateRank(List<String> poMonthList, List<FlowPurchaseGoodsDTO> flowPurchaseGoodsList) {
        for (String poMonth : poMonthList) {
            // 按照每月的入库数量排序
            flowPurchaseGoodsList = flowPurchaseGoodsList.stream().sorted(Comparator.comparing(f -> f.getStorageInfoMap().get(poMonth).getStorageQuantity(), Comparator.nullsLast(BigDecimal::compareTo))).collect(Collectors.toList());
            Collections.reverse(flowPurchaseGoodsList);

            int rank = 1;
            for (FlowPurchaseGoodsDTO flowPurchaseGoodsDTO : flowPurchaseGoodsList) {
                StorageInfoDTO storageInfoDTO = flowPurchaseGoodsDTO.getStorageInfoMap().get(poMonth);
                if (storageInfoDTO == null || storageInfoDTO.getStorageQuantity() == null) {    // 改月数量为null时，不计入排名
                    continue;
                }
                storageInfoDTO.setRank(rank);
                rank++;
            }
        }

        // 按照总数排序
        flowPurchaseGoodsList = flowPurchaseGoodsList.stream().sorted(Comparator.comparing(FlowPurchaseGoodsDTO::getStorageQuantitySum).reversed()).collect(Collectors.toList());

        for (int i = 0; i < flowPurchaseGoodsList.size(); i++) {
            FlowPurchaseGoodsDTO flowPurchaseGoodsDTO = flowPurchaseGoodsList.get(i);
            flowPurchaseGoodsDTO.setRank(i + 1);
        }

        return flowPurchaseGoodsList;
    }

    private List<FlowPurchaseMonthDTO> covertToFlowPurchaseMonthDTOList(List<Map<String, Object>> mapList, QueryFlowPurchaseListRequest request) {
        List<FlowPurchaseMonthDTO> flowPurchaseMonthDTOList = new ArrayList<>();

        FlowPurchaseMonthDTO flowPurchaseMonthSumDTO = new FlowPurchaseMonthDTO();
        flowPurchaseMonthSumDTO.setStorageMoneySum(new BigDecimal(0));
        flowPurchaseMonthSumDTO.setStorageQuantitySum(new BigDecimal(0));
        Map<String, StorageInfoDTO> storageInfoSumMap = new HashMap<>();

        for (Map<String, Object> map : mapList) {
            // map转换为对象
            FlowPurchaseMonthDTO flowPurchaseMonthDTO = generateFlowPurchaseMonthDTO(map, request);

            Map<String, StorageInfoDTO> storageInfoMap = flowPurchaseMonthDTO.getStorageInfoMap();
            // 第一行的合计信息 纵向累加
            for (Map.Entry<String, StorageInfoDTO> entry : storageInfoMap.entrySet()) {
                String time = entry.getKey();
                StorageInfoDTO storageInfoDTO = entry.getValue();
                if (!storageInfoSumMap.containsKey(time)) {
                    storageInfoSumMap.put(time, new StorageInfoDTO(time, new BigDecimal(0), new BigDecimal(0)));
                }
                StorageInfoDTO storageInfoSum = storageInfoSumMap.get(time);
                storageInfoSum.setStorageMoney(storageInfoSum.getStorageMoney().add(storageInfoDTO.getStorageMoney() != null ? storageInfoDTO.getStorageMoney() : new BigDecimal(0)));
                storageInfoSum.setStorageQuantity(storageInfoSum.getStorageQuantity().add(storageInfoDTO.getStorageQuantity() != null ? storageInfoDTO.getStorageQuantity() : new BigDecimal(0)));
            }

            // 累加总的金额和数量
            flowPurchaseMonthSumDTO.setStorageMoneySum(flowPurchaseMonthSumDTO.getStorageMoneySum().add(flowPurchaseMonthDTO.getStorageMoneySum() != null ? flowPurchaseMonthDTO.getStorageMoneySum() : new BigDecimal(0)));
            flowPurchaseMonthSumDTO.setStorageQuantitySum(flowPurchaseMonthSumDTO.getStorageQuantitySum().add(flowPurchaseMonthDTO.getStorageQuantitySum() != null ? flowPurchaseMonthDTO.getStorageQuantitySum() : new BigDecimal(0)));

            flowPurchaseMonthDTO.setStorageInfoMap(storageInfoMap);

            flowPurchaseMonthDTOList.add(flowPurchaseMonthDTO);
        }

        flowPurchaseMonthSumDTO.setStorageInfoMap(storageInfoSumMap);
        // 将合计信息放到数据第一行
        flowPurchaseMonthDTOList.add(0, flowPurchaseMonthSumDTO);

        return flowPurchaseMonthDTOList;
    }

    private FlowPurchaseMonthDTO generateFlowPurchaseMonthDTO(Map<String, Object> map, QueryFlowPurchaseListRequest request) {
        FlowPurchaseMonthDTO flowPurchaseMonthDTO = new FlowPurchaseMonthDTO();

        flowPurchaseMonthDTO.setPurchaseEnterpriseId((Long) map.get("eid"));
        flowPurchaseMonthDTO.setPurchaseEnterpriseName((String) map.get("pName"));
        flowPurchaseMonthDTO.setPurchaseChannelId((Long) map.get("pChannelId"));
        flowPurchaseMonthDTO.setSupplierEnterpriseId((Long) map.get("supplierId"));
        flowPurchaseMonthDTO.setSupplierEnterpriseName((String) map.get("sName"));
        flowPurchaseMonthDTO.setSupplierChannelId((Long) map.get("sChannelId"));
        flowPurchaseMonthDTO.setStorageMoneySum((BigDecimal) map.get("poMoney"));
        flowPurchaseMonthDTO.setStorageQuantitySum((BigDecimal) map.get("poQuantity"));

        // 只查一个月的数据的情况下，当月数量与金额和总数据与金额相等
        Map<String, StorageInfoDTO> storageInfoMap = new HashMap<>();
        if (!StringUtils.isEmpty(request.getTime())) {
            StorageInfoDTO storageInfoDTO = new StorageInfoDTO();
            storageInfoDTO.setStorageMoney(flowPurchaseMonthDTO.getStorageMoneySum());
            storageInfoDTO.setStorageQuantity(flowPurchaseMonthDTO.getStorageQuantitySum());
            storageInfoMap.put(request.getTime(), storageInfoDTO);
        } else {
            // 获取每个月的统计数据
            for (String poMonth : request.getPoMonthList()) {
                StorageInfoDTO storageInfoDTO = new StorageInfoDTO();
                storageInfoDTO.setStorageMoney((BigDecimal) map.get("poMoney_" + poMonth));
                storageInfoDTO.setStorageQuantity((BigDecimal) map.get("poQuantity_" + poMonth));
                storageInfoMap.put(poMonth, storageInfoDTO);
            }
        }
        flowPurchaseMonthDTO.setStorageInfoMap(storageInfoMap);

        return flowPurchaseMonthDTO;
    }

    private Date getMonthDate() {
        return getMonthDate(null);
    }

    private Date getMonthDate(String monthStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date monthDate = null;
        if (StringUtils.isEmpty(monthStr)) {
            Date date = new Date();
            monthStr = sdf.format(date);
        }

        try {
            monthDate = sdf.parse(monthStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return monthDate;
    }


}
