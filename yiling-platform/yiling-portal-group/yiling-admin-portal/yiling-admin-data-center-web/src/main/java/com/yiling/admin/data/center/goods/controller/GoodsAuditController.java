package com.yiling.admin.data.center.goods.controller;


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.data.center.goods.form.EditGoodsAuditStatusForm;
import com.yiling.admin.data.center.goods.form.GoodsMatchForm;
import com.yiling.admin.data.center.goods.form.ImportMergeGoodsForm;
import com.yiling.admin.data.center.goods.form.QueryGoodsAuditPageListForm;
import com.yiling.admin.data.center.goods.form.QueryStandardGoodsPageListForm;
import com.yiling.admin.data.center.goods.form.QueryStandardSpecificationPageListForm;
import com.yiling.admin.data.center.goods.form.SaveSellSpecificationsForm;
import com.yiling.admin.data.center.goods.handler.ImportMergeGoodsDataHandler;
import com.yiling.admin.data.center.goods.vo.GoodsAuditListVO;
import com.yiling.admin.data.center.goods.vo.GoodsAuditPageVO;
import com.yiling.admin.data.center.goods.vo.MatchGoodsVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsAllInfoVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsBasicInfoVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsInfoVO;
import com.yiling.admin.data.center.standard.vo.StandardGoodsPicVO;
import com.yiling.admin.data.center.standard.vo.StandardSpecificationInfoVO;
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
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsAuditApi;
import com.yiling.goods.medicine.bo.MatchGoodsBO;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.request.MatchGoodsRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsAuditRequest;
import com.yiling.goods.medicine.dto.request.SaveSellSpecificationsRequest;
import com.yiling.goods.medicine.enums.GoodsAuditStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 商品待审核表 前端控制器
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Slf4j
@RestController
@Api(tags = "商品审核信息")
@RequestMapping("/goods/audit/")
public class GoodsAuditController extends BaseController {

    @DubboReference
    private GoodsAuditApi goodsAuditApi;

    @DubboReference
    private StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @DubboReference
    private GoodsApi goodsApi;

    @DubboReference
    private StandardGoodsApi standardGoodsApi;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @Autowired
    private PictureUrlUtils pictureUrlUtils;

    @Autowired
    private ImportMergeGoodsDataHandler importMergeGoodsDataHandler;

    @Log(title = "商品规格合并清洗",businessType = BusinessTypeEnum.IMPORT)
    @ApiOperation(value = "商品规格合并清洗", httpMethod = "POST")
    @PostMapping(value = "mergeImport", headers = "content-type=multipart/form-data")
    public Result<ImportResultVO> mergeImport(@RequestParam(value = "file", required = true) MultipartFile file, @CurrentUser CurrentAdminInfo adminInfo) {
        ImportParams params = new ImportParams();
        params.setNeedSave(true);
        params.setSaveUrl(ExeclImportUtils.EXECL_PATH);
        params.setNeedVerify(true);
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
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportMergeGoodsForm.class, params, importMergeGoodsDataHandler,paramMap);
            log.info("商品规格合并清洗导入耗时：{}", System.currentTimeMillis() - start);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "管理后台商品审核列表查询", httpMethod = "POST")
    @PostMapping("list")
    public Result<Page<GoodsAuditListVO>> list(@RequestBody @Valid QueryGoodsAuditPageListForm form) {
        QueryGoodsAuditPageRequest request = PojoUtils.map(form, QueryGoodsAuditPageRequest.class);
        Page<GoodsAuditDTO> page = goodsAuditApi.queryPageListGoodsAudit(request);
        return Result.success(PojoUtils.map(page, GoodsAuditListVO.class));
    }

    @ApiOperation(value = "管理后台商品审核驳回", httpMethod = "POST")
    @PostMapping("reject")
    public Result<BoolObject> reject(@RequestBody @Valid EditGoodsAuditStatusForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        SaveOrUpdateGoodsAuditRequest request = PojoUtils.map(form, SaveOrUpdateGoodsAuditRequest.class);
        request.setAuditStatus(GoodsAuditStatusEnum.NOT_PASS_AUDIT.getCode());
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean bool = goodsAuditApi.rejectGoodsAudit(request);
        return Result.success(new BoolObject(bool));
    }

    @ApiOperation(value = "管理后台商品审核匹配", httpMethod = "POST")
    @PostMapping("match")
    public Result<MatchGoodsVO> match(@RequestBody @Valid GoodsMatchForm form) {
        //通过ID查询待审核数据
        Long id = form.getId();
        GoodsAuditDTO goodsAuditDTO = goodsAuditApi.getById(id);
        MatchGoodsRequest request = PojoUtils.map(goodsAuditDTO, MatchGoodsRequest.class);
        MatchGoodsBO matchGoodsBO = goodsApi.matchSellSpecificationsByGoods(request);
        MatchGoodsVO matchGoodsVO = PojoUtils.map(matchGoodsBO, MatchGoodsVO.class);
        matchGoodsVO.setGoodsAuditListVO(PojoUtils.map(goodsAuditDTO, GoodsAuditListVO.class));
        return Result.success(matchGoodsVO);
    }


    @ApiOperation(value = "关联标准库售卖规格Id", httpMethod = "POST")
    @PostMapping(path = "/saveSellSpecifications")
    @Log(title = "匹配",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> saveSellSpecifications(@RequestBody @Valid SaveSellSpecificationsForm form, @CurrentUser CurrentAdminInfo adminInfo) {
        //先通过商品ID查询商业公司
        SaveSellSpecificationsRequest request = PojoUtils.map(form, SaveSellSpecificationsRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setPopEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        Boolean bool = goodsAuditApi.linkSellSpecifications(request);
        return Result.success(bool);
    }

    @ApiOperation(value = "匹配标准库查询标准商品出框", httpMethod = "POST")
    @PostMapping(path = "/standardGoodsPage")
    public Result<GoodsAuditPageVO<StandardGoodsInfoVO>> standardGoodsPage
            (@RequestBody QueryStandardGoodsPageListForm form) {
        StandardGoodsInfoRequest request = PojoUtils.map(form, StandardGoodsInfoRequest.class);
        Page<StandardGoodsInfoDTO> page = standardGoodsApi.getStandardGoodsInfo(request);
        if (page != null) {
            GoodsAuditDTO goodsAuditDTO = goodsAuditApi.getById(form.getId());
            GoodsAuditPageVO<StandardGoodsInfoVO> newPage = new GoodsAuditPageVO();
            newPage.setRecords(page.getRecords());
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            newPage.setGoodsAuditInfoVO(PojoUtils.map(goodsAuditDTO, GoodsAuditListVO.class));
            return Result.success(newPage);
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "匹配售卖规格查询规格弹出框", httpMethod = "POST")
    @PostMapping(path = "/standardSpecificationPage")
    public Result<GoodsAuditPageVO<StandardSpecificationInfoVO>> standardSpecificationPage
            (@RequestBody QueryStandardSpecificationPageListForm form) {
        StandardSpecificationPageRequest request = PojoUtils.map(form, StandardSpecificationPageRequest.class);
        Page<StandardGoodsSpecificationDTO> page = standardGoodsSpecificationApi.getSpecificationPage(request);
        if (page != null) {
            GoodsAuditDTO goodsAuditDTO = goodsAuditApi.getById(form.getId());
            StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(form.getStandardId());
            GoodsAuditPageVO<StandardSpecificationInfoVO> newPage = new GoodsAuditPageVO();
            newPage.setRecords(page.getRecords());
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            newPage.setGoodsAuditInfoVO(PojoUtils.map(goodsAuditDTO, GoodsAuditListVO.class));
            newPage.setStandardGoodsInfoVO(PojoUtils.map(standardGoodsByIdInfoDTO.getBaseInfo(), StandardGoodsInfoVO.class));
            return Result.success(newPage);
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "新增标准库商品跳转", httpMethod = "GET")
    @GetMapping("jumpStandardGoods")
    public Result<StandardGoodsAllInfoVO> jumpStandardGoods(@RequestParam("id") Long id) {
        GoodsFullDTO goodsFullDTO = goodsApi.queryFullInfo(id);
        StandardGoodsAllInfoVO result = PojoUtils.map(goodsFullDTO.getStandardGoodsAllInfo(), StandardGoodsAllInfoVO.class);
        if(null!=goodsFullDTO){
            goodsFullDTO.setId(null);
        }
        result.setBaseInfo(PojoUtils.map(goodsFullDTO, StandardGoodsBasicInfoVO.class));
        if (CollectionUtil.isNotEmpty(result.getPicBasicsInfoList())) {
            for (StandardGoodsPicVO one : result.getPicBasicsInfoList()) {
                one.setPicUrl(pictureUrlUtils.getGoodsPicUrl(one.getPic()));
            }
        }
        if (CollectionUtil.isNotEmpty(result.getSpecificationInfo())) {
            for (StandardSpecificationInfoVO one : result.getSpecificationInfo()) {
                if (CollectionUtil.isNotEmpty(one.getPicInfoList())) {
                    for (StandardGoodsPicVO picOne : one.getPicInfoList()) {
                        picOne.setPicUrl(pictureUrlUtils.getGoodsPicUrl(picOne.getPic()));
                    }
                }
            }
        }
        return Result.success(result);
    }

}
