package com.yiling.ih.disease.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.config.FeignConfig;
import com.yiling.ih.disease.dto.DiseaseDTO;

/**
 * @author: gxl
 * @date: 2022/6/6
 */
@FeignClient(name="hospital",url = "${ih.service.baseUrl}",configuration = FeignConfig.class)
public interface DiseaseFeignClient {
    /**
     * 根据名称模糊搜索疾病
     * @param name
     * @return
     */
    @GetMapping("/cms/disease/diseaseSearch")
    ApiResult<ApiPage<DiseaseDTO>> diseaseSearch(@RequestParam("name")String name, @RequestParam("idList")  List<Integer> idList, @RequestParam("start") Integer start,@RequestParam("length") Integer length);
}
