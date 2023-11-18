package com.yiling.ih.disease.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.disease.api.DiseaseApi;
import com.yiling.ih.disease.dto.DiseaseDTO;
import com.yiling.ih.disease.dto.request.QueryDiseaseListRequest;
import com.yiling.ih.disease.feign.DiseaseFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/6/7
 */
@Slf4j
@DubboService
public class DiseaseApiImpl implements DiseaseApi {

    @Autowired
    private DiseaseFeignClient diseaseFeignClient;

    @Override
    public Page<DiseaseDTO> queryDisease(QueryDiseaseListRequest request) {
        ApiResult<ApiPage<DiseaseDTO>> diseaseDTOResult = diseaseFeignClient.diseaseSearch(request.getName(),request.getIdList(),request.getCurrent(),request.getSize());
        ApiPage<DiseaseDTO> data = diseaseDTOResult.getData();
        Page<DiseaseDTO> diseaseDTOPage = request.getPage();
        diseaseDTOPage.setRecords(data.getList()).setTotal(data.getTotal());
        return diseaseDTOPage;
    }
}