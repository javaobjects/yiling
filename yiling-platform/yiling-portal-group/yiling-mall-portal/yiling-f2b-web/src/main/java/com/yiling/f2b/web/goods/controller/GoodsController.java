package com.yiling.f2b.web.goods.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.f2b.web.common.utils.GoodsAssemblyUtils;
import com.yiling.f2b.web.common.utils.VoUtils;
import com.yiling.f2b.web.goods.form.QueryEsGoodsSearchForm;
import com.yiling.f2b.web.goods.vo.DistributorGoodsVO;
import com.yiling.f2b.web.goods.vo.GoodsDetailVO;
import com.yiling.f2b.web.goods.vo.SimpleAgreementVO;
import com.yiling.f2b.web.mall.form.QueryAgreementPurchaseFrom;
import com.yiling.f2b.web.mall.vo.AgreementGoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsGoodsSearchRequest;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.bo.TempAgreementGoodsBO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.agreement.util.AgreementUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.dto.DistributorGoodsBO;
import com.yiling.user.procrelation.dto.request.QuerySpecByBuyerPageRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author dexi.yao
 * @date 2021-06-17
 */
@RestController
@Api(tags = "商品信息相关")
@RequestMapping("/goods")
public class GoodsController {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InventoryApi inventoryApi;

    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;

    @DubboReference
    AgreementApi agreementApi;

    @DubboReference
    AgreementBusinessApi agreementBusinessApi;

    @DubboReference
    EsGoodsSearchApi esGoodsSearchApi;

    @DubboReference
    GoodsPriceApi goodsPriceApi;

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;

    @Autowired
    VoUtils voUtils;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    @ApiOperation(value = "商品详情页")
    @GetMapping("/detail")
    public Result<GoodsDetailVO> detail(
            @RequestParam("goodsId") @ApiParam(required = true, name = "goodsId", value = "商品id") Long goodsId,
            @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        //获取普通商品
        GoodsFullDTO goodsInfoDTO = goodsApi.queryFullInfo(goodsId);
        if (goodsInfoDTO == null) {
            return Result.failed("商品信息不存在");
        }
        if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsInfoDTO.getAuditStatus())) {
            return Result.failed("该商品已失效");
        }
        GoodsDetailVO goodsDetailVO = voUtils.toGoodsDetailVO(goodsInfoDTO);

        // 获取商品状态
        Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getGoodsLimitByGids(Arrays.asList(goodsId), buyerEid);
        if (goodsLimitMap != null) {
            goodsDetailVO.setGoodsLimitStatus(goodsLimitMap.get(goodsId));
        }

        if (buyerEid != null && buyerEid > 0) {
            //获取商品定价系统
            QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
            queryGoodsPriceRequest.setCustomerEid(buyerEid);
            queryGoodsPriceRequest.setGoodsIds(Arrays.asList(goodsId));
            Map<Long, BigDecimal> priceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(priceMap.getOrDefault(goodsId, BigDecimal.ZERO));
            goodsDetailVO.setPriceInfo(goodsPriceVO);
            //获取协议信息
//            AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
//            request.setPurchaseEid(buyerEid);
//            request.setGoodsId(goodsId);
//            List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getTempPurchaseGoodsList(request);
//            if (CollUtil.isNotEmpty(agreementGoodsDTOList)) {
//                List<Long> agreementIds = agreementGoodsDTOList.stream().map(e -> e.getAgreementId()).collect(Collectors.toList());
//                List<SupplementAgreementDetailDTO> supplementAgreementDetailDTOList = agreementApi.querySupplementAgreementsDetailList(agreementIds);
//                List<SimpleAgreementVO> simpleAgreementList = new ArrayList<>();
//                for (SupplementAgreementDetailDTO supplementAgreementDetailDTO : supplementAgreementDetailDTOList) {
//                    SimpleAgreementVO simpleAgreementVO = new SimpleAgreementVO();
//                    simpleAgreementVO.setName(supplementAgreementDetailDTO.getName());
//                    simpleAgreementVO.setSimpleContent(AgreementUtils.getAgreementText(supplementAgreementDetailDTO));
//                    simpleAgreementList.add(simpleAgreementVO);
//                }
//                goodsDetailVO.setSimpleAgreementList(simpleAgreementList);
//            }
            Map<Long, List<DistributorGoodsBO>> procurementGoodsMap = procurementRelationGoodsApi.getDistributorByYlGoodsIdAndBuyer(ListUtil.toList(goodsId), buyerEid);
            List<DistributorGoodsBO> procurementGoods = procurementGoodsMap.get(goodsId);
            if(CollectionUtil.isNotEmpty(procurementGoods)){
                //获取配送商商品信息
                goodsDetailVO.setDistributorGoodsList(goodsAssemblyUtils.getDistributorGoods(goodsId,procurementGoods,buyerEid));
            }else {
                goodsDetailVO.setDistributorGoodsList(ListUtil.empty());
            }
        }
        return Result.success(goodsDetailVO);
    }

//    @ApiOperation(value = "根据以岭商品id、配送商id查询库存、价格等售卖信息")
//    @GetMapping("/queryGoodsSaleInfo")
//    public Result<DistributorGoodsVO> queryGoodsSaleInfo(@Valid @RequestBody QueryGoodsSaleInfoForm saleInfoForm,
//                                                         @CurrentUser CurrentStaffInfo staffInfo) {
//        //直接查询配送商信息
//        List<Long> sellerIds = enterprisePurchaseRelationApi.listSellerEidsByBuyerEid(staffInfo.getCurrentEid());
//        if (CollUtil.isEmpty(sellerIds)) {
//            return Result.failed("没有对应的配送商信息");
//        }
//
//        if (!sellerIds.contains(saleInfoForm.getDistributorEid())) {
//            return Result.failed("该配送商不在配送关系内");
//        }
//
//        DistributorGoodsVO distributorGoodsVO = new DistributorGoodsVO();
//        //查询库存信息
//        InventoryDTO inventoryDTO = inventoryApi.getInventoryByGidAndDistributorEid(saleInfoForm.getGoodsId(), saleInfoForm.getDistributorEid());
//        StandardGoodsBasicDTO goodsInfoDTO = goodsApi.queryStandardGoodsBasic(inventoryDTO.getGid());
//        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(saleInfoForm.getDistributorEid());
//        distributorGoodsVO.setDistributorId(saleInfoForm.getDistributorEid());
//        distributorGoodsVO.setDistributorName(enterpriseDTO.getName());
//        distributorGoodsVO.setBigPackage(goodsInfoDTO.getBigPackage());
//        distributorGoodsVO.setGoodsId(inventoryDTO.getGid());
//        distributorGoodsVO.setQty(inventoryDTO.getQty());
//        distributorGoodsVO.setFrozenQty(inventoryDTO.getFrozenQty());
//        return Result.success(distributorGoodsVO);
//    }

    /**
     * 查询商品信息列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "查询商品信息列表")
    @PostMapping(path = "/search")
    public Result<EsAggregationDTO<GoodsItemVO>> searchGoods(@RequestBody QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        List<Long> eids = enterpriseApi.listSubEids(Constants.YILING_EID);
        EsGoodsSearchRequest request = PojoUtils.map(form, EsGoodsSearchRequest.class);
        request.setEids(eids);
        request.setBuyerGather(String.valueOf(buyerEid));
        request.setPopFlag(1);
        request.setPopStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());

        EsAggregationDTO data = esGoodsSearchApi.searchGoods(request);
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();

        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.assembly(goodsIds, buyerEid));
        }
        return Result.success(data);
    }

    /**
     * 查询商品信息列表
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "商品搜索推荐")
    @PostMapping(path = "/searchGoodsSuggest")
    public Result<List<String>> searchGoodsSuggest(@RequestBody QueryEsGoodsSearchForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        List<Long> eids = enterpriseApi.listSubEids(Constants.YILING_EID);
        EsGoodsSearchRequest request = PojoUtils.map(form, EsGoodsSearchRequest.class);
        request.setEids(eids);
        request.setBuyerGather(String.valueOf(buyerEid));
        request.setPopFlag(1);
        request.setPopStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        List<String> suggestList = esGoodsSearchApi.searchGoodsSuggest(request);
        return Result.success(suggestList);
    }

    @ApiOperation(value = "可采商品分页")
    @PostMapping("/canPurchaseGoods")
    @UserAccessAuthentication
    public Result<Page<GoodsItemVO>> canPurchaseGoods(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody QueryAgreementPurchaseFrom form){
        // 企业ID
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        QuerySpecByBuyerPageRequest request = PojoUtils.map(form, QuerySpecByBuyerPageRequest.class);
        request.setBuyerEid(buyerEid);
        Page<Long> page = procurementRelationGoodsApi.queryGoodsPageByBuyer(request);
        Page<GoodsItemVO> pageVO = new Page<>(page.getCurrent(), page.getSize());
        pageVO.setTotal(page.getTotal());
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            List<Long> ylGoodsIdList = page.getRecords();
            //获取标准库上品信息
            List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(ylGoodsIdList);
            Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

            //商品按钮状态
            Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getGoodsLimitByGids(ylGoodsIdList, staffInfo.getCurrentEid());

            //获取配送商商品id，用于查询价格
            List<Long> specIdList = standardGoodsDTOList.stream().map(StandardGoodsBasicDTO::getSellSpecificationsId).collect(Collectors.toList());
            Map<Long, List<Long>> distributorGoodsIdMap = goodsAssemblyUtils.getDistributorGoodsIdBySpec(specIdList, buyerEid);
            List<Long> distributorGoodsIdList = distributorGoodsIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

            //获取商品定价系统
            QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
            queryGoodsPriceRequest.setCustomerEid(buyerEid);
            queryGoodsPriceRequest.setGoodsIds(distributorGoodsIdList);
            Map<Long, BigDecimal> priceMap=goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

            List<GoodsItemVO> goodsListItemList = new ArrayList<>();
            ylGoodsIdList.forEach(ylGoodsId->{
                //获取商品信息
                GoodsItemVO goodsItemVO = new GoodsItemVO();
                StandardGoodsBasicDTO goodsInfo = goodsInfoDTOMap.get(ylGoodsId);
                goodsItemVO.setGoodsInfo(voUtils.toSimpleGoodsVO(goodsInfo));
                if(null!=goodsInfo){
                    //获取该商品规格下所有配送商商品id
                    List<Long> distributorGoodsIds = distributorGoodsIdMap.get(goodsInfo.getSellSpecificationsId());
                    //根据配送商id和价格map组装价格对象
                    GoodsPriceVO priceVO = goodsAssemblyUtils.getPriceByDistributorGoods(distributorGoodsIds, priceMap);
                    goodsItemVO.setPriceInfo(priceVO);
                }
                goodsItemVO.setGoodsLimitStatus(goodsLimitMap.get(ylGoodsId));

                goodsListItemList.add(goodsItemVO);
            });
            pageVO.setRecords(goodsListItemList);

        }
        return Result.success(pageVO);
    }
}
