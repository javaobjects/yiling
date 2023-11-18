package com.yiling.ih.user;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.ih.BaseTest;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.user.feign.DoctorFeignClient;
import com.yiling.ih.user.feign.dto.request.GetDoctorListByAgentIdRequest;
import com.yiling.ih.user.feign.dto.response.GetDoctorListByAgentIdResponse;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/6/16
 */
@Slf4j
public class DoctorFeignClientTest extends BaseTest {

    @Autowired
    DoctorFeignClient doctorFeignClient;

    @Test
    public void getDoctorListByAgentId() {
        ApiResult<List<GetDoctorListByAgentIdResponse>> result = doctorFeignClient.getDoctorListByAgentId(1L, "");
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }
}
