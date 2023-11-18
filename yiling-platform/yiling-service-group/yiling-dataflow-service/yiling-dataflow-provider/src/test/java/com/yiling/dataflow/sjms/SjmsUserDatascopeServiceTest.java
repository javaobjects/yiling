package com.yiling.dataflow.sjms;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.service.SjmsUserDatascopeService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/3/28
 */
@Slf4j
public class SjmsUserDatascopeServiceTest extends BaseTest {

    @Autowired
    SjmsUserDatascopeService sjmsUserDatascopeService;

    @Test
    public void listAuthorizedEids() {
        List<Long> ids = sjmsUserDatascopeService.listAuthorizedEids("YX00286");
        log.info("ids={}", JSONUtil.toJsonStr(ids));
    }

    @Test
    public void getByEmpId() {
        SjmsUserDatascopeBO sjmsUserDatascopeBO = sjmsUserDatascopeService.getByEmpId("24154");
        log.info("sjmsUserDatascopeBO={}", JSONUtil.toJsonStr(sjmsUserDatascopeBO));
    }
}
