package com.yiling.b2b.admin.goods.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.b2b.admin.goods.form.BatchUpdateGoodsStatusForm;
import com.yiling.b2b.admin.goods.form.QueryB2bGoodsPageListForm;
import com.yiling.b2b.admin.goods.form.QueryGoodsPageListForm;
import com.yiling.b2b.admin.goods.form.UpdateGoodsForm;
import com.yiling.b2b.admin.goods.form.UpdateGoodsSkuForm;
import com.yiling.b2b.admin.goods.vo.GoodsDetailsVO;
import com.yiling.b2b.admin.goods.vo.GoodsDisableVO;
import com.yiling.b2b.admin.goods.vo.GoodsListItemPageVO;
import com.yiling.b2b.admin.goods.vo.GoodsListItemVO;
import com.yiling.b2b.admin.goods.vo.GoodsSkuVO;
import com.yiling.b2b.admin.goods.vo.InventoryDetailVO;
import com.yiling.b2b.admin.goods.vo.StandardGoodsAllInfoVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsAuditApi;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.request.BatchUpdateGoodsStatusRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsCategoryApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.enums.StandardGoodsStatusEnum;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.enums.PromotionSponsorTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@RestController
@Api(tags = "商品管理模块")
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    GoodsAuditApi goodsAuditApi;
    @DubboReference
    InventoryApi inventoryApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    StandardGoodsApi standardGoodsApi;
    @DubboReference
    AgreementGoodsApi agreementGoodsApi;
    @DubboReference
    StandardGoodsCategoryApi standardGoodsCategoryApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    GoodsLimitPriceApi goodsLimitPriceApi;

    @ApiOperation(value = "b2b后台商品弹框查询", httpMethod = "POST")
    @PostMapping("/b2bList")
    public Result<Page<GoodsListItemVO>> b2bList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryB2bGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (CollUtil.isNotEmpty(form.getEidList())) {
            request.setEidList(form.getEidList());
        } else {
            if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
                List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
                request.setEidList(list);
            } else {
                request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
            }
        }
        // 0表示全部库存，没有库存的也展示.这个接口也被除去满赠以外的活动使用，所以要参数前端控制
        // 商家后台只能创建满赠促销活动
        request.setIsAvailableQty(0);
        if(null!=request.getGoodsStatus()&& request.getGoodsStatus()==0){
            request.setGoodsStatus(null);
        }
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setName(form.getGoodsName());
        if (!Integer.valueOf(0).equals(request.getYilingGoodsFlag())) {
            request.setIncludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
        }
        Page<GoodsListItemVO> page = PojoUtils.map(b2bGoodsApi.queryB2bGoodsPageList(request), GoodsListItemVO.class);

        List<Long> goodsIdList = page.getRecords().stream().map(GoodsListItemVO::getId).collect(Collectors.toList());
        if (goodsIdList.size() < 1) {
            return Result.success(page);
        }
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, enterpriseApi.listSubEids(Constants.YILING_EID));
        page.getRecords().stream().forEach(item->{
            item.setYilingGoodsFlag(false);
            if(goodsMap.get(item.getId())!=null&&goodsMap.get(item.getId())>0){
                item.setYilingGoodsFlag(true);
            }
        });

        Map<Long, Long> longMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);

        Map<Long, List<PromotionGoodsLimitDTO>> longListMap = MapUtil.newHashMap();
        {
            if (Integer.valueOf(1).equals(form.getFrom())) {
                List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryNotRepeatByGoodsIdList(goodsIdList, PromotionSponsorTypeEnum.MERCHANT.getType(), PromotionTypeEnum.FULL_GIFT.getType());
                longListMap = promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));
            }
        }

        List<Long> limitGoodsIdsList = ListUtil.empty();
        {
            if (form.getCustomerEid() != null && 0 != form.getCustomerEid()) {
                limitGoodsIdsList = goodsLimitPriceApi.getGoodsIdsByCustomerEid(Constants.YILING_EID, form.getCustomerEid());
            }
        }


        Map<Long, List<PromotionGoodsLimitDTO>> finalLongListMap = longListMap;
        List<Long> finalLimitGoodsIdsList = limitGoodsIdsList;
        page.getRecords().forEach(e -> {
            GoodsDisableVO goodsDisableVO = new GoodsDisableVO();
            goodsDisableVO.setLimitDisable(false);

            List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = finalLongListMap.get(e.getId());
            goodsDisableVO.setIsAllowSelect(0);
            if (null != promotionGoodsLimitDTOS && promotionGoodsLimitDTOS.size() > 0) {
                goodsDisableVO.setIsAllowSelect(1);
                PromotionGoodsLimitDTO promotionGoodsLimitDTO = promotionGoodsLimitDTOS.get(0);
                if (null != form.getPromotionActivityId() && form.getPromotionActivityId().equals(promotionGoodsLimitDTO.getPromotionActivityId())) {
                    goodsDisableVO.setIsAllowSelect(0);
                }
            }

            //判断限价是否占用
            if (finalLimitGoodsIdsList.contains(e.getId())) {
                goodsDisableVO.setLimitDisable(true);
                goodsDisableVO.setLimitDesc("已占用");
            }

            e.setGoodsDisableVO(goodsDisableVO);
            e.setGoodsName(e.getName());
            e.setGoodsId(e.getId());
            Long count = longMap.get(e.getId());
            e.setCount(count);
        });
        return Result.success(page);
    }

    @ApiOperation(value = "商品列表", httpMethod = "POST")
    @PostMapping("/list")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> list(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(list);
        } else {
            request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        }
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> page = b2bGoodsApi.queryB2bGoodsPageList(request);
        GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);
            List<Long> goodsIds = goodsListItemVOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
            Map<Long, List<GoodsSkuDTO>> goodsSkuMap = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
            goodsListItemVOList.forEach(e -> {
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                e.setGoodsSkuList(PojoUtils.map(goodsSkuMap.get(e.getId()), GoodsSkuVO.class));
            });

            newPage.setRecords(goodsListItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());

            //统计商品需要 排除状态条件
            request.setGoodsStatus(null);
            List<QueryStatusCountBO> listByCondition = b2bGoodsApi.queryB2bStatusCountListByCondition(request);
            Map<Integer, Long> map = listByCondition.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            newPage.setUpShelfCount(map.get(GoodsStatusEnum.UP_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UP_SHELF.getCode()));
            newPage.setUnShelfCount(map.get(GoodsStatusEnum.UN_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UN_SHELF.getCode()));
            newPage.setWaitSetCount(map.get(GoodsStatusEnum.WAIT_SET.getCode()) == null ? 0L : map.get(GoodsStatusEnum.WAIT_SET.getCode()));
        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "商品明细", httpMethod = "GET")
    @GetMapping("/detail")
    public Result<GoodsDetailsVO> detail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long goodsId) {
        GoodsDetailsVO resultData = null;
        GoodsFullDTO goodsFullDTO = goodsApi.queryFullInfo(goodsId);
        if (goodsFullDTO != null) {
            resultData = PojoUtils.map(goodsFullDTO, GoodsDetailsVO.class);
            //图片转换
            if (resultData != null && resultData.getStandardGoodsAllInfo() != null) {

                StandardGoodsAllInfoVO standardGoodsAllInfoVO = resultData.getStandardGoodsAllInfo();
                if (CollUtil.isNotEmpty(standardGoodsAllInfoVO.getPicBasicsInfoList())) {
                    standardGoodsAllInfoVO.getPicBasicsInfoList().forEach(e -> {
                        e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                    });
                }
            }

            //获取销售包装和库存信息
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIdAndStatus(goodsId,ListUtil.toList(GoodsSkuStatusEnum.NORMAL.getCode(),GoodsSkuStatusEnum.HIDE.getCode()));
            goodsSkuDTOList = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.B2B.getCode())).collect(Collectors.toList());
            resultData.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));
            resultData.getGoodsSkuList().forEach(skuVO->{
                if(GoodsSkuStatusEnum.HIDE.getCode().equals(skuVO.getStatus())){
                    skuVO.setHideFlag(true);
                }else {
                    skuVO.setHideFlag(false);
                }
            });
            //获取上下状态
            B2bGoodsDTO b2bGoodsDTO = b2bGoodsApi.getB2bGoodsByGoodsId(goodsId);
            resultData.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());

            resultData.setPicUrl(pictureUrlUtils.getGoodsPicUrl(resultData.getPic()));
            return Result.success(resultData);
        } else {
            return Result.failed("商品不存在");
        }
    }


    /**
     * 批量上下架商品
     *
     * @param form
     * @return 对象主键
     */
    @ApiOperation(value = "批量上下架商品", notes = "批量上下架商品", httpMethod = "POST")
    @PostMapping(path = "/updateStatus")
    @Log(title = "批量上下架商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateStatus(@RequestBody @Valid BatchUpdateGoodsStatusForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = ListUtil.empty();
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
        }

        List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(form.getGoodsIds());
        // 验证是否是自己的商业公司商品
        List<Long> goodsEidList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getEid).distinct().collect(Collectors.toList());
        if (goodsEidList == null) {
            return Result.failed("选择了无效的商品");
        }

        for (Long eid : goodsEidList) {
            if (!eidList.contains(eid)) {
                return Result.failed("只能修改自己商业公司里面的商品信息");
            }
        }

        List<B2bGoodsDTO> b2bGoodsDTOList = b2bGoodsApi.getB2bGoodsListByGoodsIds(form.getGoodsIds());
        List<Integer> goodsStatusList = b2bGoodsDTOList.stream().map(B2bGoodsDTO::getGoodsStatus).distinct().collect(Collectors.toList());

        for (B2bGoodsDTO b2bGoodsDTO : b2bGoodsDTOList) {
            if (!goodsApi.isWaitSetGoodsStatus(GoodsLineEnum.B2B.getCode(), b2bGoodsDTO.getGoodsId())) {
                return Result.failed("有商品没有设置库存和价格，不能上下架操作");
            }
        }

        if (!Arrays.asList(GoodsStatusEnum.UP_SHELF.getCode(), GoodsStatusEnum.UN_SHELF.getCode()).containsAll(goodsStatusList)) {
            return Result.failed("商品状态不符合上下架状态操作");
        }

        BatchUpdateGoodsStatusRequest request = PojoUtils.map(form, BatchUpdateGoodsStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentEid());


        // 上架
        if (GoodsStatusEnum.UP_SHELF.getCode().equals(form.getGoodsStatus())) {
            // 先检查商品是否是自己下架的
            for (B2bGoodsDTO goodsInfo : b2bGoodsDTOList) {
                if (!GoodsOutReasonEnum.REJECT.getCode().equals(goodsInfo.getOutReason())) {
                    return Result.failed("上架商品存在[" + GoodsOutReasonEnum.getByCode(goodsInfo.getOutReason()).getName() + "]");
                }
            }
        }
        request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        return Result.success(goodsApi.batchUpdateGoodsStatus(request));
    }

    @ApiOperation(value = "编辑商品", httpMethod = "POST")
    @PostMapping("/update")
    @Log(title = "编辑商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> update(@RequestBody @Valid UpdateGoodsForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        form.getGoodsSkuList().forEach(skuForm->{
            if(skuForm.getHideFlag()!=null && skuForm.getHideFlag()){
                skuForm.setStatus(GoodsSkuStatusEnum.HIDE.getCode());
            }else {
                skuForm.setStatus(GoodsSkuStatusEnum.NORMAL.getCode());
            }
        });
        SaveOrUpdateGoodsRequest request = PojoUtils.map(form, SaveOrUpdateGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        SaveGoodsLineRequest goodsLineInfo = new SaveGoodsLineRequest();
        goodsLineInfo.setMallFlag(1);
        request.setGoodsLineInfo(goodsLineInfo);
        request.setId(form.getGoodsId());
        List<Long> eidList = ListUtil.empty();
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
        }

        //查询原始数据
        GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(form.getGoodsId());

        //只能修改自己的商品
        if (!eidList.contains(goodsInfoDTO.getEid())) {
            return Result.failed("只能修改自己的商品信息");
        }

        //判断商品是否已经在平台上禁止销售
        if (goodsInfoDTO.getStandardId() != null && goodsInfoDTO.getStandardId() != 0) {
            StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(goodsInfoDTO.getStandardId());
            if (standardGoodsByIdInfoDTO == null || standardGoodsByIdInfoDTO.getBaseInfo().getGoodsStatus().equals(StandardGoodsStatusEnum.FORBID.getCode())) {
                return Result.failed("该商品已经在平台禁止销售");
            }
        }
        if (form.getGoodsStatus() != null) {
            //验证上下架状态
            //判断当前商品状态是否可编辑商品
            if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsInfoDTO.getAuditStatus())) {
                return Result.failed("该商品还没有审核通过不能编辑");
            }
            //如果是上架，修改下架原因0
            if (GoodsStatusEnum.UP_SHELF.getCode().equals(form.getGoodsStatus())) {
                if (goodsInfoDTO.getOutReason().equals(GoodsOutReasonEnum.PLATFORM.getCode())) {
                    return Result.failed("该商品已经在平台禁止销售");
                }
                if (goodsInfoDTO.getOutReason().equals(GoodsOutReasonEnum.QUALITY_CONTROL.getCode())) {
                    return Result.failed("该商品已经被质管禁止销售");
                }
            }
            //如果是下架架，修改下架原因2
            if (!Arrays.asList(GoodsStatusEnum.UNDER_REVIEW.getCode(), GoodsStatusEnum.REJECT.getCode()).contains(goodsInfoDTO.getAuditStatus())) {
                request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
            }
        }

        //判断商品内码是否有空格存在和重复的情况
        if (CollUtil.isNotEmpty(form.getGoodsSkuList())) {
            Set<String> inSnSet = new HashSet<>();
            for (UpdateGoodsSkuForm updateGoodsSkuForm : form.getGoodsSkuList()) {
                if (StrUtil.isNotEmpty(updateGoodsSkuForm.getInSn())) {
                    if (StrUtil.trim(updateGoodsSkuForm.getInSn()).length() != updateGoodsSkuForm.getInSn().length()) {
                        return Result.failed("商品内码前后不能空格");
                    }
                    if (!inSnSet.contains(updateGoodsSkuForm.getInSn())) {
                        inSnSet.add(updateGoodsSkuForm.getInSn());
                    } else {
                        return Result.failed("供应商商品内码已经存在");
                    }
                }
            }
        }

        request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
        request.setEid(goodsInfoDTO.getEid());
        //更新商品
        return Result.success(goodsApi.editGoods(request) > 0);
    }

    @ApiOperation(value = "库存明细", httpMethod = "GET")
    @GetMapping("/getInventoryDetail")
    public Result<List<InventoryDetailVO>> getInventoryDetail(@RequestParam("skuId")Long skuId){
        InventoryDTO inventoryDTO = inventoryApi.getBySkuId(skuId);
        if(null==inventoryDTO){
            return Result.failed("库存信息不存在");
        }
        List<InventorySubscriptionDTO> subscriptionDTOList = goodsApi.getInventoryDetailByInventoryId(inventoryDTO.getId());
        List<InventoryDetailVO> detailVOS = Lists.newLinkedList();
        if(CollectionUtil.isNotEmpty(subscriptionDTOList)){
            subscriptionDTOList.forEach(subscription->{
                InventoryDetailVO detailVO = new InventoryDetailVO();
                detailVO.setSubscriptionEid(subscription.getSubscriptionEid());
                detailVO.setSubscriptionEname(subscription.getSubscriptionEname());
                detailVO.setQty(subscription.getQty());
                detailVO.setSourceInSn(inventoryDTO.getInSn());
                detailVO.setInSn(subscription.getInSn());
                detailVOS.add(detailVO);
            });
        }
        return Result.success(detailVOS);
    }

}
