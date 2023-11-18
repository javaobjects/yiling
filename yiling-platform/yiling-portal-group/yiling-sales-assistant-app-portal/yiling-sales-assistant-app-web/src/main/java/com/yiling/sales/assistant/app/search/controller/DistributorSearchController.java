package com.yiling.sales.assistant.app.search.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.mall.customer.api.CustomerSearchApi;
import com.yiling.mall.customer.dto.request.CustomerVerificationRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.sales.assistant.app.search.form.QueryDistributorPageForm;
import com.yiling.sales.assistant.app.search.vo.DistributorInfoVO;
import com.yiling.sales.assistant.app.search.vo.SimpleActivityVO;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.request.QueryTaskDistributorRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseCustomerLineApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 配送商模块
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.controller
 * @date: 2021/9/13
 */
@RestController
@Api(tags = "配送商")
@Slf4j
@RequestMapping("/distributor/search/")
public class DistributorSearchController extends BaseController {
    private static final            String SHOP_LOGO_DEFAULT_PIC = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/common/picture/shop_default_pic.png";
    @DubboReference
    ShopApi                         shopApi;
    @DubboReference
    B2bGoodsApi                     b2bGoodsApi;
    @DubboReference
    EnterprisePurchaseRelationApi   enterprisePurchaseRelationApi;
    @DubboReference
    PopGoodsApi                     popGoodsApi;
    @DubboReference
    EnterpriseApi                   enterpriseApi;
    @DubboReference
    CustomerSearchApi               customerSearchApi;
    @DubboReference
    PromotionActivityApi            promotionActivityApi;
    @DubboReference
    EnterpriseCustomerLineApi       enterpriseCustomerLineApi;
    @DubboReference
    TaskApi                         taskApi;


    @FunctionalInterface
    private interface distributorSearchFunction {

        Result<Page<DistributorInfoVO>> apply(CurrentStaffInfo staffInfo, QueryDistributorPageForm distributorPageForm);
    }


    @ApiOperation(value = "选择配送商")
    @RequestMapping(path = "/list", method = { org.springframework.web.bind.annotation.RequestMethod.POST })
    @UserAccessAuthentication
    public Result<Page<DistributorInfoVO>> distributorSearch(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryDistributorPageForm distributorPageForm) {

        CustomerVerificationRequest verificationRequest = new CustomerVerificationRequest();
        verificationRequest.setCurrentEid(staffInfo.getCurrentEid());
        verificationRequest.setContactUserId(staffInfo.getCurrentUserId());
        verificationRequest.setCustomerEid(distributorPageForm.getPurchaseEid());
        verificationRequest.setOrderType(staffInfo.getYilingFlag() ? OrderTypeEnum.POP.getCode() : OrderTypeEnum.B2B.getCode());
        Boolean checkResult = customerSearchApi.checkIsMyCustomer(verificationRequest);

        if (!checkResult) {
            return Result.failed("请选择自己名下的客户!");
        }

        Map<Boolean, distributorSearchFunction> distributorSearchFunction = Maps.newHashMapWithExpectedSize(2);
        distributorSearchFunction.put(Boolean.TRUE, this::popDistributorSearch);
        distributorSearchFunction.put(Boolean.FALSE, this::b2bDistributorSearch);

        return distributorSearchFunction.get(staffInfo.getYilingFlag()).apply(staffInfo, distributorPageForm);
    }

    /**
     * pop 配送商商品查询
     *
     * @param distributorPageForm
     * @return
     */
    private Result<Page<DistributorInfoVO>> popDistributorSearch(CurrentStaffInfo staffInfo, QueryDistributorPageForm distributorPageForm) {
        List<Long> sellerIds = enterprisePurchaseRelationApi.listSellerEidsByBuyerEid(distributorPageForm.getPurchaseEid());
        if (CollUtil.isEmpty(sellerIds)) {
            return Result.success(new Page<>(distributorPageForm.getCurrent(), distributorPageForm.getSize()));
        }
        QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
        request.setIds(sellerIds);
        request.setCurrent(distributorPageForm.getCurrent());
        request.setSize(distributorPageForm.getSize());
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnableStatusEnum.ENABLED.getCode());

        Page<EnterpriseDTO> pageEnterpriseDTOList = enterpriseApi.pageList(request);
        if (ObjectUtil.isEmpty(pageEnterpriseDTOList) || CollUtil.isEmpty(pageEnterpriseDTOList.getRecords())) {
            return Result.success(new Page<>(1, distributorPageForm.getSize()));
        }
        Page<DistributorInfoVO> listPage = PojoUtils.map(pageEnterpriseDTOList, DistributorInfoVO.class);
        List<DistributorInfoVO> resultList = pageEnterpriseDTOList.getRecords().stream().map(EnterpriseDto -> {
            EnterpriseGoodsCountBO enterpriseGoodsCountBo = popGoodsApi.getGoodsCountByEid(EnterpriseDto.getId());
            DistributorInfoVO distributorInfoVO = PojoUtils.map(EnterpriseDto, DistributorInfoVO.class);
            distributorInfoVO.setGoodStandards(enterpriseGoodsCountBo.getSellSpecificationCount());
            distributorInfoVO.setGoodsKind(enterpriseGoodsCountBo.getStandardCount());
            distributorInfoVO.setDistributorEid(EnterpriseDto.getId());
            distributorInfoVO.setName(EnterpriseDto.getName());
            distributorInfoVO.setPurchaseEid(distributorPageForm.getPurchaseEid());
            distributorInfoVO.setLogo(StringUtils.isBlank(EnterpriseDto.getLogo()) ? SHOP_LOGO_DEFAULT_PIC : EnterpriseDto.getLogo());
            distributorInfoVO.setStartAmount(BigDecimal.ZERO);
            distributorInfoVO.setPromotionActivitys(ListUtil.empty());
            distributorInfoVO.setOrderType(OrderTypeEnum.POP.getCode());
            distributorInfoVO.setIsHasTask(false);
            return distributorInfoVO;
        }).collect(Collectors.toList());
        listPage.setRecords(resultList);

        return Result.success(listPage);
    }

    /**
     * 查询促销活动信息
     *
     * @param eidList
     * @return
     */
    private Map<Long, List<SimpleActivityVO>> selectPromotionList(List<Long> eidList) {
        // 查询满赠活动信息
        PromotionEnterpriseRequest promotionEnterpriseRequest = new PromotionEnterpriseRequest();
        promotionEnterpriseRequest.setPlatform(PlatformEnum.SALES_ASSIST.getCode());
        promotionEnterpriseRequest.setEIdList(eidList);
        List<PromotionAppListDTO> promotionAppListList = promotionActivityApi.queryEnterprisePromotion(promotionEnterpriseRequest);

        if (log.isDebugEnabled()) {

            log.debug("...queryEnterprisePromotion...参数:{},返回结果:{}", eidList, promotionAppListList);
        }

        if (CollectionUtil.isEmpty(promotionAppListList)) {
            return Collections.emptyMap();
        }
        Map<Long, List<SimpleActivityVO>> distributorPromotionMap = Maps.newHashMap();
        promotionAppListList.forEach(t -> {
            SimpleActivityVO vo = PojoUtils.map(t, SimpleActivityVO.class);
            vo.setName(t.getPromotionName());
            distributorPromotionMap.putIfAbsent(t.getEid(), new ArrayList<SimpleActivityVO>());
            distributorPromotionMap.get(t.getEid()).add(vo);
        });

        return distributorPromotionMap;
    }

    /**
     * b2b配送商商品查询
     *
     * @param distributorPageForm
     * @return
     */
    private Result<Page<DistributorInfoVO>> b2bDistributorSearch(CurrentStaffInfo staffInfo, QueryDistributorPageForm distributorPageForm) {
        // 如果是B2B调用B2B查询接口
        QueryShopRequest request = PojoUtils.map(distributorPageForm, QueryShopRequest.class);
        //新增需求：只展示当前登录人可以购买的店铺（根据店铺销售区域进行判断）
        //根据当前登录企业的区域信息，获取可以买哪些企业的接口
        List<Long> couldBuyEidList = shopApi.getSellEidByEidSaleArea(distributorPageForm.getPurchaseEid());
        if (CollUtil.isEmpty(couldBuyEidList)) {
            return Result.success(new Page<>());
        }
        Map<Long, Boolean> enterpriseCustomerLineApiCustomerLineListFlagMap = enterpriseCustomerLineApi.getCustomerLineListFlag(couldBuyEidList.stream().distinct().collect(Collectors.toList()), distributorPageForm.getPurchaseEid(), EnterpriseCustomerLineEnum.B2B);
        if (CollectionUtil.isEmpty(enterpriseCustomerLineApiCustomerLineListFlagMap)) {
            return Result.success(new Page<>());
        }
        // 过滤出已建采客户列表
        List<Long> customerEidList = Lists.newArrayList();
        enterpriseCustomerLineApiCustomerLineListFlagMap.forEach((k, v) -> {
            if (v) {
                customerEidList.add(k);
            }
        });
        //查询企业商品信息，并且过滤没有商品的企业店铺/shop/queryShopListPage
        request.setShopEidList(customerEidList);

        // 当前企业均为建立采购关系
        if (CollectionUtil.isEmpty(customerEidList)) {

            return Result.success(new Page<>());
        }
        Page<ShopListItemDTO> dtoPage = shopApi.queryShopListPage(request);
        if (dtoPage == null || CollectionUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(new Page<>(1, distributorPageForm.getSize()));
        }

        // 查询配送商是否有参与任务
        QueryTaskDistributorRequest queryTaskDistributorRequest = new QueryTaskDistributorRequest();
        queryTaskDistributorRequest.setUserId(staffInfo.getCurrentUserId());
        queryTaskDistributorRequest.setEidList(dtoPage.getRecords().stream().map(t -> t.getShopEid()).collect(Collectors.toList()));

        List<Long> taskEidList = taskApi.queryDistributorByEidList(queryTaskDistributorRequest);

        if (log.isDebugEnabled()) {
            log.debug("调用任务接口:queryDistributorByEidList..入参:{},返回参数:{}", queryTaskDistributorRequest, taskEidList);
        }

        // 店铺企业Eid
        List<Long> shopEidList = dtoPage.getRecords().stream().map(t -> t.getShopEid()).distinct().collect(Collectors.toList());
        Map<Long, List<SimpleActivityVO>> distributorPromotionMap = this.selectPromotionList(shopEidList);
        Page<DistributorInfoVO> pageResult = new Page<DistributorInfoVO>();
        pageResult.setTotal(dtoPage.getTotal());
        pageResult.setCurrent(dtoPage.getCurrent());
        pageResult.setSize(dtoPage.getSize());
        Map<Long, EnterpriseGoodsCountBO> shopGoodsCountMap = b2bGoodsApi.getGoodsCountByEidList(shopEidList);

        List<DistributorInfoVO> resultList = dtoPage.getRecords().stream().map(t -> {
            DistributorInfoVO infoVO = new DistributorInfoVO();
            infoVO.setName(t.getShopName());
            infoVO.setPurchaseEid(distributorPageForm.getPurchaseEid());
            infoVO.setDistributorEid(t.getShopEid());
            infoVO.setLogo(StringUtils.isBlank(t.getShopLogo()) ? SHOP_LOGO_DEFAULT_PIC : t.getShopLogo());
            // 查询全品
            EnterpriseGoodsCountBO enterpriseGoodsCountBO = Optional.ofNullable(shopGoodsCountMap.get(t.getShopEid())).orElse(new EnterpriseGoodsCountBO(0l,0l,0l));
            infoVO.setGoodsKind(enterpriseGoodsCountBO.getStandardCount());
            infoVO.setStartAmount(t.getStartAmount());
            infoVO.setGoodStandards(enterpriseGoodsCountBO.getSellSpecificationCount());
            infoVO.setPromotionActivitys(distributorPromotionMap.getOrDefault(t.getShopEid(), ListUtil.empty()));
            infoVO.setOrderType(OrderTypeEnum.B2B.getCode());
            // 是否有参与任务品
            Boolean isHasTask = Optional.ofNullable(taskEidList).map(z -> taskEidList.contains(t.getShopEid())).orElse(false);
            infoVO.setIsHasTask(isHasTask);

            return infoVO;
        }).collect(Collectors.toList());

        pageResult.setRecords(resultList);

        return Result.success(pageResult);
    }


}
