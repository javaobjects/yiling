package com.yiling.ih.dept.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/6/10
 */
@Data
public class QueryHospitalDeptListRequest extends BaseRequest {

    private static final long serialVersionUID = -9164977711080987045L;
    private List<Integer> ids;
}