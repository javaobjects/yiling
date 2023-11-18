package com.yiling.ih.user;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import com.yiling.ih.BaseTest;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.DoctorInfoDTO;
import com.yiling.ih.user.dto.request.QueryMrDoctorListRequest;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@Slf4j
public class DoctorApiTest extends BaseTest {

    @DubboReference
    DoctorApi doctorApi;

    @Test
    public void listByMrId() {
        QueryMrDoctorListRequest request = new QueryMrDoctorListRequest();
        request.setMrId(1L);
        request.setDoctorName("啸");

        List<DoctorInfoDTO> list = doctorApi.listByMrId(request);
        log.info("list = {}", JSONUtil.toJsonStr(list));
    }
}
