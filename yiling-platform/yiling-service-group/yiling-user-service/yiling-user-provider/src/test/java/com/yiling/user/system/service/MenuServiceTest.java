package com.yiling.user.system.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.BaseTest;
import com.yiling.user.system.bo.MenuListItemBO;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/7/6
 */
@Slf4j
public class MenuServiceTest extends BaseTest {

    @Autowired
    MenuService menuService;

    @Test
    public void listMenuTreeByUser() {
        List<MenuListItemBO> list = menuService.listMenuTreeByUser(PermissionAppEnum.MALL_ADMIN_POP, MenuTypeEnum.all(), 1L, 1L);
        log.info("list = {}", JSONUtil.toJsonStr(list));
    }

    @Test
    public void listMenuTreeByAppId() {
        List<MenuListItemBO> list = menuService.listMenuTreeByAppId(PermissionAppEnum.MALL_ADMIN_POP, MenuTypeEnum.all(), EnableStatusEnum.ENABLED);
        log.info("list = {}", JSONUtil.toJsonStr(list));
    }
}
