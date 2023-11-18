package com.yiling.ih.disease;

import java.util.List;
import java.util.Map;

import cn.hutool.json.JSONUtil;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.HmcIMChatRootQueryDiagnosisRecordDTO;
import com.yiling.ih.patient.dto.request.QueryDiagnosisRecordByUserIdAndDocIdRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.yiling.framework.common.pojo.Result;
import com.yiling.ih.BaseTest;
import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.disease.api.DiseaseApi;
import com.yiling.ih.disease.dto.DiseaseDTO;
import com.yiling.ih.disease.dto.request.QueryDiseaseListRequest;
import com.yiling.ih.disease.feign.DiseaseFeignClient;

import cn.hutool.http.HttpUtil;

/**
 * @author: gxl
 * @date: 2022/6/7
 */

@Slf4j
public class DiseaseTest extends BaseTest {

    @DubboReference(url = "dubbo://localhost:18018")
    DiseaseApi diseaseApi;
    @Autowired
    DiseaseFeignClient diseaseFeignClient;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @Test
    public void query() {
        QueryDiagnosisRecordByUserIdAndDocIdRequest request = new QueryDiagnosisRecordByUserIdAndDocIdRequest();
        request.setFromUserId(177);
        request.setDocId(104);
        request.setPatientId(138);
        HmcIMChatRootQueryDiagnosisRecordDTO result = diagnosisApi.imCharRoomQueryDiagnosisOrderWithDoc(request);
        log.info(JSONUtil.toJsonStr(result));
    }

    @Test
    public void test1() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("diseaseName", "胃痛");
        String s = HttpUtil.get("https://lbtest.ylhlwyy.com/cms/disease/diseaseSearch", map);
        System.out.println(s);
    }

    @Test
    public void test2() {
        QueryDiseaseListRequest request = new QueryDiseaseListRequest();
        request.setName("胃痛");
        ApiResult<ApiPage<DiseaseDTO>> apiPageApiResult = diseaseFeignClient.diseaseSearch("胃痛", null, 1, 10);
        System.out.println(apiPageApiResult.toString());
    }

}