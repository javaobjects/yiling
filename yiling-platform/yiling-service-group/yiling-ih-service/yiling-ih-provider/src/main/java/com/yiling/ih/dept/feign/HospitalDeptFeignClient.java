package com.yiling.ih.dept.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.config.FeignConfig;
import com.yiling.ih.dept.dto.HospitalDeptDTO;
import com.yiling.ih.dept.dto.request.QueryHospitalDeptListRequest;
import com.yiling.ih.dept.feign.response.HospitalDeptListResponse;

/**
 * 科室
 * @author: gxl
 * @date: 2022/6/7
 */
@FeignClient(name="hospital",url = "${ih.service.baseUrl}",configuration = FeignConfig.class)
public interface HospitalDeptFeignClient {
    @GetMapping("/cms/department/list")
    ApiResult<List<HospitalDeptDTO>> getDepartmentList();

    /**
     * 根据id批量查询
     * @param request
     * @return
     */
    @PostMapping("/cms/department/listByIds")
    ApiResult<List<HospitalDeptListResponse>> listByIds(@RequestBody QueryHospitalDeptListRequest request);
}
