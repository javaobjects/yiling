package com.yiling.sjms.agency.controller;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
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
import com.yiling.sjms.agency.api.AgencyUnLockFormApi;
import com.yiling.sjms.agency.dto.AgencyUnLockFormDTO;
import com.yiling.sjms.agency.dto.AgencyUnLockRelationShipFormDTO;
import com.yiling.sjms.agency.dto.request.QueryAgencyFormPageRequest;
import com.yiling.sjms.agency.dto.request.RemoveAgencyFormRequest;
import com.yiling.sjms.agency.dto.request.SaveAgencyUnlockFormRequest;
import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.form.QueryAgencyLockDetailPageForm;
import com.yiling.sjms.agency.form.QueryExtendForm;
import com.yiling.sjms.agency.form.SaveAgencyUnlockForm;
import com.yiling.sjms.agency.form.SubmitAgencyExtendChangeForm;
import com.yiling.sjms.agency.form.UpdateArchiveStatusForm;
import com.yiling.sjms.agency.vo.AgencyUnlockDetailVO;
import com.yiling.sjms.agency.vo.AgencyUnlockFormVO;
import com.yiling.sjms.agency.vo.EsbEmployVO;
import com.yiling.sjms.agency.vo.RelationShipForUnlockVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.workflow.dto.request.SubmitFormBaseRequest;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.sjms.agency.controller.AgencyRelationChangeFormController.changeField;

/**
 * <p>
 * 机构新增修改表单 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@RestController
@Api(tags = "机构解锁")
@Slf4j
@RequestMapping("/agency/unlock/form")
public class AgencyUnlockFormController extends BaseController {

    @DubboReference
    AgencyFormApi agencyFormApi;

    @DubboReference
    AgencyUnLockFormApi agencyUnLockFormApi;

    @DubboReference
    AgencyUnLockFormApi unLockFormApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    FormApi formApi;

    /**
     * 根据机构id查询机构拓展信息及三者信息
     *
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "获取当前用户的岗位编码和业务代表工号")
    @GetMapping("/queryJobId")
    public Result<EsbEmployVO> queryAgencyExtendInfo(@CurrentUser CurrentSjmsUserInfo userInfo) {
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        return Result.success(PojoUtils.map(esbEmployeeDTO, EsbEmployVO.class));
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

        List<RelationShipForUnlockVO> relationShipForUnlockVOS = PojoUtils.map(relationShipList, RelationShipForUnlockVO.class);
        if (CollectionUtils.isNotEmpty(relationShipForUnlockVOS)) {
            List<Long> jobIds = relationShipForUnlockVOS.stream().map(RelationShipForUnlockVO::getPostCode).collect(Collectors.toList());
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIdsForAgency(jobIds);
            log.info("queryRelationShipByCrmIDInfo unlock employeeDTOS" + JSONUtil.toJsonStr(employeeDTOS));
            Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(employeeDTOS)) {
                esbEmployeeDTOMap = employeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, Function.identity()));
            }
            Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
            relationShipForUnlockVOS.forEach(item -> {
                EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(item.getPostCode());
                if (ObjectUtil.isNull(esbEmployeeDTO)) {
                    return;
                }
                changeField(item, esbEmployeeDTO);
            });
        }
        return Result.success(relationShipForUnlockVOS);
    }


    @ApiOperation("新增/编辑")
    @PostMapping("/save")
    public Result<Long> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveAgencyUnlockForm form) {
        if(ObjectUtil.equal(AgencySupplyChainRoleEnum.HOSPITAL.getCode(), form.getSupplyChainRole())){
            return Result.failed("医疗机构三者关系，请通过辖区功能进行维护！");
        }
        if (Objects.nonNull(form.getProvinceCode())) {
            String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
            form.setProvinceName(namesByCodes[0]);
            form.setCityName(namesByCodes[1]);
            form.setRegionName(namesByCodes[2]);
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        SaveAgencyUnlockFormRequest request = PojoUtils.map(form, SaveAgencyUnlockFormRequest.class);
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setOpUserId(userInfo.getCurrentUserId());
        Long formId = agencyUnLockFormApi.save(request);
        return Result.success(formId);
    }

    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<Page<AgencyUnlockFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryAgencyLockDetailPageForm form) {
        QueryAgencyFormPageRequest request = PojoUtils.map(form, QueryAgencyFormPageRequest.class);
        // todo 查询条件
        Page<AgencyUnLockFormDTO> dtoPage = agencyUnLockFormApi.pageList(request);
        Page<AgencyUnlockFormVO> voPage = PojoUtils.map(dtoPage, AgencyUnlockFormVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("查看")
    @GetMapping("/query")
    public Result<AgencyUnlockDetailVO> query(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        AgencyUnLockFormDTO agencyFormDTO = agencyUnLockFormApi.queryById(id);
        AgencyUnlockDetailVO agencyFormVO = PojoUtils.map(agencyFormDTO, AgencyUnlockDetailVO.class);
        if (ObjectUtil.isNull(agencyFormVO)) {
            return Result.success(new AgencyUnlockDetailVO());
        }
        //查询机构三者信息
        List<CrmEnterpriseRelationPostDTO> relationShipList = crmEnterpriseRelationShipApi.getEnterpriseRelationPostByProductGroup(ListUtil.toList(agencyFormDTO.getCrmEnterpriseId()));
        // 在获取已经保存的信息，用来勾选
        List<Long> longListMap = agencyUnLockFormApi.queryRelationListByAgencyFormId(agencyFormDTO.getId());
        log.info("longListMap"+longListMap);
        List<RelationShipForUnlockVO> relationShipForUnlockVOS = PojoUtils.map(relationShipList, RelationShipForUnlockVO.class);
        log.info("query unlock" + JSONUtil.toJsonStr(relationShipForUnlockVOS));
        if (CollectionUtils.isNotEmpty(relationShipForUnlockVOS)) {
            FormDTO formDTO = formApi.getById(agencyFormDTO.getFormId());
            // 审批通过已经通过id关联不到三者关系了，但是需要在页面上显示
            if (ObjectUtil.isNotNull(formDTO) && formDTO.getStatus() == 200) {
                List<AgencyUnLockRelationShipFormDTO> unLockRelationShipFormDTOS = agencyUnLockFormApi.queryListByAgencyFormId(agencyFormDTO.getId());
                if (CollectionUtils.isNotEmpty(unLockRelationShipFormDTOS)) {
                    List<RelationShipForUnlockVO> finalRelationShipForUnlockVOS = relationShipForUnlockVOS;
                    unLockRelationShipFormDTOS.forEach(item->{
                        RelationShipForUnlockVO relationShipForUnlockVO = new RelationShipForUnlockVO();
                        relationShipForUnlockVO.setId(item.getSrcRelationShipIp());
                        relationShipForUnlockVO.setPostCode(item.getPostCode());
                        relationShipForUnlockVO.setProductGroup(item.getProductGroup());
                        finalRelationShipForUnlockVOS.add(relationShipForUnlockVO);
                    });
                    relationShipForUnlockVOS = relationShipForUnlockVOS.stream().sorted(Comparator.comparing(RelationShipForUnlockVO::getId)).collect(Collectors.toList());
                }
            }
            List<Long> jobIds = relationShipForUnlockVOS.stream().map(RelationShipForUnlockVO::getPostCode).collect(Collectors.toList());
            List<EsbEmployeeDTO> employeeDTOS = esbEmployeeApi.listByJobIdsForAgency(jobIds);
            log.info("query unlock employeeDTOS" + JSONUtil.toJsonStr(employeeDTOS));
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
        return Result.success(agencyFormVO);
    }

    @ApiOperation(("删除"))
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveAgencyFormRequest request = new RemoveAgencyFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = agencyUnLockFormApi.deleteById(request);
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
        agencyUnLockFormApi.submit(request);
        return Result.success(true);
    }

    @ApiOperation(value = "修改归档状态")
    @PostMapping(value = "/updateArchiveStatus")
    public Result<Boolean> updateArchiveStatus(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateArchiveStatusForm form) {
        UpdateAgencyLockArchiveRequest request = new UpdateAgencyLockArchiveRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(form.getId());
        request.setArchiveStatus(form.getArchiveStatus());
        agencyUnLockFormApi.updateArchiveStatusById(request);
        return Result.success(true);
    }
}
