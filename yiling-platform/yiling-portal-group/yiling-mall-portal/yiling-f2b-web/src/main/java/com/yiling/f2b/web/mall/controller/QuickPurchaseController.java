package com.yiling.f2b.web.mall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.f2b.web.common.utils.GoodsAssemblyUtils;
import com.yiling.f2b.web.common.utils.VoUtils;
import com.yiling.f2b.web.goods.vo.DistributorGoodsVO;
import com.yiling.f2b.web.goods.vo.GoodsSkuVO;
import com.yiling.f2b.web.goods.vo.QtyVO;
import com.yiling.f2b.web.mall.vo.QuickPurchaseGoodsVO;
import com.yiling.f2b.web.mall.vo.RecommendGoodsGroupVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.api.RecommendGoodsGroupApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupRelationDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.QueryDistributorGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryRecommendGoodsGroupPageRequest;
import com.yiling.goods.medicine.enums.GoodsGroupStatusEnum;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsOverSoldEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderHistoryGoodsDTO;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/6/28
 */
@RestController
@RequestMapping("/quick/purchase")
@Api(tags = "快速采购模块")
@Slf4j
public class QuickPurchaseController {

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InventoryApi inventoryApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @DubboReference
    GoodsPriceApi goodsPriceApi;

    @DubboReference
    RecommendGoodsGroupApi recommendGoodsGroupApi;

    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    @Autowired
    VoUtils voUtils;

    @ApiOperation(value = "根据商品名称查询协议内商品")
    @GetMapping("/queryGoodsByName")
    @UserAccessAuthentication
    public Result<CollectionObject<QuickPurchaseGoodsVO>> queryGoodsByName(@RequestParam("name") @ApiParam(name = "name", value = "输入的关键字", required = true) String name, @CurrentUser CurrentStaffInfo staffInfo) {
        List<QuickPurchaseGoodsVO> list = new ArrayList<>();
//        AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
//        request.setName(name);
//        request.setPurchaseEid(staffInfo.getCurrentEid());
//        List<AgreementGoodsDTO> goodsDTOS = agreementGoodsApi.getYearPurchaseGoodsList(request);
//        List<Long> goodsIds = goodsDTOS.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setName(name);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        List<PopGoodsDTO> ylPopGoodsList = popGoodsApi.queryPopGoods(request);
        if(CollectionUtil.isEmpty(ylPopGoodsList)){
            return Result.success(new CollectionObject<>(list));
        }
        List<Long> ylGoodsIds = ylPopGoodsList.stream().map(PopGoodsDTO::getGoodsId).collect(Collectors.toList());
        Map<Long, List<DistributorGoodsBO>> procurementGoodsMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(ylGoodsIds, staffInfo.getCurrentEid());
        if(CollectionUtil.isEmpty(procurementGoodsMap)){
            return Result.success(new CollectionObject<>(list));
        }
        ylGoodsIds = ListUtil.toList(procurementGoodsMap.keySet());
        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(ylGoodsIds);
        Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryGoodsPriceRequest.setGoodsIds(ylGoodsIds);
        Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

        ylGoodsIds.forEach(e -> {
            StandardGoodsBasicDTO goodsInfoDTO = goodsInfoDTOMap.get(e);
            List<DistributorGoodsBO> distributorGoodsBOList = procurementGoodsMap.get(e);
            if (goodsInfoDTO != null && CollectionUtil.isNotEmpty(distributorGoodsBOList)) {
                List<Long> sellerEids = distributorGoodsBOList.stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList());
                //统计有多个配送商
                List<DistributorGoodsVO> queryDistributorList = goodsAssemblyUtils.getDistributorGoods(0L, goodsInfoDTO.getId(),sellerEids, staffInfo.getCurrentEid());
                QuickPurchaseGoodsVO quickPurchaseGoodsVO = new QuickPurchaseGoodsVO();
                quickPurchaseGoodsVO.setId(goodsInfoDTO.getId());
                quickPurchaseGoodsVO.setSellSpecifications(goodsInfoDTO.getSellSpecifications());
                quickPurchaseGoodsVO.setSellUnit(goodsInfoDTO.getSellUnit());
                quickPurchaseGoodsVO.setName(goodsInfoDTO.getStandardGoods().getName());
                quickPurchaseGoodsVO.setYlGoodsId(e);
                quickPurchaseGoodsVO.setDistributorGoodsList(queryDistributorList);
                quickPurchaseGoodsVO.setDistributorCount(queryDistributorList.size());
                quickPurchaseGoodsVO.setOverSoldType(goodsInfoDTO.getOverSoldType());
                //商品价格体系
                GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                goodsPriceVO.setBuyPrice(priceMap.getOrDefault(e, BigDecimal.ZERO));
                quickPurchaseGoodsVO.setPriceInfo(goodsPriceVO);
                list.add(quickPurchaseGoodsVO);
            }
        });
        return Result.success(new CollectionObject<>(list));
    }
    @ApiOperation(value = "根据推荐商品组id查询协议内商品")
    @GetMapping("/queryGoodsByRecommendGroupId")
    @UserAccessAuthentication
    public Result<CollectionObject<QuickPurchaseGoodsVO>> queryGoodsByRecommendGroupId(@RequestParam("groupId") @ApiParam(name = "groupId", value = "推荐商品组id", required = true) Long groupId, @CurrentUser CurrentStaffInfo staffInfo){
        List<QuickPurchaseGoodsVO> list = new ArrayList<>();
        RecommendGoodsGroupDTO goodsGroupDTO = recommendGoodsGroupApi.getGroupById(groupId);
        if(null==goodsGroupDTO){
            return Result.failed("该商品组不存在");
        }
        if(!GoodsGroupStatusEnum.ENABLE.getCode().equals(goodsGroupDTO.getQuickPurchaseFlag())){
            return Result.failed("商品组已下线");
        }

        if(CollectionUtil.isNotEmpty(goodsGroupDTO.getRelationList())){
            List<Long> goodsIds = goodsGroupDTO.getRelationList().stream().map(RecommendGoodsGroupRelationDTO::getGoodsId).collect(Collectors.toList());
//            AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
//            request.setPurchaseEid(staffInfo.getCurrentEid());
//            request.setGoodsIds(goodsIds);
//            List<AgreementGoodsDTO> goodsDTOS = agreementGoodsApi.getYearPurchaseGoodsList(request);
//            List<Long> agreementGoodsIds = goodsDTOS.stream().map(e -> e.getGoodsId()).distinct().collect(Collectors.toList());
            Map<Long, List<DistributorGoodsBO>> procurementGoodsMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(goodsIds, staffInfo.getCurrentEid());
            if(CollectionUtil.isEmpty(procurementGoodsMap)){
                return Result.success(new CollectionObject<>(list));
            }
            List<Long> ylGoodsIdList = ListUtil.toList(procurementGoodsMap.keySet());
            //获取标准库商品信息
            List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(ylGoodsIdList);
            Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

            //获取商品定价系统
            QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
            queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
            queryGoodsPriceRequest.setGoodsIds(ylGoodsIdList);
            Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

            goodsGroupDTO.getRelationList().forEach(e->{
                StandardGoodsBasicDTO goodsInfoDTO = goodsInfoDTOMap.get(e.getGoodsId());
                List<DistributorGoodsBO> distributorGoodsBOList = procurementGoodsMap.get(e.getGoodsId());
                if (goodsInfoDTO != null && CollectionUtil.isNotEmpty(distributorGoodsBOList)) {
                    List<Long> sellerEids = distributorGoodsBOList.stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList());
                    //统计有多个配送商
                    List<DistributorGoodsVO> queryDistributorList = goodsAssemblyUtils.getDistributorGoods(0L, goodsInfoDTO.getId(),sellerEids, staffInfo.getCurrentEid());
                    if(CollectionUtil.isNotEmpty(queryDistributorList)){
                        QuickPurchaseGoodsVO quickPurchaseGoodsVO = new QuickPurchaseGoodsVO();
                        quickPurchaseGoodsVO.setId(goodsInfoDTO.getId());
                        quickPurchaseGoodsVO.setSellSpecifications(goodsInfoDTO.getSellSpecifications());
                        quickPurchaseGoodsVO.setSellUnit(goodsInfoDTO.getSellUnit());
                        quickPurchaseGoodsVO.setName(goodsInfoDTO.getStandardGoods().getName());
                        quickPurchaseGoodsVO.setYlGoodsId(e.getGoodsId());
                        quickPurchaseGoodsVO.setDistributorGoodsList(queryDistributorList);
                        quickPurchaseGoodsVO.setDistributorCount(queryDistributorList.size());
                        quickPurchaseGoodsVO.setOverSoldType(goodsInfoDTO.getOverSoldType());
                        //商品价格体系
                        GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                        goodsPriceVO.setBuyPrice(priceMap.getOrDefault(e.getGoodsId(), BigDecimal.ZERO));
                        quickPurchaseGoodsVO.setPriceInfo(goodsPriceVO);
                        list.add(quickPurchaseGoodsVO);
                    }
                }
            });
        }else{
            return Result.failed("选择的商品组合无商品");
        }
        if(CollectionUtil.isEmpty(list)){
            return Result.failed("没有商品可添加，请检查商品上架状态和采购协议");
        }
        return Result.success(new CollectionObject<>(list));
    }


    @ApiOperation(value = "查询推荐商品组")
    @GetMapping("/queryRecommendGoodsGroup")
    public Result<CollectionObject<RecommendGoodsGroupVO>> queryRecommendGoodsGroup(@CurrentUser CurrentStaffInfo staffInfo){
        QueryRecommendGoodsGroupPageRequest request=new QueryRecommendGoodsGroupPageRequest();
        request.setEid(Constants.YILING_EID);
        request.setQueryLimit(20);
        request.setQuickPurchaseFlag(GoodsGroupStatusEnum.ENABLE.getCode());
        List<RecommendGoodsGroupDTO> groupDTOList = recommendGoodsGroupApi.queryGroupList(request);
        if(CollectionUtil.isNotEmpty(groupDTOList)){
            groupDTOList=groupDTOList.stream().filter(group->CollectionUtil.isNotEmpty(group.getRelationList())).collect(Collectors.toList());
        }
        return Result.success(new CollectionObject<>(PojoUtils.map(groupDTOList,RecommendGoodsGroupVO.class)));
    }

    @ApiOperation(value = "跟据以岭商品id查询配送商及商品信息")
    @GetMapping("/queryDistributorList")
    public Result<CollectionObject<DistributorGoodsVO>> queryDistributorList(@RequestParam("skuId") @ApiParam(name = "skuId", value = "配送商skuId", required = true) Long skuId, @RequestParam("goodsId") @ApiParam(name = "goodsId", value = "以岭商品Id", required = true) Long goodsId, @RequestParam("distributorEid") @ApiParam(name = "distributorEid", value = "配送商信息", required = false) Long distributorEid,
                                                                             @CurrentUser CurrentStaffInfo staffInfo) {
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        Map<Long, List<DistributorGoodsBO>> distributorMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(ListUtil.toList(goodsId), buyerEid);
        List<DistributorGoodsBO> distributorGoodsBOList = distributorMap.get(goodsId);
        if(CollectionUtil.isEmpty(distributorGoodsBOList)){
            return  Result.success(new CollectionObject<>(new ArrayList<>()));
        }
        List<Long> sellerEids = distributorGoodsBOList.stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList());

        if(null!=distributorEid && distributorEid != 0){
            if(sellerEids.contains(distributorEid)){
                sellerEids = ListUtil.toList(distributorEid);
            }else {
                return  Result.success(new CollectionObject<>(new ArrayList<>()));
            }
        }
        //直接查询配送商信息
        return Result.success(new CollectionObject<>(goodsAssemblyUtils.getDistributorGoods(skuId, goodsId, sellerEids, buyerEid)));
    }

    @ApiOperation(value = "快速采购的库存加减")
    @GetMapping("/verifyInventory")
    @UserAccessAuthentication
    public Result<QtyVO> verifyInventory(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long
            skuId, @RequestParam Long stockNum) {
        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(skuId);
        QtyVO qtyVO = new QtyVO();
        if (inventoryDTO != null) {
            Long usableInventory = inventoryDTO.getQty() - inventoryDTO.getFrozenQty();
            //超卖商品直接返回原购买数量
            if (GoodsOverSoldEnum.OVER_SOLD.getType().equals(inventoryDTO.getOverSoldType())){
                qtyVO.setUsableBool(true);
                qtyVO.setUsableInventory(stockNum);
            }else if(usableInventory >= stockNum) {
                qtyVO.setUsableBool(true);
                qtyVO.setUsableInventory(stockNum);
            } else {
                qtyVO.setUsableBool(false);
                qtyVO.setUsableInventory(usableInventory);
            }
        } else {
            qtyVO.setUsableBool(false);
            qtyVO.setUsableInventory(0L);
        }
        return Result.success(qtyVO);
    }

    @ApiOperation(value = "查看历史订单商品列表")
    @GetMapping("/list")
    @UserAccessAuthentication
    public Result<CollectionObject<DistributorGoodsVO>> list(@CurrentUser CurrentStaffInfo staffInfo) {
        Long buyerEid = staffInfo.getCurrentEid();

        //查询以岭的商品ID
//        AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
//        request.setPurchaseEid(buyerEid);
//        List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getYearPurchaseGoodsList(request);
//        List<Long> goodsIds = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());

        //查询历史订单列表，过滤协议之外的商品信息
        List<OrderHistoryGoodsDTO> orderHistoryGoodsList = orderApi.getOrderGoodsByEidPOPAdmin(buyerEid);
        List<Long> orderHistoryYlGoodsIds = orderHistoryGoodsList.stream().map(OrderHistoryGoodsDTO::getGoodsId).collect(Collectors.toList());
        Map<Long, List<DistributorGoodsBO>> procurementGoodsMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(orderHistoryYlGoodsIds, buyerEid);
        List<OrderHistoryGoodsDTO> orderHistoryGoodsFilterList = orderHistoryGoodsList.stream().filter(e->e.getGoodsSkuId()!=null&&e.getGoodsSkuId()!=0).filter(e -> procurementGoodsMap.keySet().contains(e.getGoodsId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderHistoryGoodsFilterList)) {
            return Result.success(new CollectionObject(ListUtil.empty()));
        }
        //查询采购关系内的配送商信息，过滤协议之外的配送信息
        orderHistoryGoodsFilterList = orderHistoryGoodsFilterList.stream().filter(e -> {
            List<DistributorGoodsBO> goodsList = procurementGoodsMap.get(e.getGoodsId());
            if(CollectionUtil.isNotEmpty(goodsList)){
                List<Long> distributorEids = goodsList.stream().map(DistributorGoodsBO::getDistributorEid).collect(Collectors.toList());
                if(distributorEids.contains(e.getDistributorEid())){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(orderHistoryGoodsFilterList)) {
            return Result.success(new CollectionObject(ListUtil.empty()));
        }

        //批量查询配送商商品信息
        Map<Long, List<GoodsDTO>> goodsInfoDTOMap = goodsApi.batchQueryDistributorGoodsInfo(PojoUtils.map(orderHistoryGoodsFilterList, QueryDistributorGoodsRequest.class));
        List<GoodsDTO> goodsInfoDTOList = new ArrayList<>();
        for (Map.Entry<Long, List<GoodsDTO>> entry : goodsInfoDTOMap.entrySet()) {
            goodsInfoDTOList.addAll(entry.getValue());
        }
        List<Long> goodsIdList = goodsInfoDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<PopGoodsDTO> popGoodsDTOList=popGoodsApi.getPopGoodsListByGoodsIds(goodsIdList);
        Map<Long,PopGoodsDTO> popGoodsDTOMap=popGoodsDTOList.stream().collect(Collectors.toMap(PopGoodsDTO::getGoodsId,Function.identity()));

        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(buyerEid);
        queryGoodsPriceRequest.setGoodsIds(goodsIdList);
        Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

        //获取标准库商品信息
        List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
        Map<Long, StandardGoodsBasicDTO> standardGoodsDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

        //批量获取sku库存信息
        List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIdsAndStatus(goodsIdList, ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode()));
        Map<Long, Map<Long, GoodsSkuDTO>> skuMapByGoods = goodsSkuDTOList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId, Collectors.toMap(GoodsSkuDTO::getId, Function.identity())));

        //批量查询配送商信息
        List<Long> distributorEidList = orderHistoryGoodsFilterList.stream().map(e -> e.getDistributorEid()).collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseDTOList = ListUtil.empty();
        if (CollUtil.isNotEmpty(distributorEidList)) {
            enterpriseDTOList = enterpriseApi.listByIds(distributorEidList);
        }
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        //只取前20个商品
        Integer limit = 20;
        List<DistributorGoodsVO> list = new ArrayList<>(limit);
        orderHistoryGoodsFilterList.forEach(e -> {
            List<GoodsDTO> goodsInfoList = goodsInfoDTOMap.get(e.getGoodsId());
            Map<Long, GoodsSkuDTO> skuDTOMap = skuMapByGoods.get(e.getDistributorGoodsId());
            for (GoodsDTO goodsInfoDTO : goodsInfoList) {
                PopGoodsDTO popGoodsDTO=popGoodsDTOMap.get(goodsInfoDTO.getId());
                if (popGoodsDTO != null && null != skuDTOMap && goodsInfoDTO.getEid().equals(e.getDistributorEid()) && goodsInfoDTO.getId().equals(e.getDistributorGoodsId())&& popGoodsDTO.getGoodsStatus().equals(GoodsStatusEnum.UP_SHELF.getCode()) && list.size() <= limit) {
                    GoodsSkuDTO skuDTO = skuDTOMap.get(e.getGoodsSkuId());
                    if(null==skuDTO){
                        continue;
                    }
                    Long availableQty = skuDTO.getQty() - skuDTO.getFrozenQty();
                    if (availableQty > 0) {
                        StandardGoodsBasicDTO standardGoodsBasicDTO = standardGoodsDTOMap.get(goodsInfoDTO.getId());
                        List<GoodsSkuDTO> skuDTOList=skuDTOMap.values().stream().filter(f->f.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
                        List<GoodsSkuVO> goodsSkuVOList= PojoUtils.map(skuDTOList, GoodsSkuVO.class);
                        for(GoodsSkuVO goodsSkuVO:goodsSkuVOList){
                            if(goodsSkuVO.getId().equals(e.getGoodsSkuId())){
                                goodsSkuVO.setSelectFlag(1);
                            }
                        }
                        DistributorGoodsVO distributorGoodsVO = new DistributorGoodsVO();
                        distributorGoodsVO.setGoodsSkuList(goodsSkuVOList);
                        distributorGoodsVO.setName(standardGoodsBasicDTO.getName());
                        distributorGoodsVO.setSellSpecifications(goodsInfoDTO.getSellSpecifications());
                        distributorGoodsVO.setYlGoodsId(e.getGoodsId());
                        distributorGoodsVO.setGoodsId(skuDTO.getGoodsId());
                        distributorGoodsVO.setDistributorId(e.getDistributorEid());
                        distributorGoodsVO.setDistributorName(enterpriseDTOMap.get(e.getDistributorEid()).getName());
                        distributorGoodsVO.setOverSoldType(goodsInfoDTO.getOverSoldType());
                        //商品价格体系
                        GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                        goodsPriceVO.setBuyPrice(priceMap.getOrDefault(goodsInfoDTO.getId(), BigDecimal.ZERO));
                        distributorGoodsVO.setPriceInfo(goodsPriceVO);
                        list.add(distributorGoodsVO);
                    }
                }
            }
        });
        return Result.success(new CollectionObject(list));
    }
}
