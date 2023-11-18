package com.yiling.admin.b2b.goods.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.b2b.goods.form.BatchUpdateGoodsStatusForm;
import com.yiling.admin.b2b.goods.form.ImportGoodsForm;
import com.yiling.admin.b2b.goods.form.QueryB2bGoodsPageListForm;
import com.yiling.admin.b2b.goods.form.QueryGoodsPageListForm;
import com.yiling.admin.b2b.goods.form.UpdateGoodsForm;
import com.yiling.admin.b2b.goods.handler.ImportGoodsDataHandler;
import com.yiling.admin.b2b.goods.handler.ImportGoodsVerifyHandler;
import com.yiling.admin.b2b.goods.vo.EnterpriseGoodsListVO;
import com.yiling.admin.b2b.goods.vo.GoodsDetailsVO;
import com.yiling.admin.b2b.goods.vo.GoodsDisableVO;
import com.yiling.admin.b2b.goods.vo.GoodsListItemPageVO;
import com.yiling.admin.b2b.goods.vo.GoodsListItemVO;
import com.yiling.admin.b2b.goods.vo.GoodsSkuVO;
import com.yiling.admin.b2b.goods.vo.InventoryDetailVO;
import com.yiling.admin.b2b.goods.vo.QueryEnterpriseGoodsPageListForm;
import com.yiling.admin.b2b.goods.vo.StandardGoodsAllInfoVO;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.vo.ImportResultVO;
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
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@RestController
@Api(tags = "商品管理模块-运营后台")
@RequestMapping("/b2b/goods")
@Slf4j
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
    @Autowired
    ImportGoodsVerifyHandler goodsImportVerifyHandler;
    @Autowired
    ImportGoodsDataHandler goodsImportDataHandler;
    @DubboReference
    GoodsLimitPriceApi goodsLimitPriceApi;

    @ApiOperation(value = "管理后台商品信息更新导入", httpMethod = "POST")
    @PostMapping(value = "importUpdateGoods", headers = "content-type=multipart/form-data")
    @Log(title = "管理后台B2B商品修改导入", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importUpdateGoods(@RequestParam(value = "file", required = true) MultipartFile file, @CurrentUser CurrentAdminInfo adminInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(true);
        params.setSaveUrl(ExeclImportUtils.EXECL_PATH);
        params.setNeedVerify(true);
        params.setVerifyHandler(goodsImportVerifyHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            //包含了插入数据库失败的信息
            Long start = System.currentTimeMillis();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportGoodsForm.class, params, goodsImportDataHandler, paramMap);
            log.info("商品导入耗时：{}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "b2b后台商品弹框查询-运营后台", httpMethod = "POST")
    @PostMapping("/b2bList")
    public Result<Page<GoodsListItemVO>> b2bList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryB2bGoodsPageListForm form) {
        // 新增三个限制。是否以岭品，是否上下架，库存不限制，可以为0
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (CollUtil.isNotEmpty(form.getEidList())) {
            request.setEidList(form.getEidList());
        }
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setName(form.getGoodsName());
        // 0表示全部商品不限制状态
        if (request.getGoodsStatus() == 0) {
            request.setGoodsStatus(null);
        }
        if (!Integer.valueOf(0).equals(form.getYilingGoodsFlag())) {
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
        List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIdList);
        List<GoodsSkuDTO> b2bGoodsSkuList = goodsSkuDTOList.stream().filter(sku -> GoodsLineEnum.B2B.getCode().equals(sku.getGoodsLine())).collect(Collectors.toList());
        Map<Long, List<GoodsSkuDTO>> goodsSkuDTOMap = b2bGoodsSkuList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
        Map<Long, List<PromotionGoodsLimitDTO>> longListMap = MapUtil.newHashMap();
        {
            if (Integer.valueOf(1).equals(form.getFrom())) {
                Integer sponsorType = form.getSponsorType();
                if (Objects.isNull(sponsorType)) {
                    return Result.failed("请先选择活动分类");
                }
                // 活动分类（1-平台活动；2-商家活动）
                List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryNotRepeatByGoodsIdList(goodsIdList, sponsorType, form.getType());
                longListMap = promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));
            }
        }

        //List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 先判断产品类型（是否以岭品，在判断以岭品是否已经限价）
        // key=goodsId value=0(不是以岭品，大于0就是以岭商品ID)
        if (PromotionTypeEnum.isCombinationPackage(form.getType())) {
            form.setCustomerEid(form.getEidList().get(0));
        }
        List<Long> limitGoodsIdsList = ListUtil.empty();
        {
            if (form.getCustomerEid() != null && 0 != form.getCustomerEid()) {
                limitGoodsIdsList = goodsLimitPriceApi.getGoodsIdsByCustomerEid(Constants.YILING_EID, form.getCustomerEid());
            }
        }
        List<Long> finalLimitGoodsIdsList = limitGoodsIdsList;
        Map<Long, List<PromotionGoodsLimitDTO>> finalLongListMap = longListMap;
        page.getRecords().forEach(e -> {
            // 是否以岭品赋值
            GoodsDisableVO goodsDisableVO = new GoodsDisableVO();

            List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = finalLongListMap.get(e.getId());
            goodsDisableVO.setIsAllowSelect(0);
            if (null != promotionGoodsLimitDTOS && promotionGoodsLimitDTOS.size() > 0) {
                // 组合包商品可以重复添加，秒杀特价不能
                PromotionGoodsLimitDTO promotionGoodsLimitDTO = promotionGoodsLimitDTOS.get(0);
                if (!PromotionTypeEnum.isCombinationPackage(form.getType())) {
                    goodsDisableVO.setIsAllowSelect(1);
                    //如果是秒杀特价但是之前的商品添加的是组合包活动，还可以重复添加
                }
                if (null != form.getPromotionActivityId() && form.getPromotionActivityId().equals(promotionGoodsLimitDTO.getPromotionActivityId())) {
                    goodsDisableVO.setIsAllowSelect(0);
                }
            }

            e.setGoodsDisableVO(goodsDisableVO);
            e.setGoodsName(e.getName());
            e.setGoodsId(e.getId());
            e.setGoodsSkuList(PojoUtils.map(goodsSkuDTOMap.get(e.getId()), GoodsSkuVO.class));
            Long count = longMap.get(e.getId());
            if (PromotionTypeEnum.isCombinationPackage(form.getType())) {
                e.setCount(null);
            } else {
                e.setCount(count);
            }
        });

        return Result.success(page);
    }

    @ApiOperation(value = "管理后台供应商查询", httpMethod = "POST")
    @PostMapping("/enterpriseList")
    public Result<Page<EnterpriseGoodsListVO>> enterpriseList(@RequestBody @Valid QueryEnterpriseGoodsPageListForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        request.setMallFlag(1);
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        Page<EnterpriseGoodsListVO> pageVO = PojoUtils.map(page, EnterpriseGoodsListVO.class);
        pageVO.getRecords().forEach(e -> {
            List<QueryStatusCountBO> list = b2bGoodsApi.queryB2bStatusCountList(Arrays.asList(e.getId()));
            Map<Integer, Long> map = list.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            e.setUpShelfCount(map.get(GoodsStatusEnum.UP_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UP_SHELF.getCode()));
            e.setUnShelfCount(map.get(GoodsStatusEnum.UN_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UN_SHELF.getCode()));
            e.setWaitSetCount(map.get(GoodsStatusEnum.WAIT_SET.getCode()) == null ? 0L : map.get(GoodsStatusEnum.WAIT_SET.getCode()));
        });
        return Result.success(pageVO);
    }


    @ApiOperation(value = "商品列表", httpMethod = "POST")
    @PostMapping("/list")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> list(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(Arrays.asList(form.getEid()));
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
    public Result<GoodsDetailsVO> detail(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam Long goodsId) {
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

            //获取上下状态
            B2bGoodsDTO b2bGoodsDTO = b2bGoodsApi.getB2bGoodsByGoodsId(goodsId);
            resultData.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
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
    public Result<Boolean> updateStatus(@RequestBody @Valid BatchUpdateGoodsStatusForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(form.getGoodsIds());
        // 验证是否是自己的商业公司商品
        List<Long> goodsEidList = goodsInfoDTOList.stream().map(GoodsInfoDTO::getEid).distinct().collect(Collectors.toList());
        if (goodsEidList == null) {
            return Result.failed("选择了无效的商品");
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
        request.setOpUserId(adminInfo.getCurrentUserId());


        // 上架
        if (GoodsStatusEnum.UP_SHELF.getCode().equals(form.getGoodsStatus())) {
            // 先检查商品是否是自己下架的
            for (B2bGoodsDTO goodsInfo : b2bGoodsDTOList) {
                if (!GoodsOutReasonEnum.QUALITY_CONTROL.getCode().equals(goodsInfo.getOutReason())) {
                    return Result.failed("上架商品存在[" + GoodsOutReasonEnum.getByCode(goodsInfo.getOutReason()).getName() + "]");
                }
            }
        }
        request.setOutReason(GoodsOutReasonEnum.QUALITY_CONTROL.getCode());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        return Result.success(goodsApi.batchUpdateGoodsStatus(request));
    }

    @ApiOperation(value = "编辑商品", httpMethod = "POST")
    @PostMapping("/update")
    @Log(title = "编辑商品", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> update(@RequestBody @Valid UpdateGoodsForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        SaveOrUpdateGoodsRequest request = PojoUtils.map(form, SaveOrUpdateGoodsRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        SaveGoodsLineRequest goodsLineInfo = new SaveGoodsLineRequest();
        goodsLineInfo.setMallFlag(1);
        request.setGoodsLineInfo(goodsLineInfo);

        //查询原始数据
        GoodsInfoDTO goodsInfoDTO = b2bGoodsApi.queryInfo(form.getGoodsId());

        //判断商品是否已经在平台上禁止销售
        if (goodsInfoDTO.getStandardId() != null && goodsInfoDTO.getStandardId() != 0) {
            StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(goodsInfoDTO.getStandardId());
            if (standardGoodsByIdInfoDTO == null || standardGoodsByIdInfoDTO.getBaseInfo().getGoodsStatus().equals(StandardGoodsStatusEnum.FORBID.getCode())) {
                return Result.failed("该商品已经在平台禁止销售");
            }
        }
        request.setOutReason(GoodsOutReasonEnum.REJECT.getCode());
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
