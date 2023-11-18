package com.yiling.admin.data.center.goods.controller;

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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.data.center.goods.form.EditGoodsForm;
import com.yiling.admin.data.center.goods.form.ImportEnterpriseGoodsForm;
import com.yiling.admin.data.center.goods.form.QueryEnterpriseGoodsPageListForm;
import com.yiling.admin.data.center.goods.form.QueryGoodsPageListForm;
import com.yiling.admin.data.center.goods.form.QueryPopGoodsPageListForm;
import com.yiling.admin.data.center.goods.handler.ImportEnterpriseGoodsDataHandler;
import com.yiling.admin.data.center.goods.handler.ImportEnterpriseGoodsVerifyHandler;
import com.yiling.admin.data.center.goods.vo.EnterpriseGoodsListVO;
import com.yiling.admin.data.center.goods.vo.GoodsDisableVO;
import com.yiling.admin.data.center.goods.vo.GoodsListItemPageVO;
import com.yiling.admin.data.center.goods.vo.GoodsListItemVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.service.FileService;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.enums.GoodsOutReasonEnum;
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
import cn.hutool.core.util.ObjectUtil;
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
    ImportEnterpriseGoodsVerifyHandler importEnterpriseGoodsVerifyHandler;
    @Autowired
    ImportEnterpriseGoodsDataHandler   importEnterpriseGoodsDataHandler;
    @Autowired
    FileService                        fileService;
    @Autowired
    PictureUrlUtils                    pictureUrlUtils;
    @Autowired
    ExeclImportUtils                   execlImportUtils;
    @DubboReference
    GoodsApi                           goodsApi;
    @DubboReference
    PopGoodsApi                        popGoodsApi;
    @DubboReference
    InventoryApi                       inventoryApi;
    @DubboReference
    EnterpriseApi                      enterpriseApi;
    @DubboReference
    StandardGoodsApi                   standardGoodsApi;
    @DubboReference
    RecommendGoodsApi                  recommendGoodsApi;
    @DubboReference
    GoodsBiddingPriceApi               goodsBiddingPriceApi;
    @DubboReference
    LocationApi                        locationApi;
    @DubboReference
    CategoryGoodsApi                   categoryGoodsApi;

    @ApiOperation(value = "管理后台供应商商品信息导入", httpMethod = "POST")
    @PostMapping(value = "import", headers = "content-type=multipart/form-data")
    @Log(title = "供应商商品导入",businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> goodsImport(@RequestParam(value = "file", required = true) MultipartFile file, @CurrentUser CurrentAdminInfo adminInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(true);
        params.setSaveUrl(ExeclImportUtils.EXECL_PATH);
        params.setNeedVerify(true);
        params.setVerifyHandler(importEnterpriseGoodsVerifyHandler);
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
//            importResultModel = ExeclImportUtils.importExcelMore(in, ImportEnterpriseGoodsForm.class, params, importEnterpriseGoodsDataHandler, paramMap);
            importResultModel = execlImportUtils.parallelImportExcelMore(in, ImportEnterpriseGoodsForm.class, params, importEnterpriseGoodsDataHandler, paramMap);
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
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        Page<EnterpriseGoodsListVO> pageVO = PojoUtils.map(page, EnterpriseGoodsListVO.class);
        pageVO.getRecords().forEach(e -> {
            List<QueryStatusCountBO> list = goodsApi.getCountByEid(e.getId());
            Map<Integer, Long> map = list.stream().collect(Collectors.toMap(QueryStatusCountBO::getGoodsStatus, QueryStatusCountBO::getCount));
            e.setAuditPassCount(map.get(GoodsStatusEnum.AUDIT_PASS.getCode()) == null ? 0L : map.get(GoodsStatusEnum.AUDIT_PASS.getCode()));
            e.setUnderReviewCount(map.get(GoodsStatusEnum.UNDER_REVIEW.getCode()) == null ? 0L : map.get(GoodsStatusEnum.UNDER_REVIEW.getCode()));
            e.setRejectCount(map.get(GoodsStatusEnum.REJECT.getCode()) == null ? 0L : map.get(GoodsStatusEnum.REJECT.getCode()));
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "管理后台商品管理查询", httpMethod = "POST")
    @PostMapping("/list")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> list(@RequestBody @Valid QueryGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(Arrays.asList(form.getEid()));
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);
        if (page != null) {
            GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
            List<GoodsListItemVO> listItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);

            listItemVOList.forEach(e -> {
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
            newPage.setRecords(listItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            request.setGoodsStatus(null);
            return Result.success(newPage);
        } else {
            return Result.failed(ResultCode.FAILED);
        }
    }

    @ApiOperation(value = "商品修改接口")
    @PostMapping("/edit")
    @Log(title = "运营后台商品修改",businessType = BusinessTypeEnum.UPDATE)
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
        Long i = goodsApi.editGoods(request);
        return Result.success(i > 0);
    }


    @ApiOperation(value = "运营后台商品弹框查询", httpMethod = "POST")
    @PostMapping("/popList")
    public Result<Page<GoodsListItemVO>> popList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPopGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        List<Long> eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        request.setEidList(eidList);
        request.setGoodsStatus(GoodsStatusEnum.UP_SHELF.getCode());
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
}
