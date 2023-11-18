package com.yiling.dataflow.order.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.bo.FlowPurchaseInventoryBO;
import com.yiling.dataflow.order.bo.FlowPurchaseInventorySumAdjustmentQuantityBO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryListPageDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryLogListPageDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryRequest;
import com.yiling.dataflow.order.dto.request.UpdateFlowPurchaseInventoryQuantityRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryDO;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryLogDO;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.dataflow.order.enums.FlowPurchaseInventorySourceEnum;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryLogService;
import com.yiling.dataflow.order.service.FlowPurchaseInventoryService;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationDO;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流向商品库存信息
 *
 * @author: houjie.sun
 * @date: 2022/5/26
 */
@Slf4j
@DubboService
public class FlowPurchaseInventoryApiImpl implements FlowPurchaseInventoryApi {

    @Autowired
    private FlowPurchaseInventoryService flowPurchaseInventoryService;
    @Autowired
    private FlowPurchaseInventoryLogService flowPurchaseInventoryLogService;
    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;

    @DubboReference
    private UserApi userApi;
    @DubboReference
    private GoodsApi goodsApi;

    @Override
    public FlowPurchaseInventoryDTO getByEidAndGoodsInSnAndSource(Long eid, String goodsInSn, Integer source) {
        Assert.notNull(eid, "参数 eid 不能为空");
        Assert.notBlank(goodsInSn, "参数 goodsInSn 不能为空");
        Assert.notNull(source, "参数 source 不能为空");
        return PojoUtils.map(flowPurchaseInventoryService.getByEidAndGoodsInSnAndSource(eid, goodsInSn, source), FlowPurchaseInventoryDTO.class);
    }

    @Override
    public List<FlowPurchaseInventoryBO> getListByEidAndYlGoodsIdAndGoodsInSn(QueryFlowPurchaseInventorySettlementRequest request) {
        Assert.notNull(request, "参数不能为空");
        Assert.notNull(request.getList(), "参数 list 不能为空");
        request.getList().forEach(detail -> {
            Assert.notNull(detail.getEid(), "参数 eid 不能为空");
            Assert.notNull(detail.getYlGoodsId(), "参数 ylGoodsId 不能为空");
            Assert.notBlank(detail.getGoodsInSn(), "参数 goodsInSn 不能为空");
        });

        List<FlowPurchaseInventoryDTO> list = PojoUtils.map(flowPurchaseInventoryService.getListByEidAndYlGoodsIdAndGoodsInSn(request), FlowPurchaseInventoryDTO.class);
        if(CollUtil.isEmpty(list)){
            return ListUtil.empty();
        }
        Map<String, List<FlowPurchaseInventoryDTO>> inventoryMap = list.stream().collect(Collectors.groupingBy(o -> o.getEid() + "_" + o.getYlGoodsId() + "_" + o.getGoodsInSn()));
        if(MapUtil.isEmpty(inventoryMap)){
            return ListUtil.empty();
        }

        List<FlowPurchaseInventoryBO> resultList = new ArrayList<>();
        for (Map.Entry<String, List<FlowPurchaseInventoryDTO>> entry: inventoryMap.entrySet()){
            List<FlowPurchaseInventoryDTO> inventoryList = entry.getValue();
            if(CollUtil.isEmpty(inventoryList)){
                continue;
            }

            if(inventoryList.size() > 2){
                log.error("流向库存数据 po_source 异常, 参数：request{}; 异常数据：flowPurchaseInventoryList{}", request, inventoryList);
                throw new ServiceException("流向库存数据异常");
            }
            Map<Integer, List<FlowPurchaseInventoryDTO>> poSourceMap = inventoryList.stream().collect(Collectors.groupingBy(FlowPurchaseInventoryDTO::getPoSource));
            for(Map.Entry<Integer, List<FlowPurchaseInventoryDTO>> poSourceEntry: poSourceMap.entrySet()){
                if(CollUtil.isNotEmpty(poSourceEntry.getValue()) && poSourceEntry.getValue().size() > 1){
                    log.error("流向库存数据 po_source 异常, 参数：request{}; 异常数据：flowPurchaseInventoryList{}", request, poSourceEntry.getValue());
                    throw new ServiceException("流向库存数据异常");
                }
            }

            FlowPurchaseInventoryBO bo = new FlowPurchaseInventoryBO();
            bo.setEid(inventoryList.get(0).getEid());
            bo.setYlGoodsId(inventoryList.get(0).getYlGoodsId());
            bo.setGoodsInSn(inventoryList.get(0).getGoodsInSn());
            for (FlowPurchaseInventoryDTO dto : inventoryList) {
                if(ObjectUtil.equal(FlowPurchaseInventorySourceEnum.DA_YUN_HE.getCode(), dto.getPoSource())){
                    bo.setDyhId(dto.getId());
                    bo.setDyhQuantity(dto.getPoQuantity());
                } else if(ObjectUtil.equal(FlowPurchaseInventorySourceEnum.JING_DONG.getCode(), dto.getPoSource())){
                    bo.setJdId(dto.getId());
                    bo.setJdQuantity(dto.getPoQuantity());
                }
            }
            resultList.add(bo);
        }

        return resultList;
    }

    @Override
    public int updateQuantityById(UpdateFlowPurchaseInventoryQuantityRequest request) {
        Assert.notNull(request, "参数不能为空");
        Assert.notEmpty(request.getList(), "参数 list 不能为空");
        request.getList().forEach(o -> {
            Assert.notNull(o.getId(), "参数 id 不能为空");
            Assert.notNull(o.getQuantity(), "参数 quantity 不能为空");
        });
        return flowPurchaseInventoryService.updateQuantityById(request);
    }

    @Override
    public Boolean saveOrUpdateFlowPurchaseInventory(SaveFlowPurchaseInventoryRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid()) || request.getEid().intValue() == 0 || StrUtil.isBlank(request.getGoodsInSn())
                || ObjectUtil.isNull(request.getYlGoodsId()) || request.getYlGoodsId().intValue() == 0){
            return false;
        }
//        Assert.notNull(request, "参数不能为空");
//        Assert.notNull(request.getEid(), "参数 eid 不能为空");
//        Assert.notBlank(request.getGoodsInSn(), "参数 goodsInSn 不能为空");
//        Assert.notNull(request.getYlGoodsId(), "参数 ylGoodsId 不能为空");
//        Assert.notBlank(request.getGoodsBatchNo(), "参数 goodsBatchNo 不能为空");
        return flowPurchaseInventoryService.saveOrUpdateFlowPurchaseInventory(request);
    }

    @Override
    public List<FlowPurchaseInventoryDTO> getListByEidAndYlGoodsIdAndGoodsInSnAndSource(QueryFlowPurchaseInventorySettlementRequest request) {
        return PojoUtils.map(flowPurchaseInventoryService.getListByEidAndYlGoodsIdAndGoodsInSnAndSource(request), FlowPurchaseInventoryDTO.class);
    }

    @Override
    public Page<FlowPurchaseInventoryListPageDTO> pageByEidAndYlGoodsId(QueryFlowPurchaseInventoryListPageRequest request) {
        if (ObjectUtil.isNull(request) || (ObjectUtil.isNull(request.getEid()) && ObjectUtil.isNull(request.getYlGoodsId()))) {
            throw new ServiceException("商业ID和以岭品ID，不能同时为空");
        }
        Page<FlowPurchaseInventoryListPageDTO> inventoryPage = PojoUtils.map(flowPurchaseInventoryService.pageByEidAndYlGoodsId(request), FlowPurchaseInventoryListPageDTO.class);
        if (ObjectUtil.isNull(inventoryPage) || CollUtil.isEmpty(inventoryPage.getRecords())) {
            return inventoryPage;
        }
        // 商品信息
        List<Long> ylGoodsIdList = inventoryPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getYlGoodsId()) && 0 != o.getYlGoodsId()).map(FlowPurchaseInventoryListPageDTO::getYlGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsList = goodsApi.batchQueryInfo(ylGoodsIdList);
        Map<Long, GoodsDTO> goodsMap = new HashMap<>();
        if(CollUtil.isNotEmpty(goodsList)){
            goodsMap = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        }
        // 扣减库存、设置商品名称、售卖规格名称
        List<FlowPurchaseInventoryListPageDTO> list = new ArrayList<>();
        for (FlowPurchaseInventoryListPageDTO flowPurchaseInventory : inventoryPage.getRecords()) {
            GoodsDTO goodsDTO = goodsMap.get(flowPurchaseInventory.getYlGoodsId());
            if (ObjectUtil.isNotNull(goodsDTO)) {
                flowPurchaseInventory.setYlGoodsName(goodsDTO.getName());
                flowPurchaseInventory.setYlGoodsSpecifications(goodsDTO.getSellSpecifications());
            }
            list.add(flowPurchaseInventory);
        }
        inventoryPage.setRecords(list);
        return inventoryPage;
    }

    @Override
    public Page<FlowPurchaseInventoryLogListPageDTO> adjustmentLogPageLogByInventoryId(QueryFlowPurchaseInventoryLogListPageRequest request) {
        if(ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getInventoryId())){
            throw new ServiceException("采购库存ID不能为空");
        }
        // 采购库存
        FlowPurchaseInventoryDO purchaseInventory = flowPurchaseInventoryService.getById(request.getInventoryId());
        if(ObjectUtil.isNull(purchaseInventory)){
            throw new ServiceException("采购库存信息不存在");
        }
        // 调整日志
        request.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT_ADJUSTMENT.getCode());
        Page<FlowPurchaseInventoryLogListPageDTO> page = new Page<>();
        page.setSize(request.getSize());
        page.setCurrent(request.getCurrent());
        Page<FlowPurchaseInventoryLogDO> logDOPage = flowPurchaseInventoryLogService.pageByInventoryId(request);
        if(ObjectUtil.isNull(logDOPage) || CollUtil.isEmpty(logDOPage.getRecords())){
            return page;
        }

        // 以岭品关系
        FlowGoodsRelationDO flowGoodsRelation = flowGoodsRelationService.getByEidAndGoodsInSn(purchaseInventory.getEid(), purchaseInventory.getGoodsInSn());
        boolean goodsRelationFlag = ObjectUtil.isNotNull(flowGoodsRelation) ? true : false;
        // 操作人
        List<Long> createUserIdList = logDOPage.getRecords().stream().filter(o -> ObjectUtil.isNotNull(o.getCreateUser())).map(FlowPurchaseInventoryLogDO::getCreateUser).distinct().collect(Collectors.toList());
        Map<Long, UserDTO> userMap = new HashMap();
        if (CollUtil.isNotEmpty(createUserIdList)) {
            List<UserDTO> userDtoList = userApi.listByIds(createUserIdList);
            userMap = userDtoList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(),(k1, k2) -> k2));
        }

        List<FlowPurchaseInventoryLogListPageDTO> list = new ArrayList<>();
        for (FlowPurchaseInventoryLogDO inventoryLog : logDOPage.getRecords()) {
            Long userId = inventoryLog.getCreateUser();
            UserDTO userDTO = userMap.get(userId);
            FlowPurchaseInventoryLogListPageDTO logPageDTO = new FlowPurchaseInventoryLogListPageDTO();
            logPageDTO.setOpUserId(inventoryLog.getCreateUser());
            logPageDTO.setCreateTime(inventoryLog.getCreateTime());
            if(ObjectUtil.isNotNull(userDTO)){
                logPageDTO.setOpUserName(userDTO.getName());
            }
            logPageDTO.setId(inventoryLog.getId());
            logPageDTO.setEid(inventoryLog.getEid());
            logPageDTO.setPoQuantity(inventoryLog.getChangeQuantity());
            logPageDTO.setPoSource(inventoryLog.getPoSource());
            logPageDTO.setEname(purchaseInventory.getEname());
            logPageDTO.setGoodsInSn(purchaseInventory.getGoodsInSn());
            logPageDTO.setGoodsName(purchaseInventory.getGoodsName());
            logPageDTO.setGoodsSpecifications(purchaseInventory.getGoodsSpecifications());
            logPageDTO.setYlGoodsId(purchaseInventory.getYlGoodsId());
            if(goodsRelationFlag){
                logPageDTO.setYlGoodsName(flowGoodsRelation.getYlGoodsName());
                logPageDTO.setYlGoodsSpecifications(flowGoodsRelation.getYlGoodsSpecifications());
            }
            list.add(logPageDTO);
        }
        if(CollUtil.isNotEmpty(list)){
            page.setTotal(logDOPage.getTotal());
            page.setRecords(list);
        }
        return page;
    }

    @Override
    public Map<Long, BigDecimal> getSumAdjustmentQuantityByInventoryIdList(List<Long> inventoryIdList) {
        Assert.notEmpty(inventoryIdList, "参数 inventoryIdList 不能为空");
        inventoryIdList = inventoryIdList.stream().distinct().collect(Collectors.toList());
        List<FlowPurchaseInventorySumAdjustmentQuantityBO> sumAdjustmentQuantityList = flowPurchaseInventoryLogService.getSumAdjustmentQuantityByInventoryIdList(inventoryIdList);
        Map<Long, BigDecimal> map = new HashMap<>();
        if(CollUtil.isEmpty(sumAdjustmentQuantityList)){
            inventoryIdList.forEach(id -> {
                map.put(id, BigDecimal.ZERO);
            });
            return map;
        }
        Map<Long, FlowPurchaseInventorySumAdjustmentQuantityBO> sumAdjustmentMap = sumAdjustmentQuantityList.stream().collect(Collectors.toMap(FlowPurchaseInventorySumAdjustmentQuantityBO::getInventoryId, Function.identity(), (k1, k2) -> k2));
        for (Long id : inventoryIdList) {
            FlowPurchaseInventorySumAdjustmentQuantityBO sumAdjustmentQuantityBO = sumAdjustmentMap.get(id);
            if(ObjectUtil.isNull(sumAdjustmentQuantityBO)){
                map.put(id, BigDecimal.ZERO);
            } else {
                map.put(id, sumAdjustmentQuantityBO.getSumPoQuantity());
            }
        }
        return map;
    }

}
