package com.yiling.sjms.agency.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.util.ReUtil;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorParamRequest;
import org.apache.commons.lang3.StringUtils;
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
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryCrmEnterpriseRelationPageListForm;
import com.yiling.sjms.agency.form.SaveCrmEnterpriseRelationShipForm;
import com.yiling.sjms.agency.vo.CrmEnterpriseRelationShipListVO;
import com.yiling.sjms.agency.vo.EsbEmployVO;
import com.yiling.sjms.gb.api.GbOrgMangerApi;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.sjms.agency.controller.CrmHospitalController.changeField;

/**
 * <p>
 * 医疗机构拓展表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@Slf4j
@RestController
@RequestMapping("/crm/agency/crmEnterRelation")
@Api(tags = "三者关系")
public class CrmEnterpriseRelationShiplController extends BaseController {
    @DubboReference
    CrmHosptialApi crmHosptialApi;

    @DubboReference
    CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference(timeout = 10 * 1000)
    CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    EsbOrganizationApi esbOrganizationApi;

    @DubboReference
    SjmsUserApi sjmsUserApi;

    @DubboReference
    UserApi userApi;
    @DubboReference
    private CrmSupplierApi crmSupplierApi;

    @DubboReference
    GbOrgMangerApi gbOrgMangerApi;

    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;

    @DubboReference
    CrmGoodsGroupApi crmGoodsGroupApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    @DubboReference
    CrmPharmacyApi crmPharmacyApi;

    @DubboReference
    private CrmManorApi crmManorApi;
    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @ApiOperation(value = "列表查询")
    @PostMapping("/pageList")
    public Result<Page<CrmEnterpriseRelationShipListVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody QueryCrmEnterpriseRelationPageListForm form) {
        SjmsUserDatascopeBO byEmpId = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("基础档案供应商导出获取权限:empId={},longs={}",userInfo.getCurrentUserCode(),byEmpId);
        //代表没权限返回错误
        if(OrgDatascopeEnum.NONE==OrgDatascopeEnum.getFromCode(byEmpId.getOrgDatascope())){
            return Result.success(new Page<>());
        }
        QueryCrmEnterpriseRelationShipPageListRequest shipPageListRequest = PojoUtils.map(form, QueryCrmEnterpriseRelationShipPageListRequest.class);
        /*if (StringUtils.isNotEmpty(shipPageListRequest.getCustomerName())) {
            QueryCrmAgencyPageListRequest request = new QueryCrmAgencyPageListRequest();
            request.setSize(50);
            request.setCurrent(1);
            request.setName(shipPageListRequest.getCustomerName());
            Page<CrmEnterpriseDTO> employeeDTO = crmEnterpriseApi.getCrmEnterpriseInfoPage(request);
            if (CollectionUtil.isNotEmpty(employeeDTO.getRecords())) {
                List<Long> enterpriseIds = employeeDTO.getRecords().stream().map(CrmEnterpriseDTO::getId).collect(Collectors.toList());
                shipPageListRequest.setCrmEnterpriseIds(enterpriseIds);
                shipPageListRequest.setCustomerName(null);
            }else {
                return Result.success(new Page<CrmEnterpriseRelationShipListVO>());
            }
        }*/
        shipPageListRequest.setSjmsUserDatascopeBO(byEmpId);
        log.info("crmEnterRelationpageList param" + shipPageListRequest);
        Page<CrmEnterpriseRelationShipDTO> crmEnterpriseDTOS = crmEnterpriseRelationShipApi.getCrmRelationPage(shipPageListRequest);
        if (CollectionUtil.isEmpty(crmEnterpriseDTOS.getRecords())) {
            return Result.success(new Page<CrmEnterpriseRelationShipListVO>());
        }

        buildEsbEmployInfo(crmEnterpriseDTOS.getRecords(),false);
       // 更新人id找到更新人名字
        List<Long> userIds = crmEnterpriseDTOS.getRecords().stream().map(CrmEnterpriseRelationShipDTO::getUpdateUser).collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        List<Long> crmEnterPriseIds = crmEnterpriseDTOS.getRecords().stream().map(CrmEnterpriseRelationShipDTO::getCrmEnterpriseId).collect(Collectors.toList());
        List<CrmEnterpriseDTO> crmEnterpriseDTOList = crmEnterpriseApi.getCrmEnterpriseListById(crmEnterPriseIds);
        //处理辖区和分类名称
        buildManorAndCategoryName(crmEnterpriseDTOS.getRecords());
        if (CollectionUtil.isNotEmpty(userDTOS)) {
            Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            crmEnterpriseDTOS.getRecords().forEach(item -> {
                UserDTO crmHospitalDTO = userDTOMap.get(item.getUpdateUser());
                if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                    item.setUpdateUserName(crmHospitalDTO.getName());
                }
            });
        }
        // 根据机构id找到机构信息
        if (CollectionUtil.isNotEmpty(crmEnterpriseDTOList)) {
            Map<Long, CrmEnterpriseDTO> crmEnterpriseDTOMap = crmEnterpriseDTOList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getId, Function.identity()));
            crmEnterpriseDTOS.getRecords().forEach(item -> {
                CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseDTOMap.get(item.getCrmEnterpriseId());
                if (ObjectUtil.isNotEmpty(crmEnterpriseDTO)) {
                    item.setProvinceName(crmEnterpriseDTO.getProvinceName());
                    item.setCityName(crmEnterpriseDTO.getCityName());
                    item.setRegionName(crmEnterpriseDTO.getRegionName());
                    //供应链角色
                    item.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole() + "");
                    item.setCustomerName(crmEnterpriseDTO.getName());
                }else {
                    // 机构已经被删除了，机构相关的字段赋值为空
                    item.setProvinceName(null);
                    item.setCityName(null);
                    item.setRegionName(null);
                    //供应链角色
                    item.setSupplyChainRole(null);
                    item.setCustomerName(null);
                }
            });
        }

        return Result.success(PojoUtils.map(crmEnterpriseDTOS, CrmEnterpriseRelationShipListVO.class));
    }

    private void buildManorAndCategoryName(List<CrmEnterpriseRelationShipDTO> records) {
        //品种List
        List<Long> categoryIds = records.stream().filter(e->e.getCategoryId().longValue()>0L).map(CrmEnterpriseRelationShipDTO::getCategoryId).collect(Collectors.toList());
        List<Long> manorIds = records.stream().filter(e->e.getCrmEnterpriseId().longValue()>0L).map(CrmEnterpriseRelationShipDTO::getManorId).collect(Collectors.toList());
        //分类
        if(CollUtil.isNotEmpty(categoryIds)){
            List<CrmGoodsCategoryDTO> categoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
            if(CollUtil.isNotEmpty(categoryDTOS)){
                Map<Long,CrmGoodsCategoryDTO> crmGoodsCategoryDTOMap=categoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId,m->m,(v1,v2)->v1));
                records.stream().filter(e->e.getCategoryId().longValue()>0L).forEach(e->{
                    if(crmGoodsCategoryDTOMap.get(e.getCategoryId())!=null){
                        e.setCategoryName(crmGoodsCategoryDTOMap.get(e.getCategoryId()).getName());
                    }
                });
            }
        }
        //辖区
        if(CollUtil.isNotEmpty(manorIds)){
            QueryCrmManorParamRequest request= new QueryCrmManorParamRequest();
            request.setIdList(manorIds);
            List<CrmManorDTO> manorDTOS=crmManorApi.listByParam(request);
            if(CollUtil.isNotEmpty(manorDTOS)){
                Map<Long,CrmManorDTO> manorDTOMap=manorDTOS.stream().collect(Collectors.toMap(CrmManorDTO::getId,m->m,(v1,v2)->v1));
                records.stream().filter(e->e.getManorId().longValue()>0L).forEach(e->{
                    if(manorDTOMap.get(e.getManorId())!=null){
                        e.setManorName(manorDTOMap.get(e.getManorId()).getName());
                    }
                });
            }

        }
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
                item.setDutyGredeId("1");
                List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(), jobNamesList);
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
                item.setDutyGredeId("2");
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
                List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(),null, jobNamesList);
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

    @ApiOperation(value = "业务代表工号或者岗位代码获取信息")
    @GetMapping("/getByEmpIdOrJobId")
    public Result<EsbEmployVO> getByEmpIdOrJobId(@RequestParam(value = "empId", required = false) String empId, @RequestParam(value = "jobId", required = false) String jobId,@RequestParam(value = "isHospital", required = false) boolean isHospital) {
        if(StringUtils.isBlank(empId)&&StringUtils.isBlank(jobId)){
            return Result.failed(100030, "参数不能为空");
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(empId, jobId,null);
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(empId)) {
            return Result.failed(100030, "您输入的工号不存在");
        }
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(jobId)) {
            return Result.failed(100030, "您输入的岗位代码不存在");
        }
        if (ObjectUtil.isNotEmpty(esbEmployeeDTO) && CompareUtil.compare(esbEmployeeDTO.getJobId(), 0L) == 0) {
            return Result.failed(100030, "您输入的"+(StringUtils.isEmpty(empId)?"岗位代码":"工号")+"不存在");
        }
        //通过循环获取部门。
        EsbEmployVO result = PojoUtils.map(esbEmployeeDTO, EsbEmployVO.class);
        EsbOrganizationDTO organizationDTO = businessDepartmentApi.getByOrgId(esbEmployeeDTO.getDeptId());
        if (ObjectUtil.isNotEmpty(organizationDTO)) {
            result.setDepartment(organizationDTO.getOrgName());
        }
        //通过部门，业务部门，业务省区获取省区
        String provinceArea = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince());
        result.setProvinceArea(provinceArea);

        if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"1")){
            List<String> jobNamesList = ListUtil.toList("地区经理", "占位");
            List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), esbEmployeeDTO.getYxArea(), jobNamesList);
            if (CollUtil.isNotEmpty(provincialManagerList)) {
                if (provincialManagerList.size() > 1) {
                    throw new BusinessException(ResultCode.FAILED, "查询到多个地区经理，请检查数据！");
                }
                EsbEmployeeDTO esbEmployeeDTO1 = provincialManagerList.get(0);
                result.setSuperiorJob(esbEmployeeDTO1.getJobId());
                result.setSuperiorJobName(esbEmployeeDTO1.getJobName());
            }else{
                result.setSuperiorJob(null);
                result.setSuperiorJobName(null);
                result.setSuperiorName(null);
                result.setSuperior(null);
            }
        }
        if(StringUtils.equals(esbEmployeeDTO.getDutyGredeId(),"2")){
            result.setSuperior(esbEmployeeDTO.getEmpId());
            result.setSuperiorName(esbEmployeeDTO.getEmpName());
            result.setSuperiorJobName(esbEmployeeDTO.getJobName());
            result.setSuperiorJob(esbEmployeeDTO.getJobId());
            result.setEmpId(null);
            result.setEmpName(null);
            result.setJobId(null);
            result.setJobName(null);
        }
        // 省区经理，查不到则为空，查到多个则报错提示
        List<String> jobNamesList = ListUtil.toList("省区经理", "省区主管");
        List<EsbEmployeeDTO> provincialManagerList = esbEmployeeApi.getProvincialManagerByYxDeptAndYxProvinceAndJobNames(esbEmployeeDTO.getYxDept(), esbEmployeeDTO.getYxProvince(), null,jobNamesList);
        if (CollUtil.isNotEmpty(provincialManagerList)) {
            if (provincialManagerList.size() > 1) {
                throw new BusinessException(ResultCode.FAILED, "查询到多个省区经理，请检查数据！");
            }
            EsbEmployeeDTO provincialManager = provincialManagerList.get(0);
            result.setProvincialManagerPostCode(provincialManager.getJobId());
            result.setProvincialManagerPostName(provincialManager.getJobName());
            result.setProvincialManagerCode(provincialManager.getEmpId());
            result.setProvincialManagerName(provincialManager.getEmpName());
        }
        return Result.success(result);
    }

    @ApiOperation(value = "根据业务代表姓名获取业务部门和姓名")
    @GetMapping("/getYxDeptAndName")
    public Result<List<EsbEmployVO>> getYxDeptAndName(@RequestParam(value = "name", required = false) String name) {
        List<EsbEmployeeDTO> empInfoByName = esbEmployeeApi.getEmpInfoByName(name);
        return Result.success(PojoUtils.map(empInfoByName, EsbEmployVO.class));
    }


    @ApiOperation(value = "根据三者关系获取产品组")
    @GetMapping("/getGoodsGroup")
    public Result<List<CrmDepartmentAreaRelationDTO>> getGoodsGroup(@RequestParam(value = "ywbm") String ywbm, @RequestParam(value = "supplyChainRole") Integer supplyChainRole) {
        List<CrmDepartmentAreaRelationDTO> esbEmployeeDTO = crmEnterpriseRelationShipApi.getGoodsGroup(ywbm, supplyChainRole);
        if (ObjectUtil.isEmpty(esbEmployeeDTO)) {
            return Result.failed("产品组信息不存在");
        }
        // 根据产品组名称去重
        List<CrmDepartmentAreaRelationDTO> result = esbEmployeeDTO.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CrmDepartmentAreaRelationDTO::getProductGroup))), ArrayList::new));
        return Result.success(result);
    }

    @ApiOperation(value = "增加或者修改三者关系")
    @PostMapping("/add")
    public Result<Boolean> add(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCrmEnterpriseRelationShipForm form) {
        SaveCrmEnterpriseRelationShipRequest request = PojoUtils.map(form, SaveCrmEnterpriseRelationShipRequest.class);
        if(StringUtils.equals(form.getDutyGredeId(),"2")){
            request.setPostCode(form.getSuperiorJob());
        }
        request.setSupplyChainRoleType(Integer.parseInt(request.getSupplyChainRole()));
        request.setSupplyChainRole(CrmSupplyChainRoleEnum.getFromCode(Integer.parseInt(request.getSupplyChainRole())).getName());
        request.setOpUserId(userInfo.getCurrentUserId());
        List<CrmEnterpriseRelationShipDTO> sourceRelationship = crmSupplierApi.getRelationByCrmEnterpriseId(form.getCrmEnterpriseId(), null);
        CrmEnterpriseRelationShipDTO formReationShip = PojoUtils.map(form, CrmEnterpriseRelationShipDTO.class);
        //更新的情况下把当前数据过滤掉
        if (Objects.nonNull(form.getId())) {
            List<CrmEnterpriseRelationShipDTO> tempRelationShip = sourceRelationship.stream().filter(item -> !Objects.equals(item.getId(), form.getId())).collect(Collectors.toList());
            boolean repeatFlag1 = isRepeatEnterpirseRelation(formReationShip, tempRelationShip);
            if (repeatFlag1) {
                return Result.failed("三者关系有重复数据请检查");
            }
        } else {
            boolean repeatFlag2 = isRepeatEnterpirseRelation(formReationShip, sourceRelationship);
            if (repeatFlag2) {
                return Result.failed("三者关系有重复数据请检查");
            }
        }
        Boolean result = crmEnterpriseRelationShipApi.saveOrUpdate(request);
        return Result.success(result);
    }


    @ApiOperation("删除三者关系")
    @GetMapping("/delete")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveCrmEnterpriseRelationShipRequest request = new RemoveCrmEnterpriseRelationShipRequest();
        request.setId(id);
        request.setOpUserId(userInfo.getCurrentUserId());
        if (crmEnterpriseRelationShipApi.remove(request)) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation("查询三者关系详情")
    @GetMapping("/query")
    public Result<CrmEnterpriseRelationShipListVO> query(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam(value = "id") Long id) {
        CrmEnterpriseRelationShipDTO enterpriseRelationShipDTO = crmEnterpriseRelationShipApi.queryById(id);
        if (ObjectUtil.isNull(enterpriseRelationShipDTO)) {
            return Result.success(new CrmEnterpriseRelationShipListVO());
        }
        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipDTOList = new ArrayList<>();
        Long crmEnterpriseId = enterpriseRelationShipDTO.getCrmEnterpriseId();
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if(ObjectUtil.isNotNull(crmEnterpriseDTO)){
            enterpriseRelationShipDTO.setProvinceName(crmEnterpriseDTO.getProvinceName());
            enterpriseRelationShipDTO.setCityName(crmEnterpriseDTO.getCityName());
            enterpriseRelationShipDTO.setRegionName(crmEnterpriseDTO.getRegionName());
            enterpriseRelationShipDTO.setSupplyChainRole(crmEnterpriseDTO.getSupplyChainRole() + "");
            enterpriseRelationShipDTO.setCustomerName(crmEnterpriseDTO.getName() + "");
        }else {
            enterpriseRelationShipDTO.setProvinceName(null);
            enterpriseRelationShipDTO.setCityName(null);
            enterpriseRelationShipDTO.setRegionName(null);
            enterpriseRelationShipDTO.setSupplyChainRole(null);
            enterpriseRelationShipDTO.setCustomerName(null);
        }
        List<CrmGoodsGroupDTO>  goodsCodeByGroupId = crmGoodsGroupApi.findGroupByIds(Arrays.asList(enterpriseRelationShipDTO.getProductGroupId()));
        if(CollectionUtil.isNotEmpty(goodsCodeByGroupId)){
            enterpriseRelationShipDTO.setProductGroup(goodsCodeByGroupId.get(0).getName());
        }
        buildEsbEmployInfo(Arrays.asList(enterpriseRelationShipDTO),true);
        CrmEnterpriseRelationShipListVO relationShipVO = PojoUtils.map(enterpriseRelationShipDTO, CrmEnterpriseRelationShipListVO.class);
        if (ObjectUtil.isNotNull(crmEnterpriseDTO)&&crmEnterpriseDTO.getSupplyChainRole() == 3) {
            List<CrmPharmacyDTO> crmPharmacyDTOS = crmPharmacyApi.listByCrmEnterpriseId(Arrays.asList(crmEnterpriseId));
            if (CollectionUtil.isNotEmpty(crmPharmacyDTOS)) {
                relationShipVO.setTargetOrNot(crmPharmacyDTOS.get(0).getTargetFlag()+"");
            }
        }
        return Result.success(relationShipVO);
    }

    /**
     * 是否重复三者关系数据
     *
     * @param historyRelationship
     * @return
     */
    private boolean isRepeatEnterpirseRelation(CrmEnterpriseRelationShipDTO formSaveRelationShip, List<CrmEnterpriseRelationShipDTO> historyRelationship) {
        //过滤下页面传递过来旧的新增的
        //2个list合并form表单和数据库保存数据
        historyRelationship.add(formSaveRelationShip);
        //检查是否重复 唯一key 岗位编码+产品组+经销商为key
        List<String> formRelationShipKey = historyRelationship.stream().map(this::getFormReationMapKey).collect(Collectors.toList());
        long count = formRelationShipKey.stream().distinct().count();
        return count < historyRelationship.size();
    }

    public String getFormReationMapKey(CrmEnterpriseRelationShipDTO form) {
        return (ObjectUtil.isNull(form.getProductGroupId())?"":form.getProductGroupId().toString()+(ObjectUtil.isNull(form.getCrmEnterpriseId())?"":form.getCrmEnterpriseId().toString()));
    }
    @ApiOperation(value = "业务代表工号")
    @GetMapping("/getByEmpId")
    public Result<EsbEmployVO> getInfoByEmpId(@RequestParam(value = "empId", required = false) String empId) {
        if(StringUtils.isBlank(empId)){
            return Result.failed(100030, "参数不能为空");
        }
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(empId, null,null);
        if (ObjectUtil.isEmpty(esbEmployeeDTO) && StringUtils.isNotBlank(empId)) {
            return Result.failed(100030, "您输入的工号不存在");
        }
        //通过循环获取部门。
        EsbEmployVO result = PojoUtils.map(esbEmployeeDTO, EsbEmployVO.class);
        return Result.success(result);
    }
}
