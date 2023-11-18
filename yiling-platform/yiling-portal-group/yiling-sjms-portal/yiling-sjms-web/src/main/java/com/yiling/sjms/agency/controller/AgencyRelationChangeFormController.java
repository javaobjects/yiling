package com.yiling.sjms.agency.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.api.AgencyFormApi;
import com.yiling.sjms.agency.api.AgencyRelationShipChangeFormApi;
import com.yiling.sjms.agency.dto.AgencyRelationShipChangeFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyRelationChangeFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.form.QueryAgencyFormPageForm;
import com.yiling.sjms.agency.form.QueryExtendForm;
import com.yiling.sjms.agency.form.SaveAgencyRelationShipChangeFormForm;
import com.yiling.sjms.agency.form.SubmitAgencyExtendChangeForm;
import com.yiling.sjms.agency.form.UpdateArchiveStatusForm;
import com.yiling.sjms.agency.vo.AgencyChangeRelationShipDetailVO;
import com.yiling.sjms.agency.vo.AgencyRelationShipChangeFormVO;
import com.yiling.sjms.agency.vo.RelationShipForUnlockVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 机构新增修改表单 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@RestController
@Api(tags = "三者关系变更")
@Slf4j
@RequestMapping("/agency/relationShipChange")
public class AgencyRelationChangeFormController extends BaseController {

    @DubboReference
    AgencyFormApi agencyFormApi;

    @DubboReference
    AgencyRelationShipChangeFormApi relationShipChangeFormApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    CrmPharmacyApi crmPharmacyApi;

    @DubboReference
    FormApi formApi;

    @ApiOperation("新增/编辑")
    @PostMapping("/save")
    public Result<Long> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveAgencyRelationShipChangeFormForm form) {
        if(ObjectUtil.equal(AgencySupplyChainRoleEnum.HOSPITAL.getCode(), form.getSupplyChainRole())){
            return Result.failed("医疗机构三者关系，请通过辖区功能进行维护！");
        }
        if (Objects.nonNull(form.getProvinceCode())) {
            String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            form.setProvinceName(namesByCodes[0]);
            form.setCityName(namesByCodes[1]);
            form.setRegionName(namesByCodes[2]);
        }
        if(CollectionUtils.isNotEmpty(form.getRelationShip())){
            form.getRelationShip().forEach(item->{
                if(StringUtils.equals(item.getDutyGredeId(),"2")){
                    item.setPostCode(item.getSuperiorJob());
                }
            });
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        SaveAgencyRelationChangeFormRequest request = PojoUtils.map(form, SaveAgencyRelationChangeFormRequest.class);
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setOpUserId(userInfo.getCurrentUserId());


        Long formId = relationShipChangeFormApi.save(request);
        return Result.success(formId);
    }

    @ApiOperation("获取formid下所有的机构名称")
    @PostMapping("/getNameByFormId")
    public Result<List<String>> getNameByFormId(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "formId") Long id) {
        List<String> names = relationShipChangeFormApi.getNameByFormId(id);
        return Result.success(names);
    }

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<Page<AgencyRelationShipChangeFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryAgencyFormPageForm form) {
        QueryAgencyFormPageRequest request = PojoUtils.map(form, QueryAgencyFormPageRequest.class);
        Page<AgencyRelationShipChangeFormDTO> dtoPage = relationShipChangeFormApi.pageList(request);
        Page<AgencyRelationShipChangeFormVO> voPage = PojoUtils.map(dtoPage, AgencyRelationShipChangeFormVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("查看")
    @GetMapping("/query")
    public Result<AgencyChangeRelationShipDetailVO> query(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        AgencyRelationShipChangeFormDTO agencyFormDTO = relationShipChangeFormApi.queryById(id);

        AgencyChangeRelationShipDetailVO agencyFormVO = PojoUtils.map(agencyFormDTO, AgencyChangeRelationShipDetailVO.class);
        if (ObjectUtil.isNull(agencyFormVO)) {
            return Result.success(new AgencyChangeRelationShipDetailVO());
        }
        // 获取是否目标值字段
        Long crmEnterpriseId = agencyFormDTO.getCrmEnterpriseId();
        CrmEnterpriseDTO crmEnterpriseById = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if (ObjectUtil.isNotNull(crmEnterpriseById) && crmEnterpriseById.getSupplyChainRole() == 3) {
            List<CrmPharmacyDTO> crmPharmacyDTOS = crmPharmacyApi.listByCrmEnterpriseId(Arrays.asList(crmEnterpriseId));
            if (CollectionUtil.isNotEmpty(crmPharmacyDTOS)) {
                agencyFormVO.setTargetFlag(crmPharmacyDTOS.get(0).getTargetFlag());
            }
        }
        //查询机构三者信息
        List<CrmEnterpriseRelationPostDTO> relationShipList = crmEnterpriseRelationShipApi.getEnterpriseRelationPostByProductGroup(ListUtil.toList(agencyFormDTO.getCrmEnterpriseId()));
        // 在获取已经保存的信息，用来勾选
        List<Long> longListMap = relationShipChangeFormApi.queryRelationListByAgencyFormId(agencyFormDTO.getId());
        List<RelationShipForUnlockVO> relationShipForUnlockVOS = PojoUtils.map(relationShipList, RelationShipForUnlockVO.class);
        log.info("query relationShipForUnlockVOS"+JSONUtil.toJsonStr(relationShipForUnlockVOS));
        //FormDTO formDTO = formApi.getById(agencyFormDTO.getFormId());
        if (CollectionUtils.isNotEmpty(relationShipForUnlockVOS)) {
            List<Long> jobIds = relationShipForUnlockVOS.stream().map(RelationShipForUnlockVO::getPostCode).collect(Collectors.toList());
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIdsForAgency(jobIds);
            log.info("query employeeDTOS"+JSONUtil.toJsonStr(employeeDTOS));
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(employeeDTOS)) {
                esbEmployeeDTOMap = employeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, Function.identity()));
            }
            Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
            relationShipForUnlockVOS.forEach(item -> {
                if (CollectionUtils.isNotEmpty(longListMap) && longListMap.contains(item.getId())) {
                    item.setSelect(true);
                }
                EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(item.getPostCode());
                if (ObjectUtil.isNull(esbEmployeeDTO)) {
                    return;
                }
                changeField(item, esbEmployeeDTO);
            });
        }
        // 只返回勾选的
        /*if(ObjectUtil.isNotNull(formDTO)&&formDTO.getStatus()!=10){
            relationShipForUnlockVOS=relationShipForUnlockVOS.stream().filter(item->item.getSelect()).collect(Collectors.toList());
        }*/
        agencyFormVO.setRelationShip(relationShipForUnlockVOS);
        agencyFormVO.getPostCode();
        return Result.success(agencyFormVO);
    }

    @ApiOperation("获取当前用户")
    @GetMapping("/getCurrentUserEmptInfo")
    public Result<EsbEmployeeDTO> getCurrentUserEmptInfo(@CurrentUser CurrentSjmsUserInfo userInfo) {
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        return Result.success(esbEmployeeDTO);
    }

    /**
     * 根据机构id查询机构拓展信息及三者信息
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "根据机构id查询三者信息")
    @GetMapping("/queryRelationShipByCrmIDInfo")
    public Result<List<RelationShipForUnlockVO>> queryAgencyExtendInfo(@Valid QueryExtendForm form) {
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(form.getCrmEnterpriseId());
        if (ObjectUtil.isNull(crmEnterpriseDTO)) {
            return Result.success(new ArrayList<>());
        }
        //查询机构三者信息
        //List<CrmEnterpriseRelationShipDTO> relationShipList = crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByNameList(ListUtil.toList(crmEnterpriseDTO.getName()));
        List<CrmEnterpriseRelationShipDTO> relationShipList=crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByCrmenterpriseIdList(ListUtil.toList(crmEnterpriseDTO.getId()));
        // 在获取已经保存的信息，用来勾选
        List<Long> longListMap = relationShipChangeFormApi.queryRelationListByAgencyFormId(crmEnterpriseDTO.getId());
        List<RelationShipForUnlockVO> relationShipForUnlockVOS = PojoUtils.map(relationShipList, RelationShipForUnlockVO.class);
        if (CollectionUtils.isNotEmpty(relationShipForUnlockVOS)) {
            List<Long> jobIds = relationShipForUnlockVOS.stream().map(RelationShipForUnlockVO::getPostCode).collect(Collectors.toList());
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIdsForAgency(jobIds);
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = new HashMap<>();

            if (CollectionUtils.isNotEmpty(employeeDTOS)) {
                esbEmployeeDTOMap = employeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, Function.identity()));
            }
            Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
            relationShipForUnlockVOS.forEach(item -> {
                if (CollectionUtils.isNotEmpty(longListMap) && longListMap.contains(item.getId())) {
                    item.setSelect(true);
                }
                EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(item.getPostCode());
                if (ObjectUtil.isNull(esbEmployeeDTO)) {
                    return;
                }
                changeField(item, esbEmployeeDTO);
            });
        }
        return Result.success(relationShipForUnlockVOS);
    }

    @ApiOperation(("删除"))
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        log.info("remove" + id);
        RemoveAgencyFormRequest request = new RemoveAgencyFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = relationShipChangeFormApi.deleteById(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation(value = "提交审核")
    @PostMapping("submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SubmitAgencyExtendChangeForm form) {
        SubmitFormBaseRequest request = new SubmitFormBaseRequest();
        request.setId(form.getFormId()).setEmpId(userInfo.getCurrentUserCode()).setOpUserId(userInfo.getCurrentUserId());
        relationShipChangeFormApi.submit(request);
        return Result.success(true);
    }

    @ApiOperation(value = "修改归档状态")
    @PostMapping(value = "/updateArchiveStatus")
    public Result<Boolean> updateArchiveStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateArchiveStatusForm form) {
        UpdateAgencyLockArchiveRequest request = new UpdateAgencyLockArchiveRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(form.getId());
        request.setArchiveStatus(form.getArchiveStatus());
        relationShipChangeFormApi.updateArchiveStatusById(request);
        return Result.success(true);
    }

    public static void changeField(RelationShipForUnlockVO item, EsbEmployeeDTO esbEmployeeDTO) {
        if(ObjectUtil.isNull(esbEmployeeDTO)){
            return;
        }
        item.setBusinessDepartment(esbEmployeeDTO.getYxDept());
        item.setBusinessProvince(esbEmployeeDTO.getYxProvince());
        item.setBusinessArea(esbEmployeeDTO.getYxArea());
        item.setRepresentativeCode(esbEmployeeDTO.getEmpId());
        item.setRepresentativeName(esbEmployeeDTO.getEmpName());
        item.setCustomerCode(esbEmployeeDTO.getEmpId());
        item.setPostCode(esbEmployeeDTO.getJobId());
        item.setPostName(esbEmployeeDTO.getJobName());
    }
}
