package com.yiling.sales.assistant.app.mr.contact.controller;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorInfoDTO;
import com.yiling.ih.user.dto.request.QueryMrDoctorListRequest;
import com.yiling.sales.assistant.app.mr.contact.vo.ContactDoctorListVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author: hongyang.zhang
 * @data: 2022/06/08
 */
@Api(tags = "通讯录模块")
@RestController
@RequestMapping("/contact")
public class ContactController {

    @DubboReference
    DoctorApi doctorApi;
    @DubboReference
    EmployeeApi employeeApi;


    @ApiOperation(value = "代表通讯录")
    @GetMapping("/doctorList")
    public Result<CollectionObject<ContactDoctorListVO>> doctorList(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(required = false) @ApiParam(value = "医生名称") String doctorName) {

        QueryMrDoctorListRequest request = new QueryMrDoctorListRequest();
        EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getByEidUserId(staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        if (Objects.isNull(enterpriseEmployeeDTO)) {
            return Result.failed("员工信息不存在");
        }
        request.setMrId(enterpriseEmployeeDTO.getId());
        if (StringUtils.isNotBlank(doctorName)) {
            request.setDoctorName(doctorName);
        }
        List<DoctorInfoDTO> doctorInfoDTOList = doctorApi.listByMrId(request);
        List<ContactDoctorListVO> list = PojoUtils.map(doctorInfoDTOList, ContactDoctorListVO.class);
        CollectionObject<ContactDoctorListVO> result = new CollectionObject(list);
        return Result.success(result);
    }
}
