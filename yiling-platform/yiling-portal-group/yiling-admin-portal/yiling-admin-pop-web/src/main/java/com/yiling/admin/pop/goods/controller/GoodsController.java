package com.yiling.admin.pop.goods.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.export.excel.model.ImportResultModel;
import com.yiling.export.excel.util.ExeclImportUtils;
import org.apache.commons.collections4.CollectionUtils;
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
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.pop.goods.form.EditBiddingPriceForm;
import com.yiling.admin.pop.goods.form.EditGoodsForm;
import com.yiling.admin.pop.goods.form.ImportGoodsBiddingPriceForm;
import com.yiling.admin.pop.goods.form.ImportGoodsForm;
import com.yiling.admin.pop.goods.form.QueryEnterpriseGoodsPageListForm;
import com.yiling.admin.pop.goods.form.QueryGoodsBiddingPricePageListForm;
import com.yiling.admin.pop.goods.form.QueryGoodsPageListForm;
import com.yiling.admin.pop.goods.form.QueryPopGoodsPageListForm;
import com.yiling.admin.pop.goods.handler.ImportGoodsBiddingPriceHandler;
import com.yiling.admin.pop.goods.handler.ImportGoodsDataHandler;
import com.yiling.admin.pop.goods.handler.ImportGoodsVerifyHandler;
import com.yiling.admin.pop.goods.vo.EnterpriseGoodsListVO;
import com.yiling.admin.pop.goods.vo.GoodsBiddingLocationPriceVO;
import com.yiling.admin.pop.goods.vo.GoodsBiddingPricePageListItemVO;
import com.yiling.admin.pop.goods.vo.GoodsDetailsVO;
import com.yiling.admin.pop.goods.vo.GoodsDisableVO;
import com.yiling.admin.pop.goods.vo.GoodsListItemPageVO;
import com.yiling.admin.pop.goods.vo.GoodsListItemVO;
import com.yiling.admin.pop.goods.vo.GoodsSkuVO;
import com.yiling.admin.pop.goods.vo.InventoryDetailVO;
import com.yiling.admin.pop.goods.vo.StandardGoodsAllInfoVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsBiddingPriceDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.AddGoodsBiddingPriceRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveGoodsLineRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.enums.StandardGoodsStatusEnum;
import com.yiling.mall.category.api.CategoryGoodsApi;
import com.yiling.mall.category.dto.HomeCategoryGoodsDTO;
import com.yiling.mall.recommend.api.RecommendGoodsApi;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@RestController
@Api(tags = "供应商商品信息")
@RequestMapping("/admin/goods")
@Slf4j
public class GoodsController extends BaseController {

    @Autowired
    FileService                    fileService;
    @Autowired
    PictureUrlUtils                pictureUrlUtils;
    @Autowired
    ImportGoodsBiddingPriceHandler importGoodsBiddingPriceHandler;
    @Autowired
    ImportGoodsVerifyHandler       goodsImportVerifyHandler;
    @Autowired
    ImportGoodsDataHandler         goodsImportDataHandler;
    @DubboReference
    GoodsApi                       goodsApi;
    @DubboReference
    PopGoodsApi                    popGoodsApi;
    @DubboReference
    InventoryApi                   inventoryApi;
    @DubboReference
    EnterpriseApi                  enterpriseApi;
    @DubboReference
    StandardGoodsApi               standardGoodsApi;
    @DubboReference
    RecommendGoodsApi              recommendGoodsApi;
    @DubboReference
    GoodsBiddingPriceApi           goodsBiddingPriceApi;
    @DubboReference
    LocationApi                    locationApi;
    @DubboReference
    CategoryGoodsApi               categoryGoodsApi;


    @ApiOperation(value = "管理后台商品信息更新导入", httpMethod = "POST")
    @PostMapping(value = "importUpdateGoods", headers = "content-type=multipart/form-data")
    @Log(title = "管理后台POP商品信息更新导入",businessType = BusinessTypeEnum.IMPORT)
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
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportGoodsForm.class, params, goodsImportDataHandler, paramMap);
            log.info("商品导入耗时：{}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "管理后台供应商查询", httpMethod = "POST")
    @PostMapping("/enterpriseList")
    public Result<Page<EnterpriseGoodsListVO>> enterpriseList(@RequestBody @Valid QueryEnterpriseGoodsPageListForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        request.setPopFlag(1);
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        Page<EnterpriseGoodsListVO> pageVO = PojoUtils.map(page, EnterpriseGoodsListVO.class);
        pageVO.getRecords().forEach(e -> {
            List<QueryStatusCountBO> list = popGoodsApi.queryPopStatusCountList(Arrays.asList(e.getId()));
            Map<Integer, Long> map = list.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            e.setUpShelfCount(map.get(GoodsStatusEnum.UP_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UP_SHELF.getCode()));
            e.setUnShelfCount(map.get(GoodsStatusEnum.UN_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UN_SHELF.getCode()));
            e.setWaitSetCount(map.get(GoodsStatusEnum.WAIT_SET.getCode()) == null ? 0L : map.get(GoodsStatusEnum.WAIT_SET.getCode()));
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "管理后台商品管理查询", httpMethod = "POST")
    @PostMapping("/list")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> list(@RequestBody @Valid QueryGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(Arrays.asList(form.getEid()));
        Page<GoodsListItemBO> page = popGoodsApi.queryPopGoodsPageList(request);
        GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<GoodsListItemVO> listItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);
            List<Long> goodsIds = listItemVOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIds);
            Map<Long, List<GoodsSkuDTO>> goodsSkuMap = goodsSkuDTOList.stream().filter(e -> e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
            listItemVOList.forEach(e -> {
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                e.setGoodsSkuList(PojoUtils.map(goodsSkuMap.get(e.getId()), GoodsSkuVO.class));
            });
            newPage.setRecords(listItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            request.setGoodsStatus(null);
            List<QueryStatusCountBO> listByCondition = popGoodsApi.queryPopStatusCountListByCondition(request);
            Map<Integer, Long> map = listByCondition.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            newPage.setUpShelfCount(map.get(GoodsStatusEnum.UP_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UP_SHELF.getCode()));
            newPage.setUnShelfCount(map.get(GoodsStatusEnum.UN_SHELF.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UN_SHELF.getCode()));
            newPage.setWaitSetCount(map.get(GoodsStatusEnum.WAIT_SET.getCode()) == null ? 0L : map.get(GoodsStatusEnum.WAIT_SET.getCode()));
        }
        return Result.success(newPage);
    }

    @ApiOperation(value = "商品修改接口")
    @PostMapping("/edit")
    @Log(title = "管理后台商品修改接口",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> edit(@RequestBody @Valid EditGoodsForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        GoodsInfoDTO goodsInfoDTO = popGoodsApi.queryInfo(form.getGoodsId());
        if (goodsInfoDTO == null) {
            return Result.failed("商品不存在");
        }

        //判断商品是否已经在平台上禁止销售
        if (goodsInfoDTO.getStandardId() != null && goodsInfoDTO.getStandardId() != 0) {
            StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(goodsInfoDTO.getStandardId());
            if (standardGoodsByIdInfoDTO == null || standardGoodsByIdInfoDTO.getBaseInfo().getGoodsStatus().equals(StandardGoodsStatusEnum.FORBID.getCode())) {
                return Result.failed("该商品已经在平台禁止销售");
            }
        }

        SaveOrUpdateGoodsRequest request = PojoUtils.map(form, SaveOrUpdateGoodsRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setId(form.getGoodsId());
        //操作上下架
        if (form.getGoodsStatus() != null) {
            //判断当前商品状态是否可编辑商品
            if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsInfoDTO.getAuditStatus())) {
                return Result.failed("该商品还没有审核通过不能编辑");
            }
            //如果是上架，下架原因必须是运营下架
            if (GoodsStatusEnum.UP_SHELF.getCode().equals(form.getGoodsStatus()) && ObjectUtil.notEqual(goodsInfoDTO.getOutReason(), GoodsOutReasonEnum.QUALITY_CONTROL.getCode())) {
                return Result.failed("该商品不是质管下架的不能上架操作");
            }
//            //如果是下架架，修改下架原因2
//            if (GoodsStatusEnum.UN_SHELF.getCode().equals(form.getGoodsStatus())) {
            request.setOutReason(GoodsOutReasonEnum.QUALITY_CONTROL.getCode());
//            }
        }
        //更新商品
        SaveGoodsLineRequest goodsLineInfo = new SaveGoodsLineRequest();
        goodsLineInfo.setPopFlag(1);
        request.setGoodsLineInfo(goodsLineInfo);
        request.setPopEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        Long i = goodsApi.editGoods(request);
        return Result.success(i > 0);
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
            goodsSkuDTOList=goodsSkuDTOList.stream().filter(e->e.getGoodsLine().equals(GoodsLineEnum.POP.getCode())).collect(Collectors.toList());
            resultData.setGoodsSkuList(PojoUtils.map(goodsSkuDTOList, GoodsSkuVO.class));

            //获取上下状态
            PopGoodsDTO popGoodsDTO = popGoodsApi.getPopGoodsByGoodsId(goodsId);
            resultData.setGoodsStatus(popGoodsDTO.getGoodsStatus());
            return Result.success(resultData);
        } else {
            return Result.failed("商品不存在");
        }
    }


    @ApiOperation(value = "运营后台商品弹框查询", httpMethod = "POST")
    @PostMapping("/popList")
    public Result<Page<GoodsListItemVO>> popList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPopGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        request.setEidList(eidList);
        request.setGoodsLine(GoodsLineEnum.POP.getCode());
        request.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        Page<GoodsListItemVO> page = PojoUtils.map(goodsApi.queryPageListGoods(request), GoodsListItemVO.class);
        Long recommendId = form.getRecommendId();
        List<Long> goodsIdList = CollUtil.newArrayList();
        if (recommendId != null && recommendId != 0) {
            List<RecommendGoodsDTO> recommendGoodsBytRecommendId = recommendGoodsApi.getRecommendGoodsBytRecommendId(recommendId);
            goodsIdList = recommendGoodsBytRecommendId.stream().map(RecommendGoodsDTO::getGoodsId).collect(Collectors.toList());
        }

        //根据分类ID获取分类对应的商品
        if (Objects.nonNull(form.getCategoryId()) && form.getCategoryId() != 0) {
            List<HomeCategoryGoodsDTO> goodsByCategoryId = categoryGoodsApi.getCategoryGoodsByCategoryId(form.getCategoryId());
            goodsIdList = goodsByCategoryId.stream().map(HomeCategoryGoodsDTO::getGoodsId).collect(Collectors.toList());
        }

        List<Long> finalGoodsIdList = goodsIdList;
        page.getRecords().forEach(e -> {
            GoodsDisableVO goodsDisableVO = new GoodsDisableVO();
            //判断渠道商品是否已经被选了
            if (CollectionUtils.isNotEmpty(finalGoodsIdList) && finalGoodsIdList.contains(e.getId())) {
                goodsDisableVO.setAgreementDisable(true);
                goodsDisableVO.setAgreementDesc("已被占用");
            }
            e.setGoodsDisableVO(goodsDisableVO);

            e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
        });
        return Result.success(page);
    }

    @ApiOperation(value = "招标挂网价商品查询", httpMethod = "POST")
    @PostMapping("/goodsBiddingPricePageList")
    public Result<Page<GoodsBiddingPricePageListItemVO>> goodsBiddingPricePageList(@RequestBody @Valid QueryGoodsBiddingPricePageListForm form) {
        Page<GoodsBiddingPricePageListItemVO> resultPage;
        //商品id列表
        List<Long> goodsIds;
        //设置过价格的省份和价格
        List<GoodsBiddingPriceDTO> priceList;
        //设置过价格的省份和价格map
        Map<Long, List<GoodsBiddingPriceDTO>> priceListMap;

        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        //查询分公司
        List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        request.setEidList(eidList);
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        //根据条件搜索以岭商品
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);
        //补全设置招标价省份数
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(PojoUtils.map(page, GoodsBiddingPricePageListItemVO.class));
        }
        goodsIds = page.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
        //根据商品id查询设置过价格的省份
        priceList = goodsBiddingPriceApi.queryGoodsBidingPriceList(goodsIds);
        priceListMap = priceList.stream().collect(Collectors.groupingBy(GoodsBiddingPriceDTO::getGoodsId));
        resultPage = PojoUtils.map(page, GoodsBiddingPricePageListItemVO.class);
        //补足设置省份数
        resultPage.getRecords().forEach(e -> {
                    if (priceListMap.get(e.getGoodsId()) == null) {
                        e.setLocationCount(0);
                    } else {
                        e.setLocationCount(priceListMap.get(e.getGoodsId()).size());
                    }
                }
        );

        return Result.success(resultPage);
    }

    @ApiOperation(value = "根据商品id查询各省份的招标挂网价")
    @GetMapping("/queryGoodsBiddingLocationPrice")
    public Result<GoodsBiddingLocationPriceVO> queryGoodsBiddingLocationPrice(@RequestParam Long goodsId) {
        GoodsBiddingLocationPriceVO result;
        List<GoodsBiddingLocationPriceVO.LocationPrice> locationPriceList = ListUtil.toList();
        //设置过价格的省份和价格
        List<GoodsBiddingPriceDTO> currentPriceList;
        //设置过价格的 省份(key)和价格(value)map
        Map<String, BigDecimal> locationPriceMap;

        //查询商品信息
        List<GoodsInfoDTO> goodsInfo = popGoodsApi.batchQueryInfo(ListUtil.toList(goodsId));
        if (CollUtil.isEmpty(goodsInfo)) {
            return Result.failed("商品不存在");
        }
        GoodsInfoDTO goodsInfoDTO = goodsInfo.get(0);
        result = PojoUtils.map(goodsInfoDTO, GoodsBiddingLocationPriceVO.class);
        result.setGoodsId(goodsInfoDTO.getId());
        //查询所有省
        List<LocationDTO> locationList = locationApi.listByParentCode("");
        //根据商品id查询设置过价格的省份
        currentPriceList = goodsBiddingPriceApi.queryGoodsBidingPriceList(ListUtil.toList(goodsId));
        locationPriceMap = currentPriceList.stream().collect(Collectors.toMap(GoodsBiddingPriceDTO::getLocationCode, GoodsBiddingPriceDTO::getPrice));
        //组装数据
        String goodsPicUrl = pictureUrlUtils.getGoodsPicUrl(goodsInfoDTO.getPic());
        result.setPic(goodsPicUrl);
        locationList.forEach(e -> {
            GoodsBiddingLocationPriceVO.LocationPrice locationPrice = new GoodsBiddingLocationPriceVO
                    .LocationPrice(e.getCode(), e.getName(), new BigDecimal("0"));
            BigDecimal price = locationPriceMap.get(e.getCode());
            if (price != null) {
                //设置省份价格
                locationPrice.setPrice(price);
            }
            locationPriceList.add(locationPrice);
        });
        //设置以设置招标价的省的个数
        result.setLocationCount(currentPriceList.size());
        //设置省份价格
        result.setLocationPriceList(locationPriceList);
        return Result.success(result);
    }

    @ApiOperation(value = "导入招标挂网价数据")
    @PostMapping("/importGoodsBiddingPrice")
    @Log(title = "导入招标挂网价数据",businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importGoodsBiddingPrice(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importGoodsBiddingPriceHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID,adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportGoodsBiddingPriceForm.class, params, importGoodsBiddingPriceHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "编辑招标挂网价")
    @PostMapping("/editBiddingPrice")
    @Log(title = "编辑招标挂网价",businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> editBiddingPrice(@RequestBody @Valid EditBiddingPriceForm form, @CurrentUser CurrentAdminInfo adminInfo) {

        GoodsBiddingPriceDTO priceDTO = goodsBiddingPriceApi.queryGoodsBiddingPrice(form.getLocationCode(), form.getGoodsId());
        AddGoodsBiddingPriceRequest request;
        if (ObjectUtil.isNull(priceDTO)) {
            String[] namesByCodes = locationApi.getNamesByCodes(form.getLocationCode(), null, null);
            if (StrUtil.isEmpty(namesByCodes[0])) {
                return Result.failed("省编码不存在");
            }
            request = PojoUtils.map(form, AddGoodsBiddingPriceRequest.class);
            request.setLocationName(namesByCodes[0]);
        } else {
            request = PojoUtils.map(priceDTO, AddGoodsBiddingPriceRequest.class);
            request.setPrice(form.getPrice());
        }
        request.setOpUserId(adminInfo.getCurrentUserId());
        goodsBiddingPriceApi.saveOrUpdate(request);
        return Result.success(new BoolObject(Boolean.TRUE));
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
                detailVO.setInSn(subscription.getInSn());;
                detailVOS.add(detailVO);
            });
        }
        return Result.success(detailVOS);
    }
}
