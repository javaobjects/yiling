package com.yiling.sjms.agency.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmHospitalDTO;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.agency.enums.CrmPharmacyAttributeEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.flow.api.FlowPurchaseChannelApi;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyLockFormApi;
import com.yiling.sjms.agency.dto.AgencyLockFormDTO;
import com.yiling.sjms.agency.dto.request.DeleteAgencyLockFormRequest;
import com.yiling.sjms.agency.dto.request.QueryAgencyLockFormPageRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyLockFormRequest;
import com.yiling.sjms.agency.enums.AgencyLockErrorCode;
import com.yiling.sjms.agency.form.QueryAgencyExtendChangePageForm;
import com.yiling.sjms.agency.form.QueryExtendForm;
import com.yiling.sjms.agency.form.SaveAgencyExtendChangeForm;
import com.yiling.sjms.agency.form.SubmitAgencyExtendChangeForm;
import com.yiling.sjms.agency.vo.AgencyExtendChangeDetailVO;
import com.yiling.sjms.agency.vo.AgencyExtendChangeVO;
import com.yiling.sjms.agency.vo.AgencyExtendVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 机构扩展信息修改表单
 * @author: gxl
 * @date: 2023/2/22
 */
@Slf4j
@RestController
@RequestMapping("/agency/extend")
@Api(tags = "机构拓展信息修改")
public class AgencyExtendChangeController extends BaseController {

    @DubboReference
    private AgencyLockFormApi agencyLockFormApi;

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private CrmSupplierApi crmSupplierApi;

    @DubboReference
    private CrmPharmacyApi crmPharmacyApi;

    @DubboReference
    private CrmHosptialApi crmHosptialApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private FormApi formApi;

    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    FlowPurchaseChannelApi flowPurchaseChannelApi;

    @ApiOperation(value = "新增页面选中机构后-查询拓展信息")
    @GetMapping("getAgencyExtend")
    public Result<AgencyExtendVO> getAgencyExtend(@Valid QueryExtendForm form){
        AgencyExtendVO agencyExtendVO = new AgencyExtendVO();
        CrmEnterpriseDTO crmEnterpriseDTO=  crmEnterpriseApi.getCrmEnterpriseById(form.getCrmEnterpriseId());
        if(ObjectUtil.isEmpty(crmEnterpriseDTO)){
            return Result.success(agencyExtendVO);
        }
        if(form.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode())){
            CrmSupplierDTO crmSupplierDTO=crmSupplierApi.getCrmSupplierByCrmEnterId(form.getCrmEnterpriseId());
            if(ObjectUtil.isEmpty(crmSupplierDTO)){
                return Result.success(agencyExtendVO);
            }
            PojoUtils.map(crmSupplierDTO,agencyExtendVO);
        }
        if(form.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.HOSPITAL.getCode())){
            CrmHospitalDTO crmHospitalDTO=crmHosptialApi.getCrmSupplierByCrmEnterId(form.getCrmEnterpriseId().toString());
            if(ObjectUtil.isEmpty(crmHospitalDTO)){
                return Result.success(agencyExtendVO);
            }
            PojoUtils.map(crmHospitalDTO,agencyExtendVO);
        }
        if(form.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.PHARMACY.getCode())){
            CrmPharmacyDTO crmPharmacyDTO = crmPharmacyApi.queryByEnterpriseId(form.getCrmEnterpriseId());
            if(ObjectUtil.isEmpty(crmPharmacyDTO)){
                return Result.success(agencyExtendVO);
            }
            PojoUtils.map(crmPharmacyDTO,agencyExtendVO);
            agencyExtendVO.setParentSupplierCode(crmPharmacyDTO.getParentCompanyCode());
            agencyExtendVO.setParentSupplierName(crmPharmacyDTO.getParentCompanyName());
            if(CrmPharmacyAttributeEnum.CHAIN.getCode().equals(crmPharmacyDTO.getPharmacyAttribute()) && StrUtil.isNotEmpty(crmPharmacyDTO.getParentCompanyName())){
                if(StrUtil.isEmpty(agencyExtendVO.getParentSupplierCode()) || null ==agencyExtendVO.getChainAttribute()|| 0==agencyExtendVO.getChainAttribute()){
                    QueryCrmEnterpriseByNamePageListRequest request = new  QueryCrmEnterpriseByNamePageListRequest();
                    request.setName(crmPharmacyDTO.getParentCompanyName());
                    Page<CrmEnterpriseIdAndNameBO> boPage = crmEnterpriseApi.getCrmEnterpriseIdAndNameByName(request);
                    CrmEnterpriseIdAndNameBO crmEnterpriseIdAndNameBO = boPage.getRecords().get(0);
                    agencyExtendVO.setParentSupplierCode(crmEnterpriseIdAndNameBO.getId().toString());
                    List<CrmSupplierDTO> supplierDTOList = crmSupplierApi.getSupplierInfoByCrmEnterId(Collections.singletonList(crmEnterpriseIdAndNameBO.getId()));
                    if(CollUtil.isNotEmpty(supplierDTOList)){
                        agencyExtendVO.setChainAttribute(supplierDTOList.get(0).getChainAttribute());
                    }
                }
            }

        }
        return Result.success(agencyExtendVO);
    }

    @ApiOperation(value = "新增或编辑")
    @PostMapping("save")
    public Result<Long> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveAgencyExtendChangeForm form){
        SaveAgencyLockFormRequest request = new SaveAgencyLockFormRequest();
        //流向打取人清空校验
        if( form.getChangeItem().contains("flowLiablePerson") && "".equals(form.getFlowLiablePerson())){
            Boolean existByCrmEnterpriseId = crmEnterpriseRelationShipApi.getExistByCrmEnterpriseId(form.getCrmEnterpriseId());
            if(existByCrmEnterpriseId){
                throw new BusinessException(AgencyLockErrorCode.EXIST_ENTERPRISE_REL);
            }
            List<FlowPurchaseChannelDTO> byPurchaseOrgId = flowPurchaseChannelApi.findByPurchaseOrgId(form.getCrmEnterpriseId());
            if(CollUtil.isNotEmpty(byPurchaseOrgId)){
                throw new BusinessException(AgencyLockErrorCode.EXIST_ENTERPRISE_PURCHASE_CHANNEL);
            }
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        PojoUtils.map(form,request);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setBusinessType(2);
        request.setEmpName(esbEmployeeDTO.getEmpName());
        Long formId = agencyLockFormApi.save(request);
        return Result.success(formId);
    }

    @ApiOperation(value = "分页列表")
    @GetMapping(value = "queryPage")
    public Result<Page<AgencyExtendChangeVO>> queryPage(@CurrentUser CurrentSjmsUserInfo userInfo,@Valid
            QueryAgencyExtendChangePageForm queryAgencyLockFormPageForm){
        QueryAgencyLockFormPageRequest request = new QueryAgencyLockFormPageRequest();
        PojoUtils.map(queryAgencyLockFormPageForm,request);
        //request.setOpUserId(userInfo.getCurrentUserId());
        Page<AgencyLockFormDTO> agencyLockFormDTOPage = agencyLockFormApi.queryPage(request);
        Page<AgencyExtendChangeVO> agencyLockFormVOPage = PojoUtils.map(agencyLockFormDTOPage, AgencyExtendChangeVO.class);
        return Result.success(agencyLockFormVOPage);
    }

    @ApiOperation(value = "修改页面反显")
    @GetMapping(value = "getAgencyExtendChangeDetail")
    public Result<AgencyExtendChangeDetailVO> getAgencyExtendChangeDetail(Long id){
        AgencyLockFormDTO agencyLockForm = agencyLockFormApi.getAgencyLockForm(id);
        AgencyExtendChangeDetailVO  agencyExtendChangeDetailVO = new AgencyExtendChangeDetailVO();
        PojoUtils.map(agencyLockForm,agencyExtendChangeDetailVO);
        return Result.success(agencyExtendChangeDetailVO);
    }

    @ApiOperation(value = "删除单个单据")
    @GetMapping(value = "deleteOne")
    public Result<Boolean> delete(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam Long id){
        DeleteAgencyLockFormRequest request = new DeleteAgencyLockFormRequest();
        request.setId(id).setOpUserId(userInfo.getCurrentUserId());
        agencyLockFormApi.deleteById(request);
        return Result.success(true);
    }

    @ApiOperation(value = "提交审核")
    @PostMapping("submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody @Valid SubmitAgencyExtendChangeForm form){
        SubmitFormBaseRequest request = new SubmitFormBaseRequest();
        request.setId(form.getFormId()).setEmpId(userInfo.getCurrentUserCode()).setOpUserId(userInfo.getCurrentUserId());
        agencyLockFormApi.submit(request);
        return Result.success(true);
    }
}