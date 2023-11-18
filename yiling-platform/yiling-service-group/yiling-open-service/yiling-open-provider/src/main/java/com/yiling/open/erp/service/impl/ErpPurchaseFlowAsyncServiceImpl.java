package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryDTO;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequestDetail;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.enums.FlowPurchaseInventoryEnterpriseEnum;
import com.yiling.open.erp.service.ErpPurchaseFlowAsyncService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/11/5
 */
@Slf4j
@Service(value = "erpPurchaseFlowAsyncService")
public class ErpPurchaseFlowAsyncServiceImpl implements ErpPurchaseFlowAsyncService {

    @DubboReference(timeout = 10 * 1000)
    private FlowPurchaseInventoryApi flowPurchaseInventoryApi;
    @DubboReference(timeout = 10 * 1000)
    private FlowGoodsRelationApi flowGoodsRelationApi;

    /**
     * 流向采购库存变更
     * 仅限供应商名称匹配为：
     * 大运河采购：河北大运河医药物流有限公司
     * 京东采购：泰州市京东医药有限责任公司
     *
     * @param operType
     * @param erpClient
     * @param oldFlowPurchaseList
     * @param newFlowPurchaseList
     */
    @Override
    @Async("asyncExecutor")
    public void handlerFlowPurchaseInventory(Integer operType, ErpClientDTO erpClient, List<FlowPurchaseDTO> oldFlowPurchaseList, List<FlowPurchaseDTO> newFlowPurchaseList) {
        List<FlowPurchaseDTO> oldWithYlGoodsList = new ArrayList<>();
        if (CollUtil.isNotEmpty(oldFlowPurchaseList)) {
            oldWithYlGoodsList = oldFlowPurchaseList.stream().filter(o -> ObjectUtil.isNotNull(o.getPoQuantity()) && StrUtil.isNotBlank(o.getGoodsInSn()) && FlowPurchaseInventoryEnterpriseEnum.getEnterpriseNameList().contains(o.getEnterpriseName().trim())).collect(Collectors.toList());
        }

        Long eid = erpClient.getRkSuId();
        List<FlowPurchaseDTO> newWithYlGoodsList = new ArrayList<>();
        if (CollUtil.isNotEmpty(newFlowPurchaseList)) {
            newWithYlGoodsList = newFlowPurchaseList.stream().filter(o -> ObjectUtil.isNotNull(o.getPoQuantity()) && StrUtil.isNotBlank(o.getGoodsInSn()) && FlowPurchaseInventoryEnterpriseEnum.getEnterpriseNameList().contains(o.getEnterpriseName().trim())).collect(Collectors.toList());
        }

        // 删除
        if (ObjectUtil.equal(3, operType) && CollUtil.isNotEmpty(oldWithYlGoodsList)) {
            // 根据采购库存id更新数量
            updateFlowPurchaseInventoryQuantity(eid, erpClient.getClientName(), oldWithYlGoodsList, null);
            return;
        }

        // 新增、更新
        if ((ObjectUtil.equal(1, operType) || ObjectUtil.equal(2, operType) || ObjectUtil.equal(4, operType)) && CollUtil.isNotEmpty(newWithYlGoodsList)) {
            if (ObjectUtil.equal(1, operType)) {
                // 新增采购库存
                updateFlowPurchaseInventoryQuantity(eid, erpClient.getClientName(), null, newWithYlGoodsList);
            } else {
                // 根据采购库存id更新数量
                if (ObjectUtil.equal(1, operType)) {
                    updateFlowPurchaseInventoryQuantity(eid, erpClient.getClientName(), null, newWithYlGoodsList);
                } else {
                    updateFlowPurchaseInventoryQuantity(eid, erpClient.getClientName(), oldWithYlGoodsList, newWithYlGoodsList);
                }
            }
            return;
        }
    }

    /**
     * 采购库存更新
     * 库存增量 =  新数据库存数 - 旧数据库存数
     *
     * @param eid
     * @param ename
     * @param oldWithYlGoodsIdList
     * @param newWithYlGoodsIdList
     */
    private void updateFlowPurchaseInventoryQuantity(Long eid, String ename, List<FlowPurchaseDTO> oldWithYlGoodsIdList, List<FlowPurchaseDTO> newWithYlGoodsIdList) {
        if (CollUtil.isEmpty(oldWithYlGoodsIdList) && CollUtil.isEmpty(newWithYlGoodsIdList)) {
            return;
        }

        // 删除
        if (CollUtil.isNotEmpty(oldWithYlGoodsIdList) && CollUtil.isEmpty(newWithYlGoodsIdList)) {
            BigDecimal oldQuantity = BigDecimal.ZERO;
            if (CollUtil.isNotEmpty(oldWithYlGoodsIdList)) {
                oldQuantity = oldWithYlGoodsIdList.stream().filter(o -> o.getPoQuantity().compareTo(BigDecimal.ZERO) != 0).map(FlowPurchaseDTO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            BigDecimal newQuantity = BigDecimal.ZERO;
            BigDecimal quantity = oldQuantity.negate().add(newQuantity);

            FlowPurchaseInventoryDTO flowPurchaseInventoryDTO = null;
            FlowPurchaseDTO oldFlowPurchase = oldWithYlGoodsIdList.get(0);
            FlowPurchaseInventoryEnterpriseEnum inventoryEnterpriseEnum = FlowPurchaseInventoryEnterpriseEnum.getByName(oldFlowPurchase.getEnterpriseName().trim());
            if (ObjectUtil.isNotNull(inventoryEnterpriseEnum)) {
                flowPurchaseInventoryDTO = flowPurchaseInventoryApi.getByEidAndGoodsInSnAndSource(eid, oldFlowPurchase.getGoodsInSn(), inventoryEnterpriseEnum.getCode());
            }
            if (ObjectUtil.isNull(flowPurchaseInventoryDTO)) {
                return;
            }
            List<UpdateFlowPurchaseInventoryQuantityRequestDetail> inventoryQuantityList = new ArrayList<>();
            UpdateFlowPurchaseInventoryQuantityRequestDetail inventoryQuantityRequestDetail = new UpdateFlowPurchaseInventoryQuantityRequestDetail();
            inventoryQuantityRequestDetail.setId(flowPurchaseInventoryDTO.getId());
            inventoryQuantityRequestDetail.setQuantity(quantity);
            inventoryQuantityList.add(inventoryQuantityRequestDetail);
            UpdateFlowPurchaseInventoryQuantityRequest inventoryRequest = new UpdateFlowPurchaseInventoryQuantityRequest();
            inventoryRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.FLOW.getCode());
            inventoryRequest.setList(inventoryQuantityList);
            inventoryRequest.setOpUserId(0L);
            inventoryRequest.setOpTime(new Date());
            flowPurchaseInventoryApi.updateQuantityById(inventoryRequest);
        } else {
            // 新增、更新
            saveOrUpdateFlowPurchaseInventory(eid, ename, oldWithYlGoodsIdList, newWithYlGoodsIdList);
        }
    }

    private void saveOrUpdateFlowPurchaseInventory(Long eid, String ename, List<FlowPurchaseDTO> oldList, List<FlowPurchaseDTO> newList) {
        // 以岭品关系
        List<Long> eidList = newList.stream().map(FlowPurchaseDTO::getEid).distinct().collect(Collectors.toList());
        List<String> goodsInSnList = newList.stream().filter(o -> StrUtil.isNotBlank(o.getGoodsInSn())).map(FlowPurchaseDTO::getGoodsInSn).distinct().collect(Collectors.toList());
        List<FlowGoodsRelationDTO> flowGoodsRelationList = flowGoodsRelationApi.getByEidAndGoodsInSn(eidList, goodsInSnList);
        Map<String, FlowGoodsRelationDTO> flowGoodsRelationMap = new HashMap<>();
        if (CollUtil.isNotEmpty(flowGoodsRelationList)) {
            flowGoodsRelationList = flowGoodsRelationList.stream().filter(o -> ObjectUtil.isNotNull(o.getYlGoodsId()) && 0 != o.getYlGoodsId().intValue()).collect(Collectors.toList());
            // 根据eid、goodsInSn分组
            flowGoodsRelationMap = flowGoodsRelationList.stream().collect(Collectors.toMap(o -> o.getEid() + "_" + o.getGoodsInSn().trim(), o -> o, (k1, k2) -> k1));
        }

        // 历史单据
        Map<String, List<FlowPurchaseDTO>> oldPurchaseMap = new HashMap<>();
        if (CollUtil.isNotEmpty(oldList)) {
            oldPurchaseMap = oldList.stream().collect(Collectors.groupingBy(o -> o.getEid() + "_" + o.getGoodsInSn().trim() + "_" + o.getPoSource()));
        }

        // 采购来源转换：1-大运河 2-京东
        List<FlowPurchaseDTO> flowPurchaseList = new ArrayList<>();
        newList.forEach(o -> {
            FlowPurchaseInventoryEnterpriseEnum purchaseInventorySource = FlowPurchaseInventoryEnterpriseEnum.getByName(o.getEnterpriseName().trim());
            if (ObjectUtil.isNotNull(purchaseInventorySource)) {
                o.setPoSource(purchaseInventorySource.getCode().toString());
                flowPurchaseList.add(o);
            }
        });
        // 按照 eid + goods_goods_in_sn + po_source 分组统计采购总数量
        Map<String, List<FlowPurchaseDTO>> purchaseMap = flowPurchaseList.stream().collect(Collectors.groupingBy(o -> o.getEid() + "_" + o.getGoodsInSn().trim() + "_" + o.getPoSource()));
        for (String key : purchaseMap.keySet()) {
            List<FlowPurchaseDTO> flowPurchaseDTOS = purchaseMap.get(key);
            FlowPurchaseDTO flowPurchaseOld = flowPurchaseDTOS.get(0);

            FlowGoodsRelationDTO flowGoodsRelationDTO = flowGoodsRelationMap.get(flowPurchaseOld.getEid() + "_" + flowPurchaseOld.getGoodsInSn().trim());
            if (ObjectUtil.isNull(flowGoodsRelationDTO)) {
                continue;
            }

            // 历史总库存
            BigDecimal oldPoQuantity = BigDecimal.ZERO;
            List<FlowPurchaseDTO> oldFlowPurchaseDTOS = oldPurchaseMap.get(key);
            if (CollUtil.isNotEmpty(oldFlowPurchaseDTOS)) {
                oldPoQuantity = oldFlowPurchaseDTOS.stream().map(FlowPurchaseDTO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            // 总库存变动值
            BigDecimal newPoQuantity = flowPurchaseDTOS.stream().map(FlowPurchaseDTO::getPoQuantity).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal quantity = oldPoQuantity.negate().add(newPoQuantity);

            // 更新采购库存
            SaveFlowPurchaseInventoryRequest purchaseInventoryRequest = new SaveFlowPurchaseInventoryRequest();
            purchaseInventoryRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.FLOW.getCode());
            purchaseInventoryRequest.setEid(eid);
            purchaseInventoryRequest.setEname(ename);
            purchaseInventoryRequest.setGoodsName(flowPurchaseOld.getGoodsName());
            purchaseInventoryRequest.setGoodsInSn(flowPurchaseOld.getGoodsInSn().trim());
            purchaseInventoryRequest.setGoodsSpecifications(flowPurchaseOld.getPoSpecifications());
            purchaseInventoryRequest.setYlGoodsId(flowGoodsRelationDTO.getYlGoodsId());
            purchaseInventoryRequest.setPoQuantity(quantity);
            purchaseInventoryRequest.setPoSource(Integer.parseInt(flowPurchaseOld.getPoSource()));
            flowPurchaseInventoryApi.saveOrUpdateFlowPurchaseInventory(purchaseInventoryRequest);
        }
    }
}
