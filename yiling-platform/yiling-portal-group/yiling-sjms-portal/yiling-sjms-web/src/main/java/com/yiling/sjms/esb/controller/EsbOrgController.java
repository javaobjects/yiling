package com.yiling.sjms.esb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.sjms.agency.vo.EsbEmployVO;
import com.yiling.sjms.esb.form.QueryEsbEmployeePageListForm;
import com.yiling.sjms.esb.vo.EsbEmployeeJobInfoVO;
import com.yiling.sjms.esb.vo.EsbEmployeeSimpleVO;
import com.yiling.sjms.esb.vo.EsbOrgSimpleInfoVO;
import com.yiling.sjms.esb.vo.EsbOrgTreeNodeVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryEsbEmployeePageListRequest;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * ESB组织架构接口
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Slf4j
@RestController
@RequestMapping("/esb/org")
@Api(tags = "ESB组织架构接口")
public class EsbOrgController extends BaseController {

    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    EsbEmployeeApi employeeApi;

    @ApiOperation(value = "查询ESB员工分页列表")
    @PostMapping("/pageList")
    public Result<Page<EsbEmployeeSimpleVO>> pageList(@RequestBody QueryEsbEmployeePageListForm form) {
        QueryEsbEmployeePageListRequest request = PojoUtils.map(form, QueryEsbEmployeePageListRequest.class);
        Page<EsbEmployeeDTO> page = employeeApi.pageList(request);
        return Result.success(PojoUtils.map(page, EsbEmployeeSimpleVO.class));
    }

    @ApiOperation(value = "获取ESB组织架构树中某个节点的下级节点信息")
    @GetMapping("/listByPid")
    public Result<CollectionObject<EsbOrgTreeNodeVO>> listByPid(@RequestParam Long orgPid) {
        List<EsbOrgTreeNodeVO> voList = CollUtil.newArrayList();

        List<EsbEmployeeDTO> empList = employeeApi.listByDeptId(orgPid);
        if (CollUtil.isNotEmpty(empList)) {
            voList.addAll(empList.stream().map(e -> new EsbOrgTreeNodeVO(e.getEmpId(), e.getEmpName(), 2)).collect(Collectors.toList()));
        }

        List<EsbOrganizationDTO> orgList = esbOrganizationApi.listByPid(orgPid);
        if (CollUtil.isNotEmpty(orgList)) {
            voList.addAll(orgList.stream().map(e -> new EsbOrgTreeNodeVO(e.getOrgId().toString(), e.getOrgName(), 1)).collect(Collectors.toList()));
        }

        return Result.success(new CollectionObject<>(voList));
    }

    @ApiOperation(value = "根据名称模糊搜索组织架构信息")
    @GetMapping("/listByOrgName")
    public Result<List<EsbOrgSimpleInfoVO>> listByOrgName(@RequestParam(value = "orgName") String orgName) {
        List<EsbOrganizationDTO> esbOrganizationDTOList = esbOrganizationApi.findByOrgName(orgName, null);
        return Result.success(PojoUtils.map(esbOrganizationDTOList, EsbOrgSimpleInfoVO.class));
    }

    @ApiOperation(value = "根据员工姓名获取业务部门和姓名")
    @GetMapping("/getEsbEmployByName")
    public Result<List<EsbEmployVO>> getEsbEmployByName(@RequestParam(value = "name", required = false) String name) {
        List<EsbEmployeeDTO> empInfoByName = employeeApi.getEmpInfoByName(name);
        return Result.success(PojoUtils.map(empInfoByName, EsbEmployVO.class));
    }

    @ApiOperation(value = "根据工号或岗位代码员工相关信息")
    @GetMapping("/getByEmpIdOrJobId")
    public Result<EsbEmployeeJobInfoVO> getByEmpIdOrJobId(@RequestParam(value = "empId", required = false) String empId, @RequestParam(value = "jobId", required = false) String jobId) {

        EsbEmployeeDTO esbEmployeeDTO = employeeApi.getByEmpIdOrJobId(empId, jobId, null);
        EsbEmployeeJobInfoVO esbEmployeeJobInfoVO = PojoUtils.map(esbEmployeeDTO, EsbEmployeeJobInfoVO.class);
        if (esbEmployeeJobInfoVO == null) {
            return Result.success();
        }

        // 获取上级主管信息
        String superiorEmpId = esbEmployeeDTO.getSuperior();
        if (StringUtils.isNotEmpty(superiorEmpId)) {

            EsbEmployeeDTO superiorEsbEmployeeDTO = employeeApi.getByEmpIdOrJobId(superiorEmpId, null,  null);
            if (superiorEsbEmployeeDTO != null) {
                esbEmployeeJobInfoVO.setSuperior(superiorEsbEmployeeDTO.getEmpId());
                esbEmployeeJobInfoVO.setSuperiorName(superiorEsbEmployeeDTO.getEmpName());
                esbEmployeeJobInfoVO.setSuperiorJobId(superiorEsbEmployeeDTO.getJobId());
                esbEmployeeJobInfoVO.setSuperiorJobName(superiorEsbEmployeeDTO.getJobName());
            }
        }

        return Result.success(esbEmployeeJobInfoVO);
    }
}
