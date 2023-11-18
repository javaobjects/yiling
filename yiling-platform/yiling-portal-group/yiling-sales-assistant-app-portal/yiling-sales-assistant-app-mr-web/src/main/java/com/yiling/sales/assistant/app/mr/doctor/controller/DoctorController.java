package com.yiling.sales.assistant.app.mr.doctor.controller;

import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.ih.user.dto.DoctorListAppInfoDTO;
import com.yiling.ih.user.dto.request.QueryAppMrDoctorListRequest;
import com.yiling.sales.assistant.app.mr.doctor.form.QueryDoctorPageForm;
import com.yiling.sales.assistant.app.mr.doctor.vo.DoctorInfoVO;
import com.yiling.sales.assistant.app.mr.doctor.vo.DoctorListVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 销售助手APP-医生模块 前端控制器
 * </p>
 * @author: benben.jia
 * @data: 2022/06/14
 */
@Api(tags = "医生模块")
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @DubboReference
    DoctorApi doctorApi;
    @DubboReference
    EmployeeApi employeeApi;

    @ApiOperation(value = "医生列表")
    @GetMapping("/doctorList")
    public Result<Page<DoctorListVO>> doctorList(@CurrentUser CurrentStaffInfo staffInfo, QueryDoctorPageForm form) {
        QueryAppMrDoctorListRequest request = new QueryAppMrDoctorListRequest();
        PojoUtils.map(form, request);
        EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getByEidUserId(staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        if (Objects.isNull(enterpriseEmployeeDTO)) {
            return Result.failed("员工信息不存在");
        }
        request.setMrId(enterpriseEmployeeDTO.getId());
        Page<DoctorListAppInfoDTO> doctorDTOPage = doctorApi.listAppByMrId(request);
        if (doctorDTOPage.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        Page<DoctorListVO> result = PojoUtils.map(doctorDTOPage, DoctorListVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "医生详情")
    @GetMapping("/doctorInfo")
    public Result<DoctorInfoVO> doctorInfo(@RequestParam Integer doctorId) {
        DoctorAppInfoDTO doctorDTOPage = doctorApi.getDoctorInfoByDoctorId(doctorId);
        DoctorInfoVO map = PojoUtils.map(doctorDTOPage, DoctorInfoVO.class);
        return  Result.success(map);
    }
}
