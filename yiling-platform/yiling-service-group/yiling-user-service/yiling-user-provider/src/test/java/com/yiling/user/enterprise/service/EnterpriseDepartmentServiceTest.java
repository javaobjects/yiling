package com.yiling.user.enterprise.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.BaseTest;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnterpriseDepartmentServiceTest extends BaseTest {

    @Autowired
    private EnterpriseDepartmentService enterpriseDepartmentService;

    @Test
    public void listTreeByEid() {
        List<Tree<Long>> treeNodes = enterpriseDepartmentService.listTreeByEid(1L, EnableStatusEnum.ALL);
        log.info("treeNodes = {}", JSONUtil.toJsonStr(treeNodes));
    }

}
