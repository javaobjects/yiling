package com.yiling.user.system.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.BaseTest;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/4/22
 */
@Slf4j
public class HmcUserServiceTest extends BaseTest {

    @Autowired
    HmcUserService hmcUserService;

    @Test
    public void pageList() {
        QueryHmcUserPageListRequest request = new QueryHmcUserPageListRequest();
//        request.setAppId("wx6c68152400453352");
        Page<HmcUser> page = hmcUserService.pageList(request);
        log.info("page = {}", JSONUtil.toJsonStr(page));
    }
}
