package com.yiling.sjms.agency.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.*;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmAgencyPageListForm;
import com.yiling.sjms.agency.form.SaveCrmRelationShipForm;
import com.yiling.sjms.agency.form.SaveCrmSupplierForm;
import com.yiling.sjms.agency.vo.CrmRelationshiplDetailVO;
import com.yiling.sjms.agency.vo.CrmSupplierDetailsVO;
import com.yiling.sjms.agency.vo.CrmSupplierPageListVO;
import com.yiling.sjms.agency.vo.CrmSupplierQueryVO;
import com.yiling.sjms.gb.api.GbOrgMangerApi;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.expr.NewArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import com.yiling.framework.common.base.BaseController;

import javax.validation.Valid;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yiling.sjms.agency.controller.CrmHospitalController.changeField;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@RestController
@RequestMapping("/crm/agency/supplier")
@Api("机构管理商业公司档案")
@Slf4j
public class CrmSupplierController extends BaseController {
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;
    @DubboReference
    private LocationApi locationApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;

    @DubboReference
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    GbOrgMangerApi gbOrgMangerApi;

    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;

    @DubboReference
    CrmHosptialApi crmHosptialApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @ApiOperation("获取列表")
    @PostMapping("/list")
    public Result<Page<CrmSupplierPageListVO>> getAgencySupplierList(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody QueryCrmAgencyPageListForm form) {
       // 获取权限
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());

        log.info("基础档案供应商导出获取权限:empId={},longs={}",userInfo.getCurrentUserCode(),byEmpId);
        //代表没权限返回错误
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return Result.success(new Page<CrmSupplierPageListVO>());
        }
        QueryCrmAgencyPageListRequest request = PojoUtils.map(form, QueryCrmAgencyPageListRequest.class);
        request.setSupplyChainRole(1);
        //非空的情况下放入参数

        request.setSjmsUserDatascopeBO(byEmpId);
        Page<CrmEnterpriseDTO> crmEnterpriseDTOS = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
        Page<CrmSupplierPageListVO> voPage = PojoUtils.map(crmEnterpriseDTOS, CrmSupplierPageListVO.class);
        if (CollectionUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }
        List<Long> crmEnterIds = voPage.getRecords().stream().map(CrmSupplierPageListVO::getId).collect(Collectors.toList());
        List<CrmSupplierDTO> crmSupplierDTOS = crmSupplierApi.getSupplierInfoByCrmEnterId(crmEnterIds);
        //流向打取人工号获取批量
        List<String> flowJobEmpIds = Optional.ofNullable(crmSupplierDTOS.stream().filter(m -> StringUtils.isNotBlank(m.getFlowJobNumber())).map(CrmSupplierDTO::getFlowJobNumber).collect(Collectors.toList())).orElse(Lists.newArrayList());
        //流向打取人人员列表
        List<EsbEmployeeDTO> flowEsbEmployeeDTOS = CollectionUtil.isEmpty(flowJobEmpIds) ? Lists.newArrayList() : esbEmployeeApi.listByEmpIds(flowJobEmpIds);
        //流向打取人人员列表Map
        Map<String, EsbEmployeeDTO> flowEsbEmployeeDTOSMap = flowEsbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, Function.identity()));
        if (CollectionUtil.isNotEmpty(crmEnterpriseDTOS.getRecords())) {
            Map<Long, CrmSupplierDTO> crmSupplierMap = crmSupplierDTOS.stream().collect(Collectors.toMap(CrmSupplierDTO::getCrmEnterpriseId, Function.identity()));
            voPage.getRecords().forEach(item -> {
                //转化扩展信息
                CrmSupplierDTO crmSupplierDTO = crmSupplierMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(crmSupplierDTO)) {
                    Date createTime = item.getCreateTime();
                    PojoUtils.map(crmSupplierDTO, item);
                    item.setId(crmSupplierDTO.getCrmEnterpriseId());
                    item.setCreateTime(createTime);
                    //流向打取人
                    EsbEmployeeDTO esbEmployeeDTO = flowEsbEmployeeDTOSMap.get(crmSupplierDTO.getFlowJobNumber());
                    if (Objects.nonNull(esbEmployeeDTO)) {
                        item.setFlowLiablePerson(esbEmployeeDTO.getEmpName());
                    }
                }
            });
        }
        return Result.success(voPage);
    }

    @ApiOperation("新增")
    @PostMapping("/add")
    public Result<Boolean> addSupplier(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveCrmSupplierForm form) {
        // 扩展信息基本验证
        //省市区名称
        //5连锁属性验证
        if (Objects.equals(form.getHeadChainFlag(), 1) && (Objects.isNull(form.getChainAttribute())
                || Objects.equals(form.getChainAttribute(), 0))) {
            return Result.failed("连锁总部选择是,连锁属性必填");
        }
        if (Objects.equals(form.getHeadChainFlag(), 1) && (Objects.isNull(form.getChainKaFlag())
                || Objects.equals(form.getChainAttribute(), 0))) {
            return Result.failed("连锁总部选择是,连锁是否KA必填");
        }
        //验证三者关系是否重复
        //数据库中的产品组
        List<CrmEnterpriseRelationShipDTO> sourceRelationship = crmSupplierApi.getRelationByCrmEnterpriseId(form.getCrmEnterpriseId(), form.getCrmEnterpriseName());
        boolean repeatFlag = isRepeatEnterpirseRelation(form.getCrmRelationShip(), sourceRelationship, form.getCrmEnterpriseName());
        if (repeatFlag) {
            return Result.failed("三者关系有重复数据请检查");
        }
        SaveCrmSupplierRequest request = new SaveCrmSupplierRequest();
        PojoUtils.map(form, request);
        request.setName(form.getCrmEnterpriseName());
        request.setOpUserId(userInfo.getCurrentUserId());
        int addCount = crmSupplierApi.saveCrmSupplierInfo(request);
        return Result.success();
    }

    @ApiOperation("修改")
    @PostMapping("/edit")
    public Result<Boolean> editSupplierById(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveCrmSupplierForm form) {
        //修改的情况下如果不变化的情况判断
        //编辑时需要不需要判断
        if (Objects.equals(form.getHeadChainFlag(), 1) && (Objects.isNull(form.getChainAttribute())
                || Objects.equals(form.getChainAttribute(), 0))) {
            return Result.failed("连锁总部选择是,连锁属性必填");
        }
        if (Objects.equals(form.getHeadChainFlag(), 1) && (Objects.isNull(form.getChainKaFlag())
                || Objects.equals(form.getChainAttribute(), 0))) {
            return Result.failed("连锁总部选择是,连锁是否KA必填");
        }
        List<CrmEnterpriseRelationShipDTO> sourceRelationship = crmSupplierApi.getRelationByCrmEnterpriseId(form.getCrmEnterpriseId(), form.getCrmEnterpriseName());
        //更新的情况下把当前数据过滤掉
        boolean repeatFlag = isRepeatEnterpirseRelation(form.getCrmRelationShip(), sourceRelationship, form.getCrmEnterpriseName());
        if (repeatFlag) {
            return Result.failed("三者关系有重复数据请检查");
        }
        UpdateCrmSupplierRequest request = new UpdateCrmSupplierRequest();
        PojoUtils.map(form, request);
        request.setName(form.getCrmEnterpriseName());
        request.setOpUserId(userInfo.getCurrentUserId());
        int updateCount = crmSupplierApi.updateCrmSupplierInfo(request);
        return Result.success();
    }

    @ApiOperation("删除")
    @GetMapping("/remove")
    public Result<Boolean> removeSupplierById(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id", required = true) Long id) {
        RemoveCrmSupplierRequest request = new RemoveCrmSupplierRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setCrmEnterpriseId(id);
        crmSupplierApi.removeCrmSupplierById(request);
        return Result.success();
    }

    @ApiOperation("查看")
    @GetMapping("/query")
    public Result<CrmSupplierQueryVO> queryDetailsById(@RequestParam(value = "crmEnterId", required = true) Long crmEnterId) {
        CrmSupplierQueryVO detailsVO = new CrmSupplierQueryVO();
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmEnterId);
        if (Objects.isNull(crmEnterpriseDTO)) {
            return Result.success(detailsVO);
        }
        CrmSupplierDTO crmSupplierDTO = crmSupplierApi.getCrmSupplierByCrmEnterId(crmEnterId);
        //转化基本属性，注意转化时的ID
        if (Objects.nonNull(crmSupplierDTO)) {
            PojoUtils.map(crmSupplierDTO, detailsVO);
            // DIH-零售机构档案，当企业所属上级公司的名称发生改变或上级公司档案被删除，对应的下级企业关联的上级公司信息应随之更新
            String parentCompanyCode = crmSupplierDTO.getParentSupplierCode();
            if (StringUtils.isNotBlank(parentCompanyCode)) {
                CrmEnterpriseDTO enterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(Long.parseLong(parentCompanyCode));
                if (Objects.isNull(enterpriseDTO)) {
                    detailsVO.setParentSupplierCode("");
                    detailsVO.setParentSupplierName("");
                } else {
                    detailsVO.setParentSupplierName(enterpriseDTO.getName());
                }
            }
            //人员及联操作
            //List<EsbEmployeeDTO> esbEmployeeDTOS=
            List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByEmpIds(Arrays.asList(crmSupplierDTO.getFlowJobNumber(), crmSupplierDTO.getCommerceJobNumber()));
            Map<String, EsbEmployeeDTO> esbEmployeeDTOMap = Optional.ofNullable(esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, Function.identity()))).orElse(Maps.newHashMap());
            //  流向打去人参数设置
            EsbEmployeeDTO flowEmp = Optional.ofNullable(esbEmployeeDTOMap.get(crmSupplierDTO.getFlowJobNumber())).orElse(new EsbEmployeeDTO());
            EsbEmployeeDTO commerceJobEmp = Optional.ofNullable(esbEmployeeDTOMap.get(crmSupplierDTO.getCommerceJobNumber())).orElse(new EsbEmployeeDTO());
            detailsVO.setFlowLiablePerson(flowEmp.getEmpName());
            detailsVO.setDepartment(flowEmp.getYxDept());
            detailsVO.setProvincialArea(flowEmp.getYxProvince());
            detailsVO.setCommerceLiablePerson(commerceJobEmp.getEmpName());
        }
        //如果辅助信息没有的情况下
        List<CrmEnterpriseRelationShipDTO> relationship = crmHosptialApi.getRelationByCrmEnterpriseId(crmEnterId, null);
        buildEsbEmployInfo(relationship,false);
        relationship.forEach(item -> {
            item.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole() + "");
        });
        //处理三者关系中的省市区，供应商角色
        processSupplierRelationShip(relationship, crmEnterpriseDTO);
        detailsVO.setRelationshiplDetail(PojoUtils.map(relationship, CrmRelationshiplDetailVO.class));
        return Result.success(detailsVO);
    }

    /**
     * 是否重复三者关系数据
     *
     * @param historyRelationship
     * @return
     */
    private boolean isRepeatEnterpirseRelation(List<SaveCrmRelationShipForm> formRelationShip, List<CrmEnterpriseRelationShipDTO> historyRelationship, String name) {
        //过滤下页面传递过来旧的新增的
        List<SaveCrmRelationShipForm> saveCrmRelationshipRequestList = formRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList());
        List<CrmEnterpriseRelationShipDTO> formSaveRelationShip = PojoUtils.map(saveCrmRelationshipRequestList, CrmEnterpriseRelationShipDTO.class);
        formSaveRelationShip.stream().forEach(item -> item.setCustomerName(name));
        //2个list合并form表单和数据库保存数据
        historyRelationship.addAll(formSaveRelationShip);
        //检查是否重复 唯一key 岗位编码+产品组+经销商为key
        List<String> formRelationShipKey = historyRelationship.stream().map(this::getFormReationMapKey).collect(Collectors.toList());
        long count = formRelationShipKey.stream().distinct().count();
        return count < historyRelationship.size();
    }

    public String getFormReationMapKey(CrmEnterpriseRelationShipDTO form) {
        return toStringNpe(form.getProductGroupId());
    }

    public void processSupplierRelationShip(List<CrmEnterpriseRelationShipDTO> relationship, CrmEnterpriseDTO crmEnterpriseDTO) {
        relationship.stream().forEach(item -> {
            item.setProvinceName(crmEnterpriseDTO.getProvinceName());
            item.setCityName(crmEnterpriseDTO.getCityName());
            item.setRegionName(crmEnterpriseDTO.getRegionName());
            //供应链角色
            item.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole() + "");
        });
    }

    public String toStringNpe(Long var){
        return ObjectUtil.isNull(var) ? "" : var.toString();
    }

    public void buildEsbEmployInfo(List<CrmEnterpriseRelationShipDTO> crmEnterpriseDTOS, boolean needProvinceInfo) {
        // 用岗位id名字postcode找到人员信息，业务部门，省区，业务区域等
        List<Long> postCodes = crmEnterpriseDTOS.stream().filter(item->!(item.getPostCode().equals(0L))).map(CrmEnterpriseRelationShipDTO::getPostCode).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(postCodes)){
            return;
        }
        List<EsbEmployeeDTO> esbEmployeeDTOS = esbEmployeeApi.listByJobIds(postCodes);
        Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap=new HashMap<>();
        Map<Long, EsbOrganizationDTO> esbOrganizationDTOMap=new HashMap<>();
        if(CollectionUtil.isNotEmpty(esbEmployeeDTOS)){
            esbEmployeeDTOMap = esbEmployeeDTOS.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId,o -> o, (k1, k2) -> k1));
            List<Long> deptIds = esbEmployeeDTOS.stream().map(EsbEmployeeDTO::getDeptId).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(deptIds)){
                esbOrganizationDTOMap = businessDepartmentApi.listByOrgIds(deptIds);
            }
        }
        Map<Long, EsbEmployeeDTO> finalEsbEmployeeDTOMap = esbEmployeeDTOMap;
        Map<Long, EsbOrganizationDTO> finalEsbOrganizationDTOMap = esbOrganizationDTOMap;
        // 用岗位id获取esbEmploy信息
        crmEnterpriseDTOS.forEach(item -> {
            Long postCode = item.getPostCode();
            if (ObjectUtil.isNull(postCode) || postCode.equals(0L)) {
                return;
            }
            EsbEmployeeDTO esbEmployeeDTO = finalEsbEmployeeDTOMap.get(postCode);
            if (ObjectUtil.isNull(esbEmployeeDTO)) {
                return;
            }
            changeField(item, esbEmployeeDTO);
            // 获取上级主管岗位id和名称
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"1")){
                List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(),jobNamesList);
                if (CollUtil.isNotEmpty(provincialManagerList)) {
                    EsbEmployeeDTO esbEmployeeDTO1 = provincialManagerList.get(0);
                    item.setSuperiorJob(esbEmployeeDTO1.getJobId());
                    item.setSuperiorJobName(esbEmployeeDTO1.getJobName());
                }else{
                    item.setSuperiorJob(null);
                    item.setSuperiorJobName(null);
                    item.setSuperiorSupervisorName(null);
                    item.setSuperiorSupervisorCode(null);
                }
            }
            if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"2")){
                item.setSuperiorSupervisorCode(esbEmployeeDTO.getEmpId());
                item.setSuperiorSupervisorName(esbEmployeeDTO.getEmpName());
                item.setSuperiorJobName(esbEmployeeDTO.getJobName());
                item.setSuperiorJob(esbEmployeeDTO.getJobId());
                item.setRepresentativeCode(null);
                item.setRepresentativeName(null);
                item.setPostCode(null);
                item.setPostName(null);
            }
            //通过循环获取部门。
            EsbOrganizationDTO organizationDTO= finalEsbOrganizationDTOMap.get(esbEmployeeDTO.getDeptId());
            if (ObjectUtil.isNotEmpty(organizationDTO)) {
                item.setDepartment(organizationDTO.getOrgName());
            }
            //通过部门，业务部门，业务省区获取省区
            String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
            item.setProvincialArea(provinceArea);
            // 省区经理，查不到则为空，查到多个则报错提示 666
            if(needProvinceInfo){
                List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null,jobNamesList);
                if (CollUtil.isNotEmpty(provincialManagerList)) {
                    EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
                    item.setProvincialManagerPostCode(provincialManager.getJobId());
                    item.setProvincialManagerPostName(provincialManager.getJobName());
                    item.setProvincialManagerCode(provincialManager.getEmpId());
                    item.setProvincialManagerName(provincialManager.getEmpName());
                }
            }
        });
    }
}
