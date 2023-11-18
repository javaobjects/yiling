package com.yiling.admin.data.center.report.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
import com.yiling.admin.data.center.report.form.AddReportParamSubForm;
import com.yiling.admin.data.center.report.form.ImportParMemberForm;
import com.yiling.admin.data.center.report.form.QueryEntInfoPageListForm;
import com.yiling.admin.data.center.report.form.QueryReportGoodsCategoryPageForm;
import com.yiling.admin.data.center.report.form.QueryReportParPageForm;
import com.yiling.admin.data.center.report.form.UpdateReportParamSubForm;
import com.yiling.admin.data.center.report.handler.ImportParMemberHandler;
import com.yiling.admin.data.center.report.vo.EntInfoPageListItemVO;
import com.yiling.admin.data.center.report.vo.GoodsInfoVO;
import com.yiling.admin.data.center.report.vo.MemberSimpleVO;
import com.yiling.admin.data.center.report.vo.ReportParamSubVO;
import com.yiling.admin.data.center.report.vo.ReportParamVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.vo.ImportResultVO;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsLineEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.settlement.report.api.ReportParamApi;
import com.yiling.settlement.report.dto.ReportParamDTO;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.request.AddReportSubParamRequest;
import com.yiling.settlement.report.dto.request.QueryReportParPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportParamSubPageListRequest;
import com.yiling.settlement.report.enums.ReportParamTypeEnum;
import com.yiling.settlement.report.enums.ReportRewardTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
@RestController
@RequestMapping("/report/param")
@Api(tags = "报表参数")
@Slf4j
public class ParamController extends BaseController {
    @DubboReference
    ReportParamApi reportParamApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    MemberApi memberApi;

    @Autowired
    ImportParMemberHandler importParMemberHandler;


    @ApiOperation(value = "参数列表")
    @GetMapping("/queryReportParamPage")
    public Result<Page<ReportParamVO>> queryReportParamPage(QueryReportParPageForm queryReportParPageForm) {
        QueryReportParPageRequest queryReportParPageRequest = new QueryReportParPageRequest();
        PojoUtils.map(queryReportParPageForm, queryReportParPageRequest);
        Page<ReportParamDTO> reportParamDTOPage = reportParamApi.queryReportParamPage(queryReportParPageRequest);
        Page<ReportParamVO> reportParamVOPage = PojoUtils.map(reportParamDTOPage, ReportParamVO.class);
        return Result.success(reportParamVOPage);
    }

    @ApiOperation(value = "查询商品")
    @GetMapping("/queryGoods")
    public Result<CollectionObject<List<GoodsInfoVO>>> queryGoods(@RequestParam @Valid @NotEmpty String key) {
        // 出货价、供货价、商品类型
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setSize(100);
        request.setName(key);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);

        List<GoodsInfoVO> list = PojoUtils.map(page.getRecords(), GoodsInfoVO.class);
        list.forEach(o -> {
            o.setSellSpecificationsId(0L);
        });
        CollectionObject<List<GoodsInfoVO>> result = new CollectionObject(list);
        return Result.success(result);
    }

    @ApiOperation(value = "查询商品规格")
    @GetMapping("/queryGoodsSpecification")
    public Result<CollectionObject<List<GoodsInfoVO>>> queryGoodsSpecification(@RequestParam @Valid @NotEmpty String key) {
        // 商销价
        List<GoodsInfoVO> list = new ArrayList<>();
        // 标准库规格
        StandardSpecificationPageRequest specificationRequest = new StandardSpecificationPageRequest();
        specificationRequest.setSize(100);
        specificationRequest.setStandardGoodsName(key);
        Page<StandardGoodsSpecificationDTO> specificationPage = standardGoodsSpecificationApi.getSpecificationPageByGoods(specificationRequest);
        if (ObjectUtil.isNull(specificationPage) || CollUtil.isEmpty(specificationPage.getRecords())) {
            return Result.success(new CollectionObject(list));
        }
        for (StandardGoodsSpecificationDTO standardGoodsSpecificationDTO : specificationPage.getRecords()) {
            GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
            goodsInfoVO.setName(standardGoodsSpecificationDTO.getName());
            goodsInfoVO.setSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            goodsInfoVO.setSellSpecificationsId(standardGoodsSpecificationDTO.getId());
            goodsInfoVO.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
            goodsInfoVO.setSpecifications("");
            goodsInfoVO.setId(0L);
            list.add(goodsInfoVO);
        }
        // 以岭品
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setSize(200);
        request.setName(key);
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        Page<GoodsListItemBO> ylGoodsPage = goodsApi.queryPageListGoods(request);
        if (ObjectUtil.isNull(ylGoodsPage) || CollUtil.isEmpty(ylGoodsPage.getRecords())) {
            return Result.success(new CollectionObject(list));
        }
        Map<Long, GoodsListItemBO> ylGoodsMap = ylGoodsPage.getRecords().stream().collect(Collectors.toMap(GoodsListItemBO::getSellSpecificationsId, Function.identity(), (v1, v2) -> v1));
        for (GoodsInfoVO goodsInfoVO : list) {
            GoodsListItemBO goodsListItemBO = ylGoodsMap.get(goodsInfoVO.getSellSpecificationsId());
            if (ObjectUtil.isNotNull(goodsListItemBO)) {
                goodsInfoVO.setId(goodsListItemBO.getId());
            }
        }

        CollectionObject<List<GoodsInfoVO>> result = new CollectionObject(list);
        return Result.success(result);
    }

    @ApiOperation(value = "根据规格id查询以岭品")
    @GetMapping("/queryYlGoods")
    public Result<GoodsInfoVO> queryYlGoods(@RequestParam @Valid @NotNull Long sellSpecificationsId) {
        // 商品规格
        StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsSpecificationApi.getStandardGoodsSpecification(sellSpecificationsId);
        if (ObjectUtil.isNull(standardGoodsSpecificationDTO)) {
            return Result.success(null);
        }
        GoodsInfoVO result = new GoodsInfoVO();
        result.setName(standardGoodsSpecificationDTO.getName());
        result.setSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
        result.setSellSpecificationsId(standardGoodsSpecificationDTO.getId());
        result.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
        result.setSpecifications("");
        result.setId(0L);
        // 以岭品id
        QueryGoodsPageListRequest request = new QueryGoodsPageListRequest();
        request.setIncludeSellSpecificationsIds(ListUtil.toList(sellSpecificationsId));
        request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        request.setGoodsLine(GoodsLineEnum.B2B.getCode());
        List<GoodsListItemBO> goodsList = b2bGoodsApi.getB2bGoodsBySellSpecificationsIdsAndEids(request);
        if (CollUtil.isEmpty(goodsList)) {
            return Result.success(result);
        }
        result.setId(goodsList.get(0).getId());
        return Result.success(result);
    }

    @ApiOperation(value = "查询子参数列表")
    @PostMapping("/queryReportParamSubPageList")
    public Result<Page<ReportParamSubVO>> queryReportParamSubPageList(@RequestBody @Valid QueryReportGoodsCategoryPageForm form) {
        QueryReportParamSubPageListRequest request = PojoUtils.map(form, QueryReportParamSubPageListRequest.class);
        //如果是会员类型
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.MEMBER.getCode()) && ObjectUtil.isNotNull(form.getEid()) && ObjectUtil.notEqual(form.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            request.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.MEMBER.getCode()) && StrUtil.isNotBlank(form.getUserName())) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(form.getUserName());
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            request.setUpdateUser(staffList.stream().map(Staff::getId).collect(Collectors.toList()));
        }

        Page<ReportParamSubDTO> page = reportParamApi.queryReportParamSubPageList(request);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Result.success(request.getPage());
        }
        Page<ReportParamSubVO> result = PojoUtils.map(page, ReportParamSubVO.class);

        //如果是会员类型
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.MEMBER.getCode())) {
            //企业
            List<Long> eidList = page.getRecords().stream().map(ReportParamSubDTO::getEid).collect(Collectors.toList());
            Map<Long, String> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
            //会员名称
            List<Long> memberIdList = page.getRecords().stream().map(ReportParamSubDTO::getMemberId).collect(Collectors.toList());
            Map<Long, String> memberMap = memberApi.listByIds(memberIdList).stream().collect(Collectors.toMap(MemberDTO::getId, MemberDTO::getName));

            result.getRecords().forEach(e -> {
                e.setEName(entMap.getOrDefault(e.getEid(), Constants.SEPARATOR_MIDDLELINE));
                e.setMemberName(memberMap.getOrDefault(e.getMemberId(), Constants.SEPARATOR_MIDDLELINE));
            });
        }
        //查询操作人
        List<Long> userIds = page.getRecords().stream().map(ReportParamSubDTO::getUpdateUser).collect(Collectors.toList());
        Map<Long, String> userDTOMap = userApi.listByIds(userIds).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));

        result.getRecords().forEach(e -> {
            e.setUpdateUserName(userDTOMap.getOrDefault(e.getUpdateUser(), Constants.SEPARATOR_MIDDLELINE));
        });

        return Result.success(result);
    }

    @ApiOperation(value = "根据企业名称查询企业信息")
    @PostMapping("/queryEntInfoPageList")
    public Result<Page<EntInfoPageListItemVO>> queryEntInfoPageList(@RequestBody @Valid QueryEntInfoPageListForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        return Result.success(PojoUtils.map(page, EntInfoPageListItemVO.class));
    }

    @ApiOperation(value = "添加子参数--仅会员和商品分类类型的参数可以添加")
    @PostMapping("/addReportParamSub")
    public Result<Boolean> addReportParamSub(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid AddReportParamSubForm form) {
        if (ObjectUtil.notEqual(form.getParType(), ReportParamTypeEnum.GOODS.getCode()) && ObjectUtil.notEqual(form.getParType(), ReportParamTypeEnum.MEMBER.getCode())) {
            return Result.failed("仅会员和商品分类类型的参数可以添加");
        }
        AddReportSubParamRequest request = PojoUtils.map(form, AddReportSubParamRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());
        //如果是添加商品类型
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.GOODS.getCode())) {
            request.setName(form.getName());
            return Result.success(reportParamApi.saveOrUpdateReportSubParam(request));
        }
        //如果是会员类型
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.MEMBER.getCode())) {
            if (ObjectUtil.isNull(form.getMemberSource())||ObjectUtil.equal(form.getMemberSource(),0)) {
                return Result.failed("数据来源不能为空");
            }
            if (ObjectUtil.isNull(form.getMemberId())||ObjectUtil.equal(form.getMemberId(),0L)) {
                return Result.failed("会员id不能为空");
            }
            if (ObjectUtil.isNull(form.getEid()) || ObjectUtil.equal(form.getEid(), 0L)) {
                return Result.failed("商业eid不能为空");
            }
            //如果是奖励百分比类型rewardAmount=会员售价*百分比
            if (ObjectUtil.equal(ReportRewardTypeEnum.PERCENTAGE.getCode(), form.getRewardType())) {
                request.setRewardPercentage(form.getRewardValue());
            } else {
                request.setRewardAmount(form.getRewardValue());
            }
            request.setStartTime(form.getStartTime());
            request.setEndTime(form.getEndTime());
            return Result.success(reportParamApi.saveOrUpdateReportSubMemberParam(request));
        }
        return Result.failed("不支持除分类和会员以外的子参数新增和修改");
    }

    @ApiOperation(value = "修改子参数--仅会员和商品分类类型的参数可以修改")
    @PostMapping("/updateReportParamSub")
    public Result<Boolean> updateReportParamSub(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid UpdateReportParamSubForm form) {
        if (ObjectUtil.isNull(form.getId()) || ObjectUtil.equal(form.getId(), 0L)) {
            return Result.failed("子参数id不能为空");
        }
        if (ObjectUtil.notEqual(form.getParType(), ReportParamTypeEnum.GOODS.getCode()) && ObjectUtil.notEqual(form.getParType(), ReportParamTypeEnum.MEMBER.getCode())) {
            return Result.failed("仅会员和商品分类类型的参数可以修改");
        }
        ReportParamSubDTO paramSubDTO = reportParamApi.queryReportParamSubById(form.getId());
        if (ObjectUtil.isNull(paramSubDTO)) {
            return Result.failed("子参数不存在");
        }
        AddReportSubParamRequest request = PojoUtils.map(form, AddReportSubParamRequest.class);
        request.setId(form.getId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setOpTime(new Date());

        //如果是添加商品类型
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.GOODS.getCode())) {
            request.setName(form.getName());
            return Result.success(reportParamApi.saveOrUpdateReportSubParam(request));
        }
        //如果是会员类型
        if (ObjectUtil.equal(form.getParType(), ReportParamTypeEnum.MEMBER.getCode())) {
            request.setThresholdAmount(form.getThresholdAmount());
            BigDecimal rewardValue = form.getRewardValue();
            if (ObjectUtil.equal(ReportRewardTypeEnum.PERCENTAGE.getCode(), form.getRewardType())) {
                request.setRewardPercentage(rewardValue);
            } else {
                request.setRewardAmount(rewardValue);
            }
            request.setStartTime(form.getStartTime());
            request.setEndTime(form.getEndTime());
            return Result.success(reportParamApi.saveOrUpdateReportSubMemberParam(request));
        }
        return Result.failed("不支持除分类和会员以外的子参数新增和修改");
    }

    @ApiOperation(value = "导入会员参数")
    @PostMapping("/importMemberPar")
    @Log(title = "导入会员参数", businessType = BusinessTypeEnum.IMPORT)
    public Result<ImportResultVO> importMemberPar(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("file") MultipartFile file) {
        importParMemberHandler.initPar(8L, ReportParamTypeEnum.MEMBER);
        ImportParams params = new ImportParams();
        params.setNeedSave(false);
        params.setNeedVerify(true);
        params.setVerifyHandler(importParMemberHandler);

        InputStream in;
        try {
            in = file.getInputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.UPLOAD_FILE_FAILED);
        }

        ImportResultModel importResultModel;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(MyMetaHandler.FIELD_OP_USER_ID, adminInfo.getCurrentUserId());
            importResultModel = ExeclImportUtils.importExcelMore(in, ImportParMemberForm.class, params, importParMemberHandler, paramMap);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed(ResultCode.EXCEL_PARSING_ERROR);
        }

        return Result.success(PojoUtils.map(importResultModel, ImportResultVO.class));
    }

    @ApiOperation(value = "获取所有会员")
    @GetMapping("/getMemberList")
    public Result<CollectionObject<MemberSimpleVO>> getMemberList(@CurrentUser CurrentAdminInfo adminInfo) {
        List<MemberSimpleDTO> simpleDTOList = memberApi.queryAllList();
        return Result.success(new CollectionObject<>(PojoUtils.map(simpleDTOList, MemberSimpleVO.class)));
    }
}