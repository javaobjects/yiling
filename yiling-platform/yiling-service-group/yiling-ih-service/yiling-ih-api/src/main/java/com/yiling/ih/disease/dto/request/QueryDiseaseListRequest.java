package com.yiling.ih.disease.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

/**
 * 查询疾病入参
 * @author gaoxinlei
 * @date 2022-6-8
 */
@Data
public class QueryDiseaseListRequest extends QueryPageListRequest {


    private static final long serialVersionUID = 2574332310936289764L;
    private String name;

    private List<Integer> idList;

}
