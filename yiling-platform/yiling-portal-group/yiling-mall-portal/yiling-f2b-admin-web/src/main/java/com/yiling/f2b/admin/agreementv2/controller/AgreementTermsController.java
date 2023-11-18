package com.yiling.f2b.admin.agreementv2.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
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
import com.yiling.f2b.admin.agreementv2.form.CreateAgreementForm;
import com.yiling.f2b.admin.agreementv2.form.ImportAgreementGoodsForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementAuthPageForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementExistForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementGoodsPageForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementPageListForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementSecondUserPageForm;
import com.yiling.f2b.admin.agreementv2.form.QueryAgreementSupplyGoodsPageForm;
import com.yiling.f2b.admin.agreementv2.form.QueryBusinessPageForm;
import com.yiling.f2b.admin.agreementv2.form.QueryFirstPartyPageForm;
import com.yiling.f2b.admin.agreementv2.form.QueryImportAgreementListForm;
import com.yiling.f2b.admin.agreementv2.form.QueryUserInfoForm;
import com.yiling.f2b.admin.agreementv2.form.SaveAgreementSecondUserForm;
import com.yiling.f2b.admin.agreementv2.form.UpdateArchiveAgreementForm;
import com.yiling.f2b.admin.agreementv2.form.UpdateAuthAgreementForm;
import com.yiling.f2b.admin.agreementv2.handler.ImportAgreementGoodsHandler;
import com.yiling.f2b.admin.agreementv2.vo.AgreementAuthListItemVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementDetailVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementGoodsListItemVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementImportListItemVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementImportMainTermsVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementListItemVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementSecondUserVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementSignUserVO;
import com.yiling.f2b.admin.agreementv2.vo.AgreementSupplySalesGoodsVO;
import com.yiling.f2b.admin.agreementv2.vo.BusinessEnterpriseItemVO;
import com.yiling.f2b.admin.agreementv2.vo.EnterpriseItemVO;
import com.yiling.f2b.admin.agreementv2.vo.FirstPartyListItemVO;
import com.yiling.f2b.admin.agreementv2.vo.GoodsListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.user.agreementv2.api.AgreementManufacturerApi;
import com.yiling.user.agreementv2.api.AgreementTermsApi;
import com.yiling.user.agreementv2.bo.AgreementAllProductFormBO;
import com.yiling.user.agreementv2.bo.AgreementAttachmentBO;
import com.yiling.user.agreementv2.bo.AgreementAuthListItemBO;
import com.yiling.user.agreementv2.bo.AgreementDetailBO;
import com.yiling.user.agreementv2.bo.AgreementGoodsListItemBO;
import com.yiling.user.agreementv2.bo.AgreementImportListItemBO;
import com.yiling.user.agreementv2.bo.AgreementListItemBO;
import com.yiling.user.agreementv2.bo.AgreementRebateTimeSegmentBO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerDTO;
import com.yiling.user.agreementv2.dto.AgreementManufacturerGoodsDTO;
import com.yiling.user.agreementv2.dto.AgreementSecondUserDTO;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesGoodsDTO;
import com.yiling.user.agreementv2.dto.request.CreateAgreementRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementAuthPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementExistRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementManufacturerRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementPageListRequest;
import com.yiling.user.agreementv2.dto.request.QueryAgreementSecondUserPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryFirstPartyPageRequest;
import com.yiling.user.agreementv2.dto.request.QueryImportAgreementListRequest;
import com.yiling.user.agreementv2.dto.request.SaveAgreementSecondUserRequest;
import com.yiling.user.agreementv2.dto.request.UpdateArchiveAgreementRequest;
import com.yiling.user.agreementv2.dto.request.UpdateAuthAgreementRequest;
import com.yiling.user.agreementv2.enums.AgreementAuthStatusEnum;
import com.yiling.user.agreementv2.enums.AgreementFirstTypeEnum;
import com.yiling.user.agreementv2.enums.ManufacturerTypeEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.request.QueryStaffPageListRequest;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 协议主条款表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-23
 */
@Slf4j
@Api(tags = "协议V2.0协议条款")
@RestController
@RequestMapping("/agreementTerms")
public class AgreementTermsController extends BaseController {

    @DubboReference
    AgreementTermsApi agreementTermsApi;
    @DubboReference
    AgreementManufacturerApi agreementManufacturerApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    PopGoodsApi popGoodsApi;

    @Autowired
    ImportAgreementGoodsHandler importAgreementGoodsHandler;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "新建协议")
    @PostMapping("/createAgreement")
    @Log(title = "新建协议", businessType = BusinessTypeEnum.INSERT)
    public Result<Void> createAgreement(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CreateAgreementForm form) {
        CreateAgreementRequest request = PojoUtils.map(form, CreateAgreementRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        agreementTermsApi.createAgreement(request);
        return Result.success();
    }

    @ApiOperation(value = "查询甲方/乙方")
    @PostMapping("/queryFirstParty")
    public Result<Page<FirstPartyListItemVO>> queryFirstParty(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryFirstPartyPageForm form) {
        QueryFirstPartyPageRequest request = PojoUtils.map(form, QueryFirstPartyPageRequest.class);
        Page<FirstPartyListItemVO> voPage;
        // 选择甲方时：根据甲方类型，带出对应过滤表单
        if (AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER == AgreementFirstTypeEnum.getByCode(request.getFirstType())
                || AgreementFirstTypeEnum.INDUSTRIAL_BRAND == AgreementFirstTypeEnum.getByCode(request.getFirstType())) {
            // 查询厂家管理
            QueryAgreementManufacturerRequest manufacturerRequest = PojoUtils.map(form, QueryAgreementManufacturerRequest.class);
            manufacturerRequest.setEname(request.getEname());
            Page<AgreementManufacturerDTO> manufacturerDtoPage = agreementManufacturerApi.queryManufacturerListPage(manufacturerRequest);
            voPage = PojoUtils.map(manufacturerDtoPage, FirstPartyListItemVO.class);

        } else {
            //查询中台企业数据
            QueryEnterprisePageListRequest pageListRequest = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
            pageListRequest.setName(request.getEname());
            pageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            pageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            Page<EnterpriseDTO> enterpriseDtoPage = enterpriseApi.pageList(pageListRequest);
            List<FirstPartyListItemVO> partyListItemVoList = enterpriseDtoPage.getRecords().stream().map(enterpriseDTO -> {
                FirstPartyListItemVO firstPartyListItemVO = new FirstPartyListItemVO();
                firstPartyListItemVO.setEid(enterpriseDTO.getId());
                firstPartyListItemVO.setEname(enterpriseDTO.getName());
                firstPartyListItemVO.setType(3);
                return firstPartyListItemVO;
            }).collect(Collectors.toList());

            voPage = PojoUtils.map(enterpriseDtoPage, FirstPartyListItemVO.class);
            voPage.setRecords(partyListItemVoList);
        }

        return Result.success(voPage);
    }

    @ApiOperation(value = "查询协议是否存在，存在则返回协议编码")
    @PostMapping("/checkAgreementExist")
    public Result<String> checkAgreementExist(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementExistForm form) {
        QueryAgreementExistRequest request = PojoUtils.map(form, QueryAgreementExistRequest.class);
        String agreementNo = agreementTermsApi.checkAgreementExist(request);
        return Result.success(agreementNo);
    }

    @ApiOperation(value = "查询甲方协议签订人")
    @PostMapping("/queryStaffPage")
    public Result<Page<AgreementSignUserVO>> queryStaffPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryUserInfoForm form) {
        QueryStaffPageListRequest request = PojoUtils.map(form, QueryStaffPageListRequest.class);

        Page<Staff> page = staffApi.pageList(request);
        List<AgreementSignUserVO> signUserVoList = page.getRecords().stream().map(staff -> {
            AgreementSignUserVO agreementSignUserVO = new AgreementSignUserVO();
            agreementSignUserVO.setUserId(staff.getId());
            agreementSignUserVO.setUserName(staff.getName());
            agreementSignUserVO.setMobile(staff.getMobile());
            agreementSignUserVO.setEmail(staff.getEmail());
            return agreementSignUserVO;
        }).collect(Collectors.toList());

        Page<AgreementSignUserVO> voPage = PojoUtils.map(page, AgreementSignUserVO.class);
        voPage.setRecords(signUserVoList);

        return Result.success(voPage);
    }

    @ApiOperation(value = "查询指定商业公司")
    @PostMapping("/queryBusinessPage")
    public Result<Page<BusinessEnterpriseItemVO>> queryBusinessPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryBusinessPageForm form) {
        Page<BusinessEnterpriseItemVO> voPage;
        //查询中台企业数据
        QueryEnterprisePageListRequest pageListRequest = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        pageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        pageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        pageListRequest.setName(form.getEname());
        Page<EnterpriseDTO> enterpriseDtoPage = enterpriseApi.pageList(pageListRequest);

        List<BusinessEnterpriseItemVO> enterpriseItemVoList = enterpriseDtoPage.getRecords().stream().map(enterpriseDTO -> {
            BusinessEnterpriseItemVO enterpriseItemVO = new BusinessEnterpriseItemVO();
            enterpriseItemVO.setEid(enterpriseDTO.getId());
            enterpriseItemVO.setEname(enterpriseDTO.getName());
            enterpriseItemVO.setType(enterpriseDTO.getType());
            enterpriseItemVO.setAddress(new StringJoiner(" ").add(enterpriseDTO.getProvinceName()).add(enterpriseDTO.getCityName())
                    .add(enterpriseDTO.getRegionName()).toString());
            return enterpriseItemVO;
        }).collect(Collectors.toList());

        voPage = PojoUtils.map(enterpriseDtoPage, BusinessEnterpriseItemVO.class);
        voPage.setRecords(enterpriseItemVoList);

        return Result.success(voPage);
    }

    @ApiOperation(value = "查询乙方协议签订人")
    @PostMapping("/querySecondUserPage")
    public Result<Page<AgreementSecondUserVO>> querySecondUserPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementSecondUserPageForm form) {
        QueryAgreementSecondUserPageRequest request = PojoUtils.map(form, QueryAgreementSecondUserPageRequest.class);
        Page<AgreementSecondUserDTO> secondUserDtoPage = agreementTermsApi.querySecondUserListPage(request);

        return Result.success(PojoUtils.map(secondUserDtoPage, AgreementSecondUserVO.class));
    }

    @ApiOperation(value = "新建/修改乙方协议签订人")
    @PostMapping("/saveAgreementSecondUser")
    @Log(title = "新建/修改乙方协议签订人", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> saveAgreementSecondUser(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveAgreementSecondUserForm form) {
        SaveAgreementSecondUserRequest request = PojoUtils.map(form, SaveAgreementSecondUserRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        agreementTermsApi.saveAgreementSecondUser(request);

        return Result.success();
    }

    @ApiOperation(value = "删除乙方协议签订人")
    @GetMapping("/deleteAgreementSecondUser")
    @Log(title = "删除乙方协议签订人", businessType = BusinessTypeEnum.DELETE)
    public Result<Void> deleteAgreementSecondUser(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("id") Long id) {
        agreementTermsApi.deleteAgreementSecondUser(id, staffInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "返利条款页面选择商品分页列表（修改页面需要用到，暂时隐藏）", hidden = true)
    @PostMapping("/querySupplyGoodsPage")
    public Result<Page<AgreementSupplySalesGoodsVO>> querySupplyGoodsPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementSupplyGoodsPageForm form) {
        QueryAgreementGoodsPageRequest request = PojoUtils.map(form, QueryAgreementGoodsPageRequest.class);
        Page<AgreementSupplySalesGoodsDTO> itemBoPage = agreementTermsApi.querySupplyGoodsPage(request);
        Page<AgreementSupplySalesGoodsVO> voPage = PojoUtils.map(itemBoPage, AgreementSupplySalesGoodsVO.class);

        List<AgreementSupplySalesGoodsVO> pageRecords = voPage.getRecords();
        if (CollUtil.isEmpty(pageRecords)) {
            return Result.success(voPage);
        }
        // 获取规格ID集合
        List<Long> specificationIdList = pageRecords.stream().map(AgreementSupplySalesGoodsVO::getSpecificationGoodsId).distinct().collect(Collectors.toList());
        Map<Long, List<AgreementManufacturerGoodsDTO>> goodsSpecificationMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(specificationIdList)) {
            goodsSpecificationMap = agreementManufacturerApi.getGoodsBySpecificationId(specificationIdList);
        }

        // 获取厂家集合
        Set<Long> manufacturerIdSet = new HashSet<>();
        goodsSpecificationMap.forEach((specificationId, goodsList) -> goodsList.forEach(
                agreementManufacturerGoodsDTO -> manufacturerIdSet.add(agreementManufacturerGoodsDTO.getManufacturerId())));
        Map<Long, AgreementManufacturerDTO> manufacturerDoMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(manufacturerIdSet)) {
            manufacturerDoMap = agreementManufacturerApi.listByIds(ListUtil.toList(manufacturerIdSet)).stream().collect(Collectors.toMap(AgreementManufacturerDTO::getId, Function.identity()));
        }

        for (AgreementSupplySalesGoodsVO supplySalesGoodsVO : pageRecords) {
            List<AgreementManufacturerGoodsDTO> manufacturerGoodsDTOList = goodsSpecificationMap.get(supplySalesGoodsVO.getSpecificationGoodsId());
            if (CollUtil.isNotEmpty(manufacturerGoodsDTOList)) {
                for (AgreementManufacturerGoodsDTO agreementManufacturerGoodsDTO : manufacturerGoodsDTOList) {
                    AgreementManufacturerDTO manufacturerDO = manufacturerDoMap.get(agreementManufacturerGoodsDTO.getManufacturerId());
                    if (Objects.nonNull(manufacturerDO) && (ManufacturerTypeEnum.PRODUCER == ManufacturerTypeEnum.getByCode(manufacturerDO.getType()))) {
                        supplySalesGoodsVO.setProducerManufacturer(manufacturerDO.getEname());
                    } else if(Objects.nonNull(manufacturerDO) && (ManufacturerTypeEnum.BRAND == ManufacturerTypeEnum.getByCode(manufacturerDO.getType()))) {
                        supplySalesGoodsVO.setBrandManufacturer(manufacturerDO.getEname());
                    }
                }
            }
        }

        return Result.success(voPage);
    }

    @ApiOperation(value = "查询协议分页列表")
    @PostMapping("/queryAgreementPage")
    public Result<Page<AgreementListItemVO>> queryAgreementPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementPageListForm form) {
        QueryAgreementPageListRequest request = PojoUtils.map(form, QueryAgreementPageListRequest.class);
        Page<AgreementListItemBO> agreementPage = agreementTermsApi.queryAgreementPage(request);
        Page<AgreementListItemVO> voPage = PojoUtils.map(agreementPage, AgreementListItemVO.class);
        voPage.getRecords().forEach(agreementListItemVO -> agreementListItemVO.setSecondType("商业-供应商"));
        return Result.success(voPage);
    }

    @ApiOperation(value = "查询协议商品列表")
    @PostMapping("/queryAgreementGoodsPage")
    public Result<Page<AgreementGoodsListItemVO>> queryAgreementGoodsPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementGoodsPageForm form) {
        QueryAgreementGoodsPageRequest request = PojoUtils.map(form, QueryAgreementGoodsPageRequest.class);
        Page<AgreementGoodsListItemBO> itemBoPage = agreementTermsApi.queryAgreementGoodsPage(request);
        return Result.success(PojoUtils.map(itemBoPage, AgreementGoodsListItemVO.class));
    }

    @ApiOperation(value = "获取协议详情")
    @GetMapping("/getDetail")
    public Result<AgreementDetailVO> getDetail(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "协议ID", required = true) @RequestParam("id") Long id) {
        AgreementDetailBO agreementDetailBO = agreementTermsApi.getDetail(id);
        List<AgreementAttachmentBO> agreementAttachmentList = agreementDetailBO.getAgreementMainTerms().getAgreementAttachmentList();
        if (CollUtil.isNotEmpty(agreementAttachmentList)) {
            agreementAttachmentList.forEach(agreementAttachmentBO -> agreementAttachmentBO.setFileUrl(fileService.getUrl(agreementAttachmentBO.getFileKey(), FileTypeEnum.AGREEMENT_ATTACHMENT)));
        }

        // 获取当前商品数
        Long goodsNumber = getGoodsNumber(id, agreementDetailBO.getAgreementSupplySalesTerms().getAllLevelKindsFlag(),
                agreementDetailBO.getAgreementMainTerms().getFirstType(), agreementDetailBO.getAgreementMainTerms().getEid());

        if (agreementDetailBO.getAgreementRebateTerms().getReserveSupplyFlag() == 0) {
            List<AgreementRebateTimeSegmentBO> rebateTimeSegmentList = agreementDetailBO.getAgreementRebateTerms().getAgreementRebateTimeSegmentList();
            rebateTimeSegmentList.forEach(agreementRebateTimeSegmentBO -> {
                List<AgreementAllProductFormBO> allProductFormList = agreementRebateTimeSegmentBO.getAllProductFormList();
                allProductFormList.forEach(agreementAllProductFormBO -> agreementAllProductFormBO.setGoodsNumber(goodsNumber));
            });
        }

        return Result.success(PojoUtils.map(agreementDetailBO, AgreementDetailVO.class));
    }

    @ApiOperation(value = "查询协议审核列表")
    @PostMapping("/queryAgreementAuthPage")
    public Result<Page<AgreementAuthListItemVO>> queryAgreementAuthPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementAuthPageForm form) {
        QueryAgreementAuthPageRequest request = PojoUtils.map(form, QueryAgreementAuthPageRequest.class);
        Page<AgreementAuthListItemBO> itemBoPage = agreementTermsApi.queryAgreementAuthPage(request);
        Page<AgreementAuthListItemVO> voPage = PojoUtils.map(itemBoPage, AgreementAuthListItemVO.class);
        voPage.getRecords().forEach(agreementAuthListItemVO -> {
            // 设置是否可以进行归档
            if (AgreementAuthStatusEnum.getByCode(agreementAuthListItemVO.getAuthStatus()) == AgreementAuthStatusEnum.PASS
                    && DateUtil.compare(new Date(), agreementAuthListItemVO.getEndTime()) > 0 ) {
                agreementAuthListItemVO.setArchiveFlag(true);
            }
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "协议审核")
    @PostMapping("/updateAuthAgreement")
    @Log(title = "协议审核", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateAuthAgreement(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateAuthAgreementForm form) {
        UpdateAuthAgreementRequest request = PojoUtils.map(form, UpdateAuthAgreementRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        agreementTermsApi.updateAuthAgreement(request);
        return Result.success();
    }

    @ApiOperation(value = "协议归档")
    @PostMapping("/updateArchiveAgreement")
    @Log(title = "协议归档", businessType = BusinessTypeEnum.UPDATE)
    public Result<Void> updateArchiveAgreement(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateArchiveAgreementForm form) {
        UpdateArchiveAgreementRequest request = PojoUtils.map(form, UpdateArchiveAgreementRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        agreementTermsApi.updateArchiveAgreement(request);
        return Result.success();
    }

    @ApiOperation(value = "协议导入选择协议列表")
    @PostMapping("/queryImportAgreementList")
    public Result<List<AgreementImportListItemVO>> queryImportAgreementList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryImportAgreementListForm form) {
        // 查询协议责任人为该账号的，已二审通过或已归档的协议
        QueryImportAgreementListRequest request = PojoUtils.map(form, QueryImportAgreementListRequest.class);
        request.setMainUserId(staffInfo.getCurrentUserId());
        request.setAuthStatusList(ListUtil.toList(AgreementAuthStatusEnum.PASS.getCode(), AgreementAuthStatusEnum.ARCHIVE.getCode()));
        List<AgreementImportListItemBO> boPage = agreementTermsApi.queryImportAgreementList(request);

        List<AgreementImportListItemVO> listItemVOS = boPage.stream().map(agreementImportListItemBO -> {
            // 协议附件
            agreementImportListItemBO.getAgreementAttachmentList().forEach(agreementAttachmentBO -> agreementAttachmentBO.setFileUrl(fileService.getUrl(agreementAttachmentBO.getFileKey(), FileTypeEnum.AGREEMENT_ATTACHMENT)));
            AgreementImportListItemVO importListItemVO = PojoUtils.map(agreementImportListItemBO, AgreementImportListItemVO.class);
            importListItemVO.setAgreementMainTerms(PojoUtils.map(agreementImportListItemBO, AgreementImportMainTermsVO.class));
            return importListItemVO;
        }).collect(Collectors.toList());

        listItemVOS.forEach(agreementImportListItemVO -> {
            // 获取当前商品数
            Long goodsNumber = getGoodsNumber(agreementImportListItemVO.getId(), agreementImportListItemVO.getAgreementSupplySalesTerms().getAllLevelKindsFlag(),
                    agreementImportListItemVO.getFirstType(), agreementImportListItemVO.getEid());

            agreementImportListItemVO.setGoodsNumber(goodsNumber.intValue());
        });

        return Result.success(listItemVOS);
    }

    @ApiOperation(value = "获取企业商品总数（如果甲方类型为生产厂家或品牌厂家则去厂家管理查询总商品数，否则查询中台商品库总数）")
    @GetMapping("/getGoodsNumberByEid")
    public Result<Long> getGoodsNumberByEid(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "企业ID", required = true) @RequestParam("eid") Long eid,
                                            @ApiParam(value = "甲方类型", required = true) @RequestParam("firstType") Integer firstType) {

        // 如果甲方类型为生产厂家或品牌厂家则去厂家管理查询总商品数，否则查询中台商品库数据
        Long goodsNumber = getNumber(firstType, eid);
        return Result.success(goodsNumber);
    }

    @ApiOperation(value = "获取子公司列表")
    @GetMapping("/getSubEnterpriseList")
    public Result<List<EnterpriseItemVO>> getSubEnterpriseList(@CurrentUser CurrentStaffInfo staffInfo,
                                                               @ApiParam(value = "乙方企业ID", required = true) @RequestParam("secondEid") Long secondEid,
                                                               @ApiParam(value = "子企业名称") @RequestParam(value = "name", required = false) String name) {
        List<EnterpriseDTO> list = enterpriseApi.listByParentId(secondEid);
        if (StrUtil.isNotEmpty(name)) {
            list = list.stream().filter(enterpriseDTO -> enterpriseDTO.getName().contains(name)).collect(Collectors.toList());
        }

        List<EnterpriseItemVO> itemVOList = list.stream().map(enterpriseDTO -> {
            EnterpriseItemVO enterpriseItemVO = new EnterpriseItemVO();
            enterpriseItemVO.setEid(enterpriseDTO.getId());
            enterpriseItemVO.setEname(enterpriseDTO.getName());
            return enterpriseItemVO;
        }).collect(Collectors.toList());

        return Result.success(itemVOList);
    }

    @ApiOperation(value = "协议商品信息导入", httpMethod = "POST")
    @PostMapping(value = "importAgreementGoods", headers = "content-type=multipart/form-data")
    @Log(title = "协议商品信息导入", businessType = BusinessTypeEnum.IMPORT)
    public Result<List<GoodsListItemVO>> importAgreementGoods(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "file") MultipartFile file,
                                                              @RequestParam(value = "firstType") Integer firstType, @RequestParam(value = "eid") Long eid) {
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importAgreementGoodsHandler);
        params.setKeyIndex(0);
        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel<ImportAgreementGoodsForm> importResultModel;
        try {
            // 包含了插入数据库失败的信息
            Map<String, Object> paramMap = new HashMap<>(16);
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, staffInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportAgreementGoodsForm.class, params, importAgreementGoodsHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }
        if (Objects.isNull(importResultModel)) {
            return Result.failed("导入信息失败");
        }
        List<ImportAgreementGoodsForm> modelList = importResultModel.getList();
        List<GoodsListItemVO> goodsListItemVOList = checkAgreementImportGoods(modelList, firstType, eid);
        if (CollUtil.isEmpty(goodsListItemVOList)) {
            return Result.failed("导入符合条件的商品为空");
        }
        return Result.success(goodsListItemVOList);
    }

    private List<GoodsListItemVO> checkAgreementImportGoods(List<ImportAgreementGoodsForm> modelList, Integer firstType, Long eid) {
        if (CollUtil.isEmpty(modelList)) {
            return ListUtil.toList();
        }

        StringBuilder sb = new StringBuilder();
        List<Long> goodsIdList = modelList.stream().map(ImportAgreementGoodsForm::getGoodsId).collect(Collectors.toList());
        List<StandardGoodsSpecificationDTO> specificationDTOList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(goodsIdList);
        Map<Long, StandardGoodsSpecificationDTO> specificationDtoMap = specificationDTOList.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));
        for (ImportAgreementGoodsForm goodsForm :modelList) {
            if (Objects.isNull(specificationDtoMap.get(goodsForm.getSpecificationGoodsId()))) {
                sb.append("规格商品ID：").append(goodsForm.getSpecificationGoodsId()).append("，商品名称：").append(goodsForm.getGoodsName()).append(" 不存在；");
            }
        }
        if (sb.length() > 0) {
            throw new BusinessException(UserErrorCode.AGREEMENT_SPECIFICATION_GOODS_EXIST, sb.toString());
        }

        // 如果甲方为品牌厂家或生产厂家，还要根据厂家管理中的关联商品校验关联性，如果不符合关联性，给出相应的提示
        if (AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER == AgreementFirstTypeEnum.getByCode(firstType)
                || AgreementFirstTypeEnum.INDUSTRIAL_BRAND == AgreementFirstTypeEnum.getByCode(firstType)) {
            AgreementManufacturerDTO manufacturerDTO = agreementManufacturerApi.getByEid(eid);

            List<Long> specificationGoodsIds = modelList.stream().map(ImportAgreementGoodsForm::getSpecificationGoodsId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(specificationGoodsIds)) {
                Map<Long, List<AgreementManufacturerGoodsDTO>> map = agreementManufacturerApi.getGoodsBySpecificationId(specificationGoodsIds);
                Set<Long> allManufacturerIds = new HashSet<>();
                map.forEach((specificationGoodsId, list) -> {
                    List<Long> manufacturerIds = list.stream().map(AgreementManufacturerGoodsDTO::getManufacturerId).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(manufacturerIds)) {
                        allManufacturerIds.addAll(manufacturerIds);
                    }
                });

                if (CollUtil.isNotEmpty(allManufacturerIds)) {
                    Map<Long, Integer> manufacturerMap = agreementManufacturerApi.listByIds(ListUtil.toList(allManufacturerIds)).stream().collect(Collectors.toMap(BaseDTO::getId, AgreementManufacturerDTO::getType));

                    StringBuilder buffer = new StringBuilder();
                    for (ImportAgreementGoodsForm agreementGoodsForm : modelList) {
                        List<Long> manufacturerIdList = map.get(agreementGoodsForm.getSpecificationGoodsId()).stream().map(AgreementManufacturerGoodsDTO::getManufacturerId)
                                .collect(Collectors.toList());
                        boolean flag = false;
                        for (Long id : manufacturerIdList) {
                            Integer type = manufacturerMap.get(id);
                            if (type != null && type.equals(manufacturerDTO.getType())) {
                               flag = true;
                               break;
                            }
                        }
                        if (!flag) {
                            buffer.append("规格商品ID：").append(agreementGoodsForm.getSpecificationGoodsId()).append("，商品名称：")
                                    .append(agreementGoodsForm.getGoodsName()).append("，不在当前厂家；");
                        }
                    }
                    if (buffer.length() > 0) {
                        throw new BusinessException(UserErrorCode.AGREEMENT_SPECIFICATION_GOODS_EXIST, buffer.toString());
                    }
                }
            }

        }
        return PojoUtils.map(modelList, GoodsListItemVO.class);
    }

    /**
     * 获取商品数量
     *
     * @param id
     * @param allLevelKindsFlag
     * @param firstType
     * @param eid
     * @return
     */
    private Long getGoodsNumber(Long id, Integer allLevelKindsFlag, Integer firstType, Long eid) {
        Long goodsNumber;
        if (allLevelKindsFlag == 0) {
            goodsNumber = agreementTermsApi.getSupplyGoodsNumber(id).longValue();
        } else {
            goodsNumber = getNumber(firstType, eid);
        }
        return goodsNumber;
    }

    /**
     * 如果甲方类型为生产厂家或品牌厂家则去厂家管理查询总商品数，否则查询中台商品库数据
     *
     * @param firstType 甲方类型
     * @param eid 甲方企业ID
     * @return
     */
    private Long getNumber(Integer firstType, Long eid) {
        Long goodsNumber;
        if (AgreementFirstTypeEnum.getByCode(firstType) == AgreementFirstTypeEnum.INDUSTRIAL_PRODUCER || AgreementFirstTypeEnum.getByCode(firstType) == AgreementFirstTypeEnum.INDUSTRIAL_BRAND) {
            goodsNumber = agreementManufacturerApi.getManufactureGoodsNumberByEid(eid).longValue();
        } else {
            EnterpriseGoodsCountBO goodsCountBO = Optional.ofNullable(popGoodsApi.getGoodsCountByEid(eid)).orElse(new EnterpriseGoodsCountBO());
            goodsNumber = goodsCountBO.getSellSpecificationCount();
        }
        return goodsNumber;
    }

}
