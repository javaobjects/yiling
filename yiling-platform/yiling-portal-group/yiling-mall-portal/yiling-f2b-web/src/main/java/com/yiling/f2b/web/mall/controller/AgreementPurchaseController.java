package com.yiling.f2b.web.mall.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.f2b.web.common.utils.GoodsAssemblyUtils;
import com.yiling.f2b.web.common.utils.VoUtils;
import com.yiling.f2b.web.mall.form.QueryAgreementPurchaseFrom;
import com.yiling.f2b.web.mall.vo.AgreementGoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.agreement.api.AgreementApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.bo.SupplementaryAgreementGoodsBO;
import com.yiling.user.agreement.bo.TempAgreementGoodsBO;
import com.yiling.user.agreement.dto.AgreementDTO;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsPageRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/6/15
 */
@RestController
@RequestMapping("/agreement/purchase")
@Api(tags = "协议可采购商品")
@Slf4j
public class AgreementPurchaseController extends BaseController {

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    AgreementApi agreementApi;

    @DubboReference
    AgreementBusinessApi agreementBusinessApi;

    @DubboReference
    GoodsPriceApi goodsPriceApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    VoUtils voUtils;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    @ApiOperation(value = "获取主协议可采购商品")
    @PostMapping("/yearGoodsList")
    @UserAccessAuthentication
    @Deprecated
    public Result<Page<GoodsItemVO>> yearGoodsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryAgreementPurchaseFrom form) {
       Long buyerEid=staffInfo.getCurrentEid();

        AgreementPurchaseGoodsPageRequest request = PojoUtils.map(form, AgreementPurchaseGoodsPageRequest.class);
        request.setPurchaseEid(buyerEid);
//        Page<AgreementGoodsDTO> page = agreementGoodsApi.getYearPurchaseGoodsPageList(request);
        //原通过年度协议查询商品无法筛选上架商品，调整为使用补充协议直接查询上架商品
        Page<SupplementaryAgreementGoodsBO> page = agreementGoodsApi.getSupplementarySaleGoodPageList(request);
        if (page != null) {
            Page<GoodsItemVO> newPage = new Page<>(page.getCurrent(), page.getSize());
            List<SupplementaryAgreementGoodsBO> list = page.getRecords();
            List<Long> goodsIdList = list.stream().map(SupplementaryAgreementGoodsBO::getGoodsId).collect(Collectors.toList());

            //获取标准库上品信息
            List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
            Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

            //商品按钮状态
            Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getGoodsLimitByGids(goodsIdList, staffInfo.getCurrentEid());

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
            goodsIdList.forEach(e->{
                GoodsItemVO goodsItemVO = new GoodsItemVO();
                StandardGoodsBasicDTO goodsInfo = goodsInfoDTOMap.get(e);
                goodsItemVO.setGoodsInfo(voUtils.toSimpleGoodsVO(goodsInfo));
                if(null!=goodsInfo){
                    List<Long> distributorGoodsIds = distributorGoodsIdMap.get(goodsInfo.getSellSpecificationsId());
                    GoodsPriceVO priceVO = goodsAssemblyUtils.getPriceByDistributorGoods(distributorGoodsIds, priceMap);
                    goodsItemVO.setPriceInfo(priceVO);
                }
                goodsItemVO.setGoodsLimitStatus(goodsLimitMap.get(e));
                goodsListItemList.add(goodsItemVO);
            });
            newPage.setRecords(goodsListItemList);
            newPage.setTotal(page.getTotal());
            newPage.setCurrent(page.getCurrent());
            newPage.setSize(page.getSize());
            return Result.success(newPage);
        }
        return Result.failed(ResultCode.FAILED);
    }


    @ApiOperation(value = "获取补充协议可采购商品")
    @PostMapping("/tempGoodsList")
    @UserAccessAuthentication
    @Deprecated
    public Result<Page<AgreementGoodsVO>> tempGoodsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryAgreementPurchaseFrom form) {
        Long buyerEid=staffInfo.getCurrentEid();

        AgreementPurchaseGoodsPageRequest request = PojoUtils.map(form, AgreementPurchaseGoodsPageRequest.class);
        request.setPurchaseEid(buyerEid);
        Page<TempAgreementGoodsBO> page = agreementGoodsApi.getTempPurchaseGoodsPageList(request);
        if (page != null) {
            Page<AgreementGoodsVO> newPage = new Page<>(page.getCurrent(), page.getSize());
            List<TempAgreementGoodsBO> list = page.getRecords();
            List<Long> goodsIdList = new ArrayList<>();
            List<Long> agreementIdList = new ArrayList<>();
            for(TempAgreementGoodsBO tempAgreementGoodsBO:list){
                goodsIdList.add(tempAgreementGoodsBO.getGoodsId());
               if(StringUtils.isNotEmpty(tempAgreementGoodsBO.getAgreementIds())) {
                   String[] agreementIds=tempAgreementGoodsBO.getAgreementIds().split(",");
                   for(String agreementId:agreementIds) {
                       agreementIdList.add(Long.parseLong(agreementId));
                   }
               }
            }

            //获取标准库上品信息
            List<StandardGoodsBasicDTO> standardGoodsDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
            Map<Long, StandardGoodsBasicDTO> goodsInfoDTOMap = standardGoodsDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));

            //商品按钮状态
            Map<Long, Integer> goodsLimitMap = agreementBusinessApi.getGoodsLimitByGids(goodsIdList, staffInfo.getCurrentEid());

            //获取配送商商品id，用于查询价格
            List<Long> specIdList = standardGoodsDTOList.stream().map(StandardGoodsBasicDTO::getSellSpecificationsId).collect(Collectors.toList());
            Map<Long, List<Long>> distributorGoodsIdMap = goodsAssemblyUtils.getDistributorGoodsIdBySpec(specIdList, buyerEid);
            List<Long> distributorGoodsIdList = distributorGoodsIdMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

            //获取商品定价系统
            QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
            queryGoodsPriceRequest.setCustomerEid(buyerEid);
            queryGoodsPriceRequest.setGoodsIds(distributorGoodsIdList);
            Map<Long, BigDecimal> priceMap=goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);

            //获取协议列表信息
            List<AgreementDTO> agreementDTOList= agreementApi.getAgreementDetailsInfoByIds(agreementIdList);
            Map<Long,String> agreementMap=agreementDTOList.stream().collect(Collectors.toMap(AgreementDTO::getId, AgreementDTO::getName));

            List<AgreementGoodsVO> goodsListItemList = new ArrayList<>();
            list.forEach(e->{
                AgreementGoodsVO agreementGoodsVO=new AgreementGoodsVO();
                //获取商品信息
                GoodsItemVO goodsItemVO = new GoodsItemVO();
                StandardGoodsBasicDTO goodsInfo = goodsInfoDTOMap.get(e.getGoodsId());
                goodsItemVO.setGoodsInfo(voUtils.toSimpleGoodsVO(goodsInfo));
                if(null!=goodsInfo){
                    //获取该商品规格下所有配送商商品id
                    List<Long> distributorGoodsIds = distributorGoodsIdMap.get(goodsInfo.getSellSpecificationsId());
                    //根据配送商id和价格map组装价格对象
                    GoodsPriceVO priceVO = goodsAssemblyUtils.getPriceByDistributorGoods(distributorGoodsIds, priceMap);
                    goodsItemVO.setPriceInfo(priceVO);
                }
                goodsItemVO.setGoodsLimitStatus(goodsLimitMap.get(e.getGoodsId()));
                agreementGoodsVO.setGoodsItem(goodsItemVO);
                //获取协议信息
                 List<String> agrementNameList=new ArrayList<>();
                if(StringUtils.isNotEmpty(e.getAgreementIds())){
                    String[] agreementIds=e.getAgreementIds().split(",");
                    for(String agreementId:agreementIds) {
                        agrementNameList.add(agreementMap.getOrDefault(Long.parseLong(agreementId),""));
                    }
                }
                agreementGoodsVO.setAgrementNameList(agrementNameList);
                goodsListItemList.add(agreementGoodsVO);
            });
            newPage.setRecords(goodsListItemList);
            newPage.setTotal(page.getTotal());
            newPage.setCurrent(page.getCurrent());
            newPage.setSize(page.getSize());
            return Result.success(newPage);
        }
        return Result.failed(ResultCode.FAILED);
    }
}
