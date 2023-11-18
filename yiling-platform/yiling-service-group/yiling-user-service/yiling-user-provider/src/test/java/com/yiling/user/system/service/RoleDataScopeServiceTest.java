package com.yiling.user.system.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.BaseTest;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/11/10
 */
@Slf4j
public class RoleDataScopeServiceTest extends BaseTest {

    @Autowired
    RoleDataScopeService  roleDataScopeService;

    @Test
    public void getUserIdsByDataScope() {
        List<Long> userIds = roleDataScopeService.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, 1L, 2L);
        log.info("userIds = {}", JSONUtil.toJsonStr(userIds));
    }
}
