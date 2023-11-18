package com.yiling.admin.b2b.promotion.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.b2b.coupon.from.QueryCouponActivityEnterpriseFrom;
import com.yiling.admin.b2b.coupon.vo.CouponActivityEnterprisePageVO;
import com.yiling.admin.b2b.goods.vo.GoodsDisableVO;
import com.yiling.admin.b2b.goods.vo.GoodsListItemVO;
import com.yiling.admin.b2b.goods.vo.GoodsSkuVO;
import com.yiling.admin.b2b.promotion.form.ImportPromotionGoodsForm;
import com.yiling.admin.b2b.promotion.form.PromotionActivityPageForm;
import com.yiling.admin.b2b.promotion.form.PromotionActivityStatusForm;
import com.yiling.admin.b2b.promotion.form.PromotionGoodsGiftUsedForm;
import com.yiling.admin.b2b.promotion.form.PromotionGoodsLimitSaveForm;
import com.yiling.admin.b2b.promotion.form.PromotionImportGoodsForm;
import com.yiling.admin.b2b.promotion.form.PromotionSaveForm;
import com.yiling.admin.b2b.promotion.handler.ImportPromotionGoodsDataHandler;
import com.yiling.admin.b2b.promotion.handler.PromotionAreaUtil;
import com.yiling.admin.b2b.promotion.vo.PromotionActivityPageVO;
import com.yiling.admin.b2b.promotion.vo.PromotionActivityVO;
import com.yiling.admin.b2b.promotion.vo.PromotionCombinationPackageVO;
import com.yiling.admin.b2b.promotion.vo.PromotionEnterpriseLimitVO;
import com.yiling.admin.b2b.promotion.vo.PromotionGoodsGiftLimitVO;
import com.yiling.admin.b2b.promotion.vo.PromotionGoodsGiftUsedVO;
import com.yiling.admin.b2b.promotion.vo.PromotionSecKillSpecialVO;
import com.yiling.admin.b2b.promotion.vo.PromotionVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.api.PromotionGoodsGiftLimitApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionEnterpriseLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionSecKillSpecialDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.enums.PromotionEffectTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionMerchantTypeEnum;
import com.yiling.marketing.promotion.enums.PromotionPermittedTypeCode;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseClassEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 促销活动主表 前端控制器
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Slf4j
@Api(tags = "促销活动管理接口-运营后台")
@RestController
@RequestMapping("/promotion/activity")
public class PromotionActivityController extends BaseController {
    @DubboReference
    PromotionActivityApi            promotionActivityApi;
    @DubboReference
    PromotionGoodsGiftLimitApi      giftLimitApi;
    @DubboReference
    B2bGoodsApi                     b2bGoodsApi;
    @DubboReference
    InventoryApi                    inventoryApi;
    @Autowired
    ImportPromotionGoodsDataHandler importPromotionGoodsDataHandler;
    @DubboReference
    EnterpriseApi                   enterpriseApi;
    @DubboReference
    GoodsApi                        goodsApi;
    @DubboReference
    UserApi                         userApi;

    @ApiOperation(value = "分页列表查询B2B中促销活动-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PromotionActivityPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionActivityPageForm form) {
        PromotionActivityPageRequest request = PojoUtils.map(form, PromotionActivityPageRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());

        Page<PromotionActivityPageDTO> pageDTO = promotionActivityApi.pageList(request);
        pageDTO.getRecords().forEach(item -> {
            if (StringUtils.isNotBlank(item.getPlatformSelected())) {
                List<Integer> platformSelectedList = JSON.parseArray(item.getPlatformSelected(), Integer.class);
                item.setPlatformSelectedList(platformSelectedList);
            }
        });
        Page<PromotionActivityPageVO> pageVO = PojoUtils.map(pageDTO, PromotionActivityPageVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "编辑和查询详情-运营后台")
    @GetMapping("/queryById")
    public Result<PromotionVO> queryById(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "id") Long id) {
        // 促销活动主表
        PromotionActivityDTO promotionActivityDTO = promotionActivityApi.queryById(id);
        if (Objects.isNull(promotionActivityDTO)) {
            return Result.failed("未获取到活动信息");
        }
        PromotionVO vo = new PromotionVO();
        PromotionActivityVO activityVO = PojoUtils.map(promotionActivityDTO, PromotionActivityVO.class);
        if (StringUtils.isNotBlank(promotionActivityDTO.getPlatformSelected())) {
            List<Integer> platformSelectedList = JSON.parseArray(promotionActivityDTO.getPlatformSelected(), Integer.class);
            activityVO.setPlatformSelected(platformSelectedList);
        }

        // 如果秒杀&特价 & 立即生效 -> 计算持续时间
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(promotionActivityDTO.getType())
            && PromotionEffectTypeEnum.NOW.getCode().equals(promotionActivityDTO.getEffectType())) {
            int lastTime = CouponUtil.daysBetween2(promotionActivityDTO.getBeginTime(), promotionActivityDTO.getEndTime());
            activityVO.setLastTime(lastTime);
        }

        vo.setPromotionActivity(activityVO);
        // 促销活动企业限制表
        List<PromotionEnterpriseLimitDTO> promotionEnterpriseLimitDTOList = promotionActivityApi.queryEnterpriseByActivityId(id);
        List<PromotionEnterpriseLimitVO> enterpriseLimitVOS = PojoUtils.map(promotionEnterpriseLimitDTOList, PromotionEnterpriseLimitVO.class);
        vo.setPromotionEnterpriseLimitList(enterpriseLimitVOS);

        // 秒杀&特价 -> 秒杀&特价表
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(promotionActivityDTO.getType())) {
            PromotionSecKillSpecialDTO secKillSpecialDTO = promotionActivityApi.querySecKillSpecialByActivityId(id);
            PromotionSecKillSpecialVO secKillSpecialVO = PojoUtils.map(secKillSpecialDTO, PromotionSecKillSpecialVO.class);

            if (PromotionPermittedTypeCode.PART.getCode().equals(secKillSpecialDTO.getPermittedAreaType())) {
                String description = PromotionAreaUtil.getDescription(secKillSpecialDTO.getPermittedAreaDetail());
                secKillSpecialVO.setPermittedAreaDetailDescription(description);
            }

            if (PromotionPermittedTypeCode.PART.getCode().equals(secKillSpecialDTO.getPermittedEnterpriseType())) {
                List<Integer> permittedEnterpriseDetailList = JSON.parseArray(secKillSpecialDTO.getPermittedEnterpriseDetail(), Integer.class);
                secKillSpecialVO.setPermittedEnterpriseDetail(permittedEnterpriseDetailList);
            }
            vo.setPromotionSecKillSpecial(secKillSpecialVO);

        }
        // 满赠 -> 促销活动赠品表
        if (PromotionTypeEnum.isFullGift(promotionActivityDTO.getType())) {
            List<PromotionGoodsGiftLimitDTO> promotionGoodsGiftLimitDTO = promotionActivityApi.queryGoodsGiftByActivityId(id);
            List<PromotionGoodsGiftLimitVO> goodsGiftLimitVO = PojoUtils.map(promotionGoodsGiftLimitDTO, PromotionGoodsGiftLimitVO.class);
            vo.setPromotionGoodsGiftLimitList(goodsGiftLimitVO);
        }
        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryGoodsByActivityId(id);
        //用skuid分组
        Map<Long, PromotionGoodsLimitDTO> goodSkuLimitDTOMap = new HashMap<>();
        if (PromotionTypeEnum.COMBINATION_PACKAGE.getType().equals(promotionActivityDTO.getType())) {
            goodSkuLimitDTOMap = promotionGoodsLimitDTOList.stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsSkuId, Function.identity()));
        }
        // 组合包 -> 组合包信息表
        if (PromotionTypeEnum.isCombinationPackage(promotionActivityDTO.getType())) {
            PromotionCombinationPackDTO promotionCombinationPackDTO = promotionActivityApi.quaryCombinationPackByActivityId(id);
            PromotionCombinationPackageVO combinationPackage = PojoUtils.map(promotionCombinationPackDTO, PromotionCombinationPackageVO.class);
            // 如果组合包要计算实际销售价，活动价，立省价格
            List<BigDecimal> sellingPrice = new ArrayList<>();
            List<BigDecimal> combinationPackagePrice = new ArrayList<>();
            promotionGoodsLimitDTOList.forEach(item -> {
                combinationPackagePrice.add(item.getPackageTotalPrice());
                sellingPrice.add(NumberUtil.round(NumberUtil.mul(item.getPrice(), item.getAllowBuyCount()), 2));
            });
            combinationPackage.setSellingPrice(sellingPrice.stream().reduce(BigDecimal::add).get());
            combinationPackage.setCombinationPackagePrice(combinationPackagePrice.stream().reduce(BigDecimal::add).get());
            combinationPackage.setCombinationDiscountPrice(NumberUtil.round(NumberUtil.sub(combinationPackage.getSellingPrice(), combinationPackage.getCombinationPackagePrice()), 2));
            vo.setPromotionCombinationPackage(combinationPackage);
        }
        //针对组合包活动要判断活动是否开始，如果没有开始可以编辑，就要显示goods对应的sku集合信息。
        // 促销活动商品表
        List<Long> goodsIdList = promotionGoodsLimitDTOList.stream().map(PromotionGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
        Map<Long, PromotionGoodsLimitDTO> promotionGoodsLimitDTOMap = promotionGoodsLimitDTOList.stream()
            .collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsId, o -> o, (k1, k2) -> k1));
        List<GoodsInfoDTO> goodsInfoDTOS = b2bGoodsApi.batchQueryInfo(goodsIdList);
        List<GoodsListItemVO> goodsListItemVOS = PojoUtils.map(goodsInfoDTOS, GoodsListItemVO.class);
        Map<Long, Long> longMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
        List<GoodsSkuDTO> goodsSkuDTOList = null;
        if(PromotionTypeEnum.COMBINATION_PACKAGE.getType().equals(promotionActivityDTO.getType())){
             goodsSkuDTOList=goodsApi.getGoodsSkuByGoodsIds(goodsIdList);
        }

        List<Long> subEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList,subEids);
        for (GoodsListItemVO e :goodsListItemVOS) {
            e.setYilingGoodsFlag(false);
            if(goodsMap.get(e.getId())!=null&&goodsMap.get(e.getId())>0){
                e.setYilingGoodsFlag(true);
            }
            e.setGoodsId(e.getId());
            e.setGoodsName(e.getName());
            if(PromotionTypeEnum.COMBINATION_PACKAGE.getType().equals(promotionActivityDTO.getType())){
                setCombinationPackageInfo(e,promotionGoodsLimitDTOMap,goodsSkuDTOList,goodSkuLimitDTOMap);
            }else {
                Long count = longMap.get(e.getId());
                e.setCount(count);
            }
            e.setPromotionPrice(promotionGoodsLimitDTOMap.get(e.getId()).getPromotionPrice());

            e.setAllowBuyCount(promotionGoodsLimitDTOMap.get(e.getId()).getAllowBuyCount());
        }
        vo.setPromotionGoodsLimitList(goodsListItemVOS);

        return Result.success(vo);
    }

    // goodsid查出来的商品信息。 goodsid对应的促销活动信息，goodssku对应的商品信息， goodsku对应的促销活动信息
    private void setCombinationPackageInfo(GoodsListItemVO goodsListItemVO,Map<Long, PromotionGoodsLimitDTO>promotionGoodsLimitDTOMap,List<GoodsSkuDTO> goodsSkuDTOList,Map<Long, PromotionGoodsLimitDTO> goodSkuLimitDTOMap) {
        // 活动类型是组合包的时候，要获取组合包产品goods关联的sku库存数量，不是产品的goods的数量。
        PromotionGoodsLimitDTO promotionGoodsLimit = promotionGoodsLimitDTOMap.get(goodsListItemVO.getId());
        PromotionGoodsLimitDTO promotionGoodsLimitDTO = goodSkuLimitDTOMap.get(promotionGoodsLimit.getGoodsSkuId());
        Map<Long, List<GoodsSkuDTO>> goodsSkuDTOMap = goodsSkuDTOList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
        List<GoodsSkuDTO> goodsSkuDTOS = goodsSkuDTOMap.get(goodsListItemVO.getId());
        goodsListItemVO.setPackageTotalPrice(promotionGoodsLimitDTO.getPackageTotalPrice());
        goodsListItemVO.setGoodsSkuId(promotionGoodsLimit.getGoodsSkuId());
        goodsListItemVO.setGoodsSkuList(PojoUtils.map(goodsSkuDTOS, GoodsSkuVO.class));
        Optional<GoodsSkuDTO> firstSku = goodsSkuDTOMap.getOrDefault(goodsListItemVO.getId(),new ArrayList<>()).stream().filter(item -> ObjectUtil.isNotNull(item)&&promotionGoodsLimit.getGoodsSkuId().equals(item.getId())).findFirst();
        if(firstSku.isPresent()){
            GoodsSkuDTO goodsSkuDTO = firstSku.get();
            Long qty = goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty();
            goodsListItemVO.setInSn(goodsSkuDTO.getSn());
            goodsListItemVO.setCount(qty);
            goodsListItemVO.setPackageNumber(goodsSkuDTO.getPackageNumber());
        }
    }

    @ApiOperation(value = "提交按钮-运营后台")
    @PostMapping("/submit")
    @Log(title = "提交促销活动", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> submit(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid PromotionSaveForm form) {

        // 1、参数校验
        form.check();

        // 2、购买数量校验
        if (PromotionTypeEnum.isSecKillOrSpecialPriceOrCombinationPackage(form.getActivity().getType())) {
            List<Long> goodsIdList = form.getGoodsLimitList().stream().map(PromotionGoodsLimitSaveForm::getGoodsId).collect(Collectors.toList());
            Map<Long, Long> goodsMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
            boolean result = form.getGoodsLimitList().stream()
                .anyMatch(item -> item.getAllowBuyCount() > Optional.ofNullable(goodsMap.get(item.getGoodsId())).orElse(0L).longValue());
            if (result) {
                return Result.failed("允许购买数量非法或者超过了商品库存数量");
            }
        }

        // 3、保存
        PromotionSaveRequest request = PojoUtils.map(form, PromotionSaveRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        long id = promotionActivityApi.savePromotionActivity(request);
        return Result.success(id);
    }

    @ApiOperation(value = "状态修改-运营后台")
    @PostMapping("/status")
    @Log(title = "编辑促销活动状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editStatus(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionActivityStatusForm form) {
        PromotionActivityStatusRequest request = PojoUtils.map(form, PromotionActivityStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        boolean isSuccess = promotionActivityApi.editStatusById(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("修改失败");
        }
    }

    @ApiOperation(value = "复制-运营后台")
    @PostMapping("/copy")
    @Log(title = "复制促销活动", businessType = BusinessTypeEnum.OTHER)
    public Result<Long> copy(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody PromotionActivityStatusForm form) {
        PromotionActivityStatusRequest request = PojoUtils.map(form, PromotionActivityStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        PromotionActivityDTO activityDTO = promotionActivityApi.copy(request);
        if (null != activityDTO) {
            return Result.success(activityDTO.getId());
        } else {
            return Result.failed("修改失败");
        }
    }

    @ApiOperation(value = "查询参与满赠活动的订单信息")
    @PostMapping("/pageGiftOrder")
    public Result<Page<PromotionGoodsGiftUsedVO>> pageGiftOrder(@CurrentUser CurrentAdminInfo staffInfo,
                                                                @RequestBody PromotionGoodsGiftUsedForm form) {
        PromotionGoodsGiftUsedRequest request = PojoUtils.map(form, PromotionGoodsGiftUsedRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        Page<PromotionGoodsGiftUsedDTO> dtoPage = giftLimitApi.pageGiftOrder(request);
        if (CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(PojoUtils.map(dtoPage, PromotionGoodsGiftUsedVO.class));
        }
        List<Long> userIdList = dtoPage.getRecords().stream().map(PromotionGoodsGiftUsedDTO::getCreateUser).collect(Collectors.toList());
        List<Long> buyerEIdList = dtoPage.getRecords().stream().map(PromotionGoodsGiftUsedDTO::getBuyerEid).collect(Collectors.toList());
        List<UserDTO> userDTOList = userApi.listByIds(userIdList);
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(buyerEIdList);
        Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, o -> o, (k1, k2) -> k1));
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));
        dtoPage.getRecords().stream().forEach(item -> {
            item.setBuyerName(Optional.ofNullable(userDTOMap.get(item.getCreateUser())).map(UserDTO::getName).orElse(null));
            item.setBuyerTel(Optional.ofNullable(userDTOMap.get(item.getCreateUser())).map(UserDTO::getMobile).orElse(null));
            item.setAddress(Optional.ofNullable(enterpriseDTOMap.get(item.getBuyerEid())).map(EnterpriseDTO::getAddress).orElse(null));
        });
        Page<PromotionGoodsGiftUsedVO> voPage = PojoUtils.map(dtoPage, PromotionGoodsGiftUsedVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "营销活动商品信息导入", httpMethod = "POST")
    @PostMapping(value = "importPromotionGoods", headers = "content-type=multipart/form-data")
    public Result<List<GoodsListItemVO>> importPromotionGoods(PromotionImportGoodsForm form, @CurrentUser CurrentAdminInfo staffInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importPromotionGoodsDataHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = form.getFile().getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel<ImportPromotionGoodsForm> importResultModel;
        try {
            //包含了插入数据库失败的信息
            Long start = System.currentTimeMillis();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, staffInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportPromotionGoodsForm.class, params, importPromotionGoodsDataHandler,
                paramMap);
            log.info("营销活动商品信息导入耗时：{},导入数据为:[{}]", System.currentTimeMillis() - start, importResultModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        if (null == importResultModel) {
            return Result.failed("导入信息失败");
        }
        List<ImportPromotionGoodsForm> modelList = importResultModel.getList();
        List<GoodsListItemVO> goodsListItemVOList = checkPromotionGoods(modelList, form);
        if (CollUtil.isEmpty(goodsListItemVOList)) {
            return Result.failed("导入符合条件的商品为空");
        }
        return Result.success(goodsListItemVOList);
    }

    /**
     * 校验商品信息
     *
     * @param form
     * @return
     */
    private List<GoodsListItemVO> checkPromotionGoods(List<ImportPromotionGoodsForm> list, PromotionImportGoodsForm form) {
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        log.info("开始校验商品信息,参数：{}", form);
        List<Long> goodsIdList = list.stream().map(ImportPromotionGoodsForm::getGoodsId).collect(Collectors.toList());
        List<GoodsInfoDTO> goodsInfoDTOS = b2bGoodsApi.batchQueryInfo(goodsIdList);
        List<Long> yilingEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        Map<Long, Long> yilingGoodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, yilingEids);

        List<GoodsInfoDTO> goodsInfoDTOList = goodsInfoDTOS.stream().filter(goodsInfoDTO -> {

            boolean goodsStatus = 1 == goodsInfoDTO.getGoodsStatus();
            // 空或者0都不校验
            if (Objects.isNull(form.getMerchantType())||form.getMerchantType()==0) {
                return goodsStatus;
            }

            Long flag = Optional.ofNullable(yilingGoodsMap.get(goodsInfoDTO.getId())).orElse(0L);
            boolean notYl = Long.valueOf(0).equals(flag);
            if (PromotionMerchantTypeEnum.YL.getType().equals(form.getMerchantType())) {
                return goodsStatus && !notYl;
            }

            if (PromotionMerchantTypeEnum.NOT_YL.getType().equals(form.getMerchantType())) {
                return goodsStatus && notYl;
            }
            if (PromotionMerchantTypeEnum.ALL.getType().equals(form.getMerchantType())) {
                return goodsStatus;
            }
            return false;

        }).collect(Collectors.toList());
        List<GoodsListItemVO> resultList = new ArrayList<>();
        List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(goodsInfoDTOList, GoodsListItemVO.class);
        Map<Long, Long> longMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);

        List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOList = promotionActivityApi.queryNotRepeatByGoodsIdList(goodsIdList, form.getSponsorType(), form.getType());
        Map<Long, List<PromotionGoodsLimitDTO>> finalLongListMap = promotionGoodsLimitDTOList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));

        log.info("对应的商品信息为:[{}],已参与活动的商品信息为:[{}]", goodsInfoDTOList, finalLongListMap);
        List<Long> eidList = Arrays.stream(form.getEidList().split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        for (GoodsListItemVO goodsListItemVO : goodsListItemVOList) {
            if (eidList.contains(goodsListItemVO.getEid())) {
                GoodsDisableVO goodsDisableVO = new GoodsDisableVO();

                List<PromotionGoodsLimitDTO> promotionGoodsLimitDTOS = finalLongListMap.get(goodsListItemVO.getId());
                goodsDisableVO.setIsAllowSelect(0);
                if (CollUtil.isNotEmpty(promotionGoodsLimitDTOS) && promotionGoodsLimitDTOS.size() > 0) {
                    if (!PromotionTypeEnum.isCombinationPackage(form.getType())) {
                        goodsDisableVO.setIsAllowSelect(1);
                    }
                    PromotionGoodsLimitDTO promotionGoodsLimitDTO = promotionGoodsLimitDTOS.get(0);
                    if (promotionGoodsLimitDTO.getPromotionActivityId().equals(form.getPromotionActivityId())) {
                        goodsDisableVO.setIsAllowSelect(0);
                    }
                }

                goodsListItemVO.setGoodsDisableVO(goodsDisableVO);
                goodsListItemVO.setGoodsName(goodsListItemVO.getName());
                goodsListItemVO.setGoodsId(goodsListItemVO.getId());
                Long count = longMap.get(goodsListItemVO.getId());
                goodsListItemVO.setCount(count);
                if (0 == goodsDisableVO.getIsAllowSelect()) {
                    resultList.add(goodsListItemVO);
                }
            }
        }
        log.info("校验成功的返回数据为:[{}]", resultList);
        return resultList;
    }

    @ApiOperation(value = "设置商家-查询供应商", httpMethod = "POST")
    @PostMapping("/queryEnterpriseListPage")
    public Result<Page<CouponActivityEnterprisePageVO>> queryEnterpriseListPage(@CurrentUser CurrentStaffInfo staffInfo,
                                                                                @RequestBody QueryCouponActivityEnterpriseFrom from) {
        QueryCouponActivityEnterpriseRequest request = PojoUtils.map(from, QueryCouponActivityEnterpriseRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Page<CouponActivityEnterprisePageVO> page = request.getPage();
        if (ObjectUtil.isNull(request)) {
            Result.success(page);
        }
        // 根据id、name查询企业信息
        QueryEnterprisePageListRequest enterprisePageListRequest = PojoUtils.map(request, QueryEnterprisePageListRequest.class);
        enterprisePageListRequest.setCurrent(request.getCurrent());
        enterprisePageListRequest.setSize(request.getSize());
        enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        // 仅能添加开通B2B、商业类型的
        enterprisePageListRequest.setMallFlag(1);
        if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
            enterprisePageListRequest.setId(request.getEid());
        }
        if (StrUtil.isNotBlank(request.getEname())) {
            enterprisePageListRequest.setName(request.getEname());
        }
        // 这里判断，如果选择的是以岭商品，那么增加企业id范围条件，同时添加YL企业id
        List<Integer> inTypeList = Lists.newArrayList();
        inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
        List<Long> idList = enterpriseApi.listSubEids(Constants.YILING_EID);
        idList.add(Constants.YILING_EID);
        if (EnterpriseClassEnum.yl.getCode().equals(from.getMerchantType())) {
            enterprisePageListRequest.setIds(idList);
            inTypeList.add(EnterpriseTypeEnum.INDUSTRY.getCode());
        } else {
            enterprisePageListRequest.setNotInIds(idList);
        }
        enterprisePageListRequest.setInTypeList(inTypeList);
        Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
        List<CouponActivityEnterprisePageVO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(enterpriseDTOPage.getRecords())) {
            CouponActivityEnterprisePageVO couponActivityEnterprise;
            int index = 0;
            for (EnterpriseDTO enterprise : enterpriseDTOPage.getRecords()) {
                couponActivityEnterprise = new CouponActivityEnterprisePageVO();
                couponActivityEnterprise.setEid(enterprise.getId());
                couponActivityEnterprise.setEname(enterprise.getName());
                list.add(index, couponActivityEnterprise);
                index++;
            }
        }
        page = PojoUtils.map(enterpriseDTOPage, CouponActivityEnterprisePageVO.class);
        page.setRecords(list);
        return Result.success(page);
    }

}
