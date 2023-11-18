package com.yiling.ih.dept.api;

import java.util.List;

import com.yiling.ih.dept.dto.HospitalDeptDTO;
import com.yiling.ih.dept.dto.HospitalDeptListDTO;
import com.yiling.ih.dept.dto.request.QueryHospitalDeptListRequest;

/**
 * @author: gxl
 * @date: 2022/6/6
 */
public interface HospitalDeptApi {

    /**
     * 查询科室 树
     * @return
     */
    List<HospitalDeptDTO> getDepartmentList();

    /**
     * 根据id批量查
     * @return
     */
    List<HospitalDeptListDTO> listByIds(QueryHospitalDeptListRequest queryHospitalDeptListRequest);
}
