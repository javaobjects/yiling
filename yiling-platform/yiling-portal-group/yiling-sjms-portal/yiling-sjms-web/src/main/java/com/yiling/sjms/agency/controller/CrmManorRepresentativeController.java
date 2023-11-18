package com.yiling.sjms.agency.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.SaveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmManorApi;
import com.yiling.dataflow.crm.api.CrmManorRepresentativeApi;
import com.yiling.dataflow.crm.dto.CrmManorDTO;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmManorRepresentativeRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.form.QueryManorRepresentativePageForm;
import com.yiling.sjms.agency.form.SaveCrmManorRepresentativeForm;
import com.yiling.sjms.agency.vo.CrmManorRepresentativeVO;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Api(tags = "辖区代表管理接口")
@RestController
@RequestMapping("/crm/agency/manor/r")
public class CrmManorRepresentativeController {
    @DubboReference
    private CrmManorApi crmManorApi;
    @DubboReference
    private CrmManorRepresentativeApi crmManorRepresentativeApi;
    @DubboReference
    private UserApi userApi;
    @DubboReference(timeout = 10 * 1000)
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi ;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    BusinessDepartmentApi businessDepartmentApi;
    @ApiOperation(value = "列表查询")
    @PostMapping("/pageList")
    public Result<Page<CrmManorRepresentativeVO>> pageList(@RequestBody QueryManorRepresentativePageForm form) {
        QueryCrmManorRepresentativePageRequest request = new QueryCrmManorRepresentativePageRequest();
        PojoUtils.map(form, request);
        Page<CrmManorRepresentativeDTO> crmManorRepresentativeDTOPage = crmManorRepresentativeApi.pageList(request);
        Page<CrmManorRepresentativeVO> pageListVOPage = new Page<>();
        if (CollUtil.isEmpty(crmManorRepresentativeDTOPage.getRecords())) {
            return Result.success(pageListVOPage);
        }
        //转化数据对象
        pageListVOPage=PojoUtils.map(crmManorRepresentativeDTOPage, CrmManorRepresentativeVO.class);
        //获取岗位编码集合
        //组装操作人集合
        List<Long> userIds = pageListVOPage.getRecords().stream().map(CrmManorRepresentativeVO::getUpdateUser).collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(userIds);
        //处理操作人名称处理
        if (CollectionUtil.isNotEmpty(userDTOS)) {
            Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
            pageListVOPage.getRecords().forEach(item -> {
                UserDTO crmHospitalDTO = userDTOMap.get(item.getUpdateUser());
                if (ObjectUtil.isNotEmpty(crmHospitalDTO)) {
                    item.setUpdateUserName(crmHospitalDTO.getName());
                }
            });
        }
        //处理岗位人员息
        buildEsbEmployInfo(pageListVOPage.getRecords(),false);
        return Result.success(pageListVOPage);
    }

    @ApiOperation("新增/编辑")
    @PostMapping("/save")
    public Result<Boolean> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCrmManorRepresentativeForm form) {
        SaveCrmManorRepresentativeRequest request = new SaveCrmManorRepresentativeRequest();
        log.info("辖区代表编辑保存辖区ID,岗位编码,是否是上级岗位{},{},{}",form.getManorId(),form.getRepresentativePostCode(),form.getSuperiorJob());
        PojoUtils.map(form, request);
        if(StringUtils.equals(form.getDutyGredeId(),"2")){
            request.setRepresentativePostCode(form.getSuperiorJob());
            request.setRepresentativePostName(form.getSuperiorJobName());
        }
        request.setOpUserId(userInfo.getCurrentUserId());
        Long aLong = crmManorRepresentativeApi.saveOrUpdate(request);
        return Result.success(aLong>0);
    }
    @ApiOperation(value = "详情代表信息")
    @GetMapping("/detail")
    public Result<CrmManorRepresentativeVO> detail(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "manorId", required = true) Long manorId) {
        CrmManorDTO crmManorDTO = crmManorApi.getManorById(manorId);
        CrmManorRepresentativeVO simpleVO = new CrmManorRepresentativeVO();


        //获取岗位信息
        CrmManorRepresentativeDTO byManorId = crmManorRepresentativeApi.getByManorId(manorId);
        if (Objects.nonNull(byManorId)) {
            //岗位编码
            PojoUtils.map(byManorId,simpleVO);
            buildEsbEmployInfo(Arrays.asList(simpleVO),true);
        }
        simpleVO.setManorId(manorId);
        simpleVO.setManorNo(crmManorDTO.getManorNo());
        simpleVO.setName(crmManorDTO.getName());
        return Result.success(simpleVO);
    }
    public void buildEsbEmployInfo(List<CrmManorRepresentativeVO> crmEnterpriseDTOS, boolean needProvinceInfo) {
        // 用岗位id名字postcode找到人员信息，业务部门，省区，业务区域等
        List<Long> postCodes = crmEnterpriseDTOS.stream().filter(item->(Objects.nonNull(item.getRepresentativePostCode())&&
                item.getRepresentativePostCode()!=0L)).map(CrmManorRepresentativeVO::getRepresentativePostCode).collect(Collectors.toList());
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
            Long postCode = item.getRepresentativePostCode();
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
                item.setRepresentativePostName(null);
                item.setRepresentativePostCode(null);
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
    public static void changeField(CrmManorRepresentativeVO item, EsbEmployeeDTO esbEmployeeDTO) {
        if(ObjectUtil.isNull(esbEmployeeDTO)){
            return;
        }
        item.setBusinessDepartment(esbEmployeeDTO.getYxDept());
        item.setBusinessProvince(esbEmployeeDTO.getYxProvince());
        item.setBusinessArea(esbEmployeeDTO.getYxArea());
        item.setSuperiorSupervisorCode(esbEmployeeDTO.getSuperior());
        item.setSuperiorSupervisorName(esbEmployeeDTO.getSuperiorName());
        item.setRepresentativeCode(esbEmployeeDTO.getEmpId());
        item.setRepresentativeName(esbEmployeeDTO.getEmpName());
        item.setRepresentativePostCode(esbEmployeeDTO.getJobId());
        item.setRepresentativePostName(esbEmployeeDTO.getJobName());
    }
}
