package com.yiling.ih.disease.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.disease.dto.DiseaseDTO;
import com.yiling.ih.disease.dto.request.QueryDiseaseListRequest;

/**
 * @author: gxl
 * @date: 2022/6/7
 */
public interface DiseaseApi {
    /**
     * 查询疾病信息
     * @param request
     * @return
     */
    Page<DiseaseDTO> queryDisease(QueryDiseaseListRequest request);
}
