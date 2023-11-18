package com.yiling.sjms.agency.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.tianyancha.api.TycEnterpriseApi;
import com.yiling.basic.tianyancha.dto.TycEnterpriseInfoDTO;
import com.yiling.basic.tianyancha.dto.TycResultDTO;
import com.yiling.basic.tianyancha.dto.request.TycEnterpriseQueryRequest;
import com.yiling.basic.tianyancha.enums.TycErrorCode;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.request.SaveCrmEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.UnlockSaleBusinessApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.CheckAgencyDataForm;
import com.yiling.sjms.agency.form.QueryCrmEnterpriseInfoByNamePageListForm;
import com.yiling.sjms.agency.form.SaveCrmAgencyForm;
import com.yiling.sjms.agency.vo.CheckResultVO;
import com.yiling.sjms.agency.vo.CrmAgencyDetailsVO;
import com.yiling.sjms.agency.vo.CrmEnterpriseParentVO;
import com.yiling.sjms.agency.vo.CrmEnterprisePartVO;
import com.yiling.sjms.agency.vo.EnterpriseDisableVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机构档案基础接口
 *
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Slf4j
@RestController
@RequestMapping("/crm/agency/agency")
@Api(tags = "机构档案基础接口")
public class CrmAgencyController extends BaseController {

    @DubboReference
    TycEnterpriseApi      tycEnterpriseApi;
    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseApi      crmEnterpriseApi;
    @DubboReference
    CrmSupplierApi        crmSupplierApi;
    @DubboReference
    LocationApi           locationApi;
    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    CrmPharmacyApi crmPharmacyApi;
    @DubboReference
    UnlockSaleCustomerApi unlockSaleCustomerApi;
    @DubboReference
    UnlockSaleBusinessApi unlockSaleBusinessApi;

    @ApiOperation(value = "查询企业经营范围接口")
    @GetMapping("/getBusinessScope")
    public Result<String> getBusinessScope(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "name") String name) {
        TycEnterpriseQueryRequest request = new TycEnterpriseQueryRequest();
        request.setKeyword(name);
        TycResultDTO<TycEnterpriseInfoDTO> resultBO = tycEnterpriseApi.findEnterpriseByKeyword(request);
        if (TycErrorCode.OK.getCode().equals(resultBO.getError_code()) && null != resultBO.getResult()) {
            TycEnterpriseInfoDTO tycEnterpriseInfoDTO = resultBO.getResult();
            return Result.success(tycEnterpriseInfoDTO.getBusinessScope());
        } else if (TycErrorCode.NO_DATA.getCode().equals(resultBO.getError_code())) {
            return Result.failed("查询内容不存在");
        } else {
            return Result.failed("查询失败");
        }
    }

    @ApiOperation(value = "校验机构数据信息")
    @PostMapping("/checkData")
    public Result<CheckResultVO> checkData(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody CheckAgencyDataForm form) {
        CheckResultVO checkResultVO = new CheckResultVO();
        checkResultVO.setIsSuccess(true);
        QueryCrmAgencyCountRequest request;
        if (Objects.nonNull(form.getYlCode())) {
            // 检查以岭编码是否重复
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getId())) {
                request.setNotId(form.getId());
            }
            request.setYlCode(form.getYlCode());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的以岭编码" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
        }
        if (StringUtils.isNotBlank(form.getLicenseNumber())) {
            // 统一社会信用代码检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getId())) {
                request.setNotId(form.getId());
            }
            request.setLicenseNumber(form.getLicenseNumber());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的统一社会信用代码" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
        }
        if (StringUtils.isNotBlank(form.getName())) {
            // 机构名称重复检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getId())) {
                request.setNotId(form.getId());
            }
            request.setName(form.getName());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的机构名称" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
        }
        if (StringUtils.isNotBlank(form.getInstitutionPracticeLicense())) {
            // 医疗机构执业许可证重复检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getId())) {
                request.setNotId(form.getId());
            }
            request.setInstitutionPracticeLicense(form.getInstitutionPracticeLicense());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的编号" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
        }
        if (StringUtils.isNotBlank(form.getDistributionLicenseNumber())) {
            // 药品经营许可证编号重复检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getId())) {
                request.setNotId(form.getId());
            }
            request.setDistributionLicenseNumber(form.getDistributionLicenseNumber());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的编号" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
        }
        if (StringUtils.isNotBlank(form.getCode())) {
            // 药品经营许可证编号重复检查
            request = new QueryCrmAgencyCountRequest();
            if (Objects.nonNull(form.getId())) {
                request.setNotId(form.getId());
            }
            request.setCode(form.getCode());
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getFirstCrmEnterpriseInfo(request);
            if (Objects.nonNull(crmEnterpriseDTO)) {
                checkResultVO.setIsSuccess(false);
                checkResultVO.setErrorMessage("您输入的CRM编码" + getRoleString(crmEnterpriseDTO.getSupplyChainRole()) + "已存在");
                return Result.success(checkResultVO);
            }
        }
        return Result.success(checkResultVO);
    }

    @ApiOperation(value = "根据机构名称查询机构基本信息")
    @PostMapping("/name")
    public Result<Page<CrmAgencyDetailsVO>> getAgencyInfo(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseInfoByNamePageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        request.setFilterBySupplyChainRole(true);
        Page<CrmEnterpriseDTO> crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseByName(request);
        Page<CrmAgencyDetailsVO> voPage = PojoUtils.map(crmEnterpriseDTO, CrmAgencyDetailsVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("通过公司名称查询机构基础信息的id和名称--有效机构和无效机构都查询")
    @PostMapping("/getCrmEnterpriseIdAndNameByName")
    public Result<Page<CrmAgencyDetailsVO>> getCrmEnterpriseIdAndNameByName(@RequestBody QueryCrmEnterpriseInfoByNamePageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        request.setTableSuffix(Objects.nonNull(form.getYearMonth())? String.format("wash_%s",DateUtil.format(form.getYearMonth(),"yyyyMM")) :null);
        Page<CrmEnterpriseIdAndNameBO> boPage = crmEnterpriseApi.getCrmEnterpriseIdAndNameByName(request);
        Page<CrmAgencyDetailsVO> voPage = PojoUtils.map(boPage, CrmAgencyDetailsVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("通过公司名称查询机构部分基础信息--有效机构和无效机构都查询,有权限控制")
    @PostMapping("/getCrmEnterprisePartInfoByName")
    public Result<Page<CrmEnterprisePartVO>> getCrmEnterprisePartInfoByName(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseInfoByNamePageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        //默认带权限控制此接口
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("上级公司公共获取权限:empId={},longs={}", userInfo.getCurrentUserCode(), byEmpId);
        //代表没权限返回错误
        if (OrgDatascopeEnum.NONE == OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())) {
            log.info("上级公司公共获取权限:empId={}没有权限获取数据", userInfo.getCurrentUserCode());
            return Result.success(new Page<>());
        }
        request.setSjmsUserDatascopeBO(byEmpId);
        request.setTableSuffix(Objects.nonNull(form.getYearMonth())? String.format("wash_%s",DateUtil.format(form.getYearMonth(),"yyyyMM")) :null);
        Page<CrmEnterprisePartBO> boPage = crmEnterpriseApi.getCrmEnterprisePartInfoByName(request);
        if (CollectionUtil.isNotEmpty(boPage.getRecords())) {
            List<Long> crmEnterpriseId = boPage.getRecords().stream().filter(item -> item.getSupplyChainRole() == 3).map(CrmEnterprisePartBO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(crmEnterpriseId)) {
                List<CrmPharmacyDTO> crmPharmacyDTOS = crmPharmacyApi.listByCrmEnterpriseId(crmEnterpriseId);
                Map<Long, Integer> crmEnterpriseIdMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(crmPharmacyDTOS)) {
                    crmEnterpriseIdMap = crmPharmacyDTOS.stream().collect(Collectors.toMap(CrmPharmacyDTO::getCrmEnterpriseId, CrmPharmacyDTO::getTargetFlag));
                }
                Map<Long, Integer> finalCrmEnterpriseIdMap = crmEnterpriseIdMap;
                boPage.getRecords().forEach(item->{
                    item.setTargetFlag(finalCrmEnterpriseIdMap.get(item.getId()));
                });
            }
        }
        Page<CrmEnterprisePartVO> voPage = PojoUtils.map(boPage, CrmEnterprisePartVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("通过公司名称查询机构部分基础信息--有效机构和无效机构都查询，没有权限控制")
    @PostMapping("/getUnPermitCrmEnterprisePartInfoByName")
    public Result<Page<CrmEnterprisePartVO>> getEffectiveCrmEnterprisePartInfoByName(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseInfoByNamePageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        request.setTableSuffix(Objects.nonNull(form.getYearMonth())? String.format("wash_%s",DateUtil.format(form.getYearMonth(),"yyyyMM")) :null);
        Page<CrmEnterprisePartBO> boPage = crmEnterpriseApi.getCrmEnterprisePartInfoByName(request);
        if (CollectionUtil.isNotEmpty(boPage.getRecords())) {
            List<Long> crmEnterpriseId = boPage.getRecords().stream().filter(item -> item.getSupplyChainRole() == 3).map(CrmEnterprisePartBO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(crmEnterpriseId)) {
                List<CrmPharmacyDTO> crmPharmacyDTOS = crmPharmacyApi.listByCrmEnterpriseId(crmEnterpriseId);
                Map<Long, Integer> crmEnterpriseIdMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(crmPharmacyDTOS)) {
                    crmEnterpriseIdMap = crmPharmacyDTOS.stream().collect(Collectors.toMap(CrmPharmacyDTO::getCrmEnterpriseId, CrmPharmacyDTO::getTargetFlag));
                }
                Map<Long, Integer> finalCrmEnterpriseIdMap = crmEnterpriseIdMap;
                boPage.getRecords().forEach(item->{
                    item.setTargetFlag(finalCrmEnterpriseIdMap.get(item.getId()));
                });
            }
        }
        Page<CrmEnterprisePartVO> voPage = PojoUtils.map(boPage, CrmEnterprisePartVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("通过上级公司名称查询上级公司id和连锁属性信息--有效机构和无效机构都查询，有权限控制")
    @PostMapping("/getCrmEnterpriseParentInfoByName")
    public Result<Page<CrmEnterpriseParentVO>> getCrmEnterpriseParentInfoByName(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmEnterpriseInfoByNamePageListForm form) {
        QueryCrmEnterpriseByNamePageListRequest request = PojoUtils.map(form, QueryCrmEnterpriseByNamePageListRequest.class);
        // 获取权限
        if (Objects.nonNull(form.getPermit()) && form.getPermit().intValue() == 1) {
            SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
            log.info("上级公司公共获取权限:empId={},longs={}", userInfo.getCurrentUserCode(), byEmpId);
            //代表没权限返回错误
            if (OrgDatascopeEnum.NONE == OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())) {
                log.info("上级公司公共获取权限:empId={}没有权限获取数据", userInfo.getCurrentUserCode());
                return Result.success(new Page<>());
            }
            request.setSjmsUserDatascopeBO(byEmpId);
        }
        Page<CrmEnterpriseIdAndNameBO> boPage = crmEnterpriseApi.getCrmEnterpriseIdAndNameByName(request);
        Page<CrmEnterpriseParentVO> voPage = PojoUtils.map(boPage, CrmEnterpriseParentVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }

        List<Long> idList = voPage.getRecords().stream().map(CrmEnterpriseParentVO::getId).collect(Collectors.toList());
        List<CrmSupplierDTO> supplierDTOList = crmSupplierApi.getSupplierInfoByCrmEnterId(idList);
        Map<Long, CrmSupplierDTO> supplierDTOMap = supplierDTOList.stream().collect(Collectors.toMap(CrmSupplierDTO::getCrmEnterpriseId, e -> e, (k1, k2) -> k1));
        for (CrmEnterpriseParentVO parentVO : voPage.getRecords()) {
            CrmSupplierDTO crmSupplierDTO = supplierDTOMap.get(parentVO.getId());
            if (Objects.nonNull(crmSupplierDTO)) {
                parentVO.setChainAttribute(crmSupplierDTO.getChainAttribute());
            }
        }
        return Result.success(voPage);
    }

    private String getRoleString(Integer supplyChainRole) {
        // erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
        String supplyName = "";
        if (1 == supplyChainRole) {
            supplyName = "在商业公司";
        } else if (2 == supplyChainRole) {
            supplyName = "在医疗机构";
        } else if (3 == supplyChainRole) {
            supplyName = "在零售机构";
        }
        return supplyName;
    }

    @ApiOperation("保存基础档案")
    @PostMapping("/save")
    public Result<Long> saveOrUpdate(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCrmAgencyForm form) {
        if (Objects.nonNull(form.getProvinceCode())) {
            String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            form.setProvinceName(namesByCodes[0]);
            form.setCityName(namesByCodes[1]);
            form.setRegionName(namesByCodes[2]);
        }
        //判断ID是否存在
        if (Objects.isNull(form.getId())) {
            SaveCrmEnterpriseRequest request = new SaveCrmEnterpriseRequest();
            PojoUtils.map(form, request);
            request.setOpUserId(userInfo.getCurrentUserId());
            request.setOpTime(new Date());
            Long aLong = crmEnterpriseApi.saveCrmEnterpriseSimple(request);
            return Result.success(aLong);
        }
        //更新操作
        UpdateAgencyEnterpriseRequest updateRequest = new UpdateAgencyEnterpriseRequest();
        PojoUtils.map(form, updateRequest);
        updateRequest.setOpUserId(userInfo.getCurrentUserId());
        updateRequest.setOpTime(new Date());
        crmEnterpriseApi.updateCrmEnterpriseSimple(updateRequest);
        return Result.success(form.getId());
    }

    @ApiOperation("通过机构ID查询机构相关信息")
    @GetMapping("/query")
    public Result<CrmAgencyDetailsVO> getQueryById(@RequestParam(value = "id", required = true) Long id) {
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(id);
        return Result.success(PojoUtils.map(crmEnterpriseDTO, CrmAgencyDetailsVO.class));
    }

}
