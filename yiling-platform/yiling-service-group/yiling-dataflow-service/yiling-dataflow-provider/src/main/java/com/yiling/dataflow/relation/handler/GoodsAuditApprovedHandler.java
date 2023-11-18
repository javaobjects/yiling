package com.yiling.dataflow.relation.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.order.dto.request.SaveOrUpdateFlowGoodsRelationRequest;
import com.yiling.dataflow.relation.service.FlowGoodsRelationService;
import com.yiling.framework.common.util.Constants;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商品审核通过处理
 *
 * @author: houjie.sun
 * @date: 2022/6/13
 */
@Slf4j
@Service
public class GoodsAuditApprovedHandler {

    @DubboReference
    private EnterpriseApi enterpriseApi;
    @DubboReference
    private GoodsApi goodsApi;
    @Autowired
    private FlowGoodsRelationService flowGoodsRelationService;

    public Boolean handlerGoodsAuditApprovedMqSync(Long goodsId, String goodsName, String specifications, String ename) {
        if (ObjectUtil.isNull(goodsId)) {
            return false;
        }

        // 只取以岭品
        List<Long> yilingEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        List<Long> goodsIds = new ArrayList<>();
        goodsIds.add(goodsId);
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIds, yilingEids);
        if (MapUtil.isEmpty(goodsMap)) {
            log.warn("GoodsAuditApprovedHandler handlerGoodsAuditApprovedMqSync, goodsMap is empty, 非以岭品不做商家品与以岭的关系映射");
            return true;
        }
        Long ylGoodsId = goodsMap.get(goodsId);
        if (ObjectUtil.isNull(ylGoodsId) || 0 == ylGoodsId.intValue()) {
            log.warn("GoodsAuditApprovedHandler handlerGoodsAuditApprovedMqSync, ylGoodsId is null or 0, 非以岭品不做商家品与以岭的关系映射");
            return true;
        }
        // 以岭品信息
        GoodsFullDTO ylGoods = goodsApi.queryFullInfo(ylGoodsId);
        if (ObjectUtil.isNull(ylGoods)) {
            log.error("GoodsAuditApprovedHandler handlerGoodsAuditApprovedMqSync, 此商家商品对应的以岭商品不存在, goodsId:{}, ylGoodsId:{}", goodsId, ylGoodsId);
            return false;
        }
        if (ylGoods.getAuditStatus().intValue() != GoodsStatusEnum.AUDIT_PASS.getCode().intValue()) {
            log.error("GoodsAuditApprovedHandler handlerGoodsAuditApprovedMqSync, 此以岭商品未审核通过, goodsId:{}, ylGoodsId:{}", goodsId, ylGoodsId);
            return false;
        }
        // 获取erp商品内码
        List<GoodsSkuDTO> goodsSkuList = goodsApi.getGoodsSkuByGoodsIdAndStatus(goodsId, ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
        if (CollUtil.isEmpty(goodsSkuList)) {
            log.warn("GoodsAuditApprovedHandler handlerGoodsAuditApprovedMqSync, goodsSkuList is empty, goodsId:{},非以岭品不做商家品与以岭的关系映射", goodsId);
            return false;
        }
        // 获取商品ERP内码不为空的并去重
        List<String> inSnList = goodsSkuList.stream().filter(o -> StrUtil.isNotBlank(o.getInSn())).map(GoodsSkuDTO::getInSn).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(inSnList)) {
            return true;
        }
        Long eid = goodsSkuList.get(0).getEid();
        String ylGoodsName = ylGoods.getStandardGoodsAllInfo().getBaseInfo().getName();
        String manufacturer = ylGoods.getStandardGoodsAllInfo().getBaseInfo().getManufacturer();

        // 映射关系新增、更新
        SaveOrUpdateFlowGoodsRelationRequest request = new SaveOrUpdateFlowGoodsRelationRequest();
        request.setEid(eid);
        request.setEname(ename);
        request.setGoodsName(goodsName);
        request.setGoodsSpecifications(specifications);
        request.setGoodsManufacturer(manufacturer);
        request.setYlGoodsId(ylGoodsId);
        request.setYlGoodsName(ylGoodsName);
        request.setYlGoodsSpecifications(ylGoods.getSellSpecifications());
        request.setGoodsInSnList(inSnList);
        return flowGoodsRelationService.saveOrUpdateByGoodsAuditApproved(request);
    }

}
