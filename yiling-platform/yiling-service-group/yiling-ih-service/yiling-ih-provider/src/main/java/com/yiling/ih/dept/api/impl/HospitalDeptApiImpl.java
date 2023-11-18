package com.yiling.ih.dept.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.dept.api.HospitalDeptApi;
import com.yiling.ih.dept.dto.HospitalDeptDTO;
import com.yiling.ih.dept.dto.HospitalDeptListDTO;
import com.yiling.ih.dept.dto.request.QueryHospitalDeptListRequest;
import com.yiling.ih.dept.feign.HospitalDeptFeignClient;
import com.yiling.ih.dept.feign.response.HospitalDeptListResponse;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 医院科室
 * @author: gxl
 * @date: 2022/6/7
 */
@Slf4j
@DubboService
public class HospitalDeptApiImpl implements HospitalDeptApi {

    @Autowired
    private HospitalDeptFeignClient hospitalDeptFeignClient;

    @Override
    public List<HospitalDeptDTO> getDepartmentList() {
        ApiResult<List<HospitalDeptDTO>> result = hospitalDeptFeignClient.getDepartmentList();
        if(result.success()){
            return result.getData();
        }
        return ListUtil.empty();
    }

    @Override
    public List<HospitalDeptListDTO> listByIds(QueryHospitalDeptListRequest queryHospitalDeptListRequest) {
        ApiResult<List<HospitalDeptListResponse>> apiResult = hospitalDeptFeignClient.listByIds(queryHospitalDeptListRequest);
        if(apiResult.success()){
            List<HospitalDeptListResponse> list = apiResult.getData();
            if (CollUtil.isEmpty(list)) {
                return ListUtil.empty();
            }
            List<HospitalDeptListDTO> resultList = list.stream().map(e -> this.convert(e)).collect(Collectors.toList());
            return resultList;
        }
        return ListUtil.empty();
    }

    private HospitalDeptListDTO  convert(HospitalDeptListResponse response){
        HospitalDeptListDTO hospitalDeptListDTO = new HospitalDeptListDTO();
        hospitalDeptListDTO.setLabel(response.getDepartmentName());
        hospitalDeptListDTO.setId(response.getId());
        return hospitalDeptListDTO;
    }
}