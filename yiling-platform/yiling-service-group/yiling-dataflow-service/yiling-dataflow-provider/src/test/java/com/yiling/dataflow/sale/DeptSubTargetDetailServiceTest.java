package com.yiling.dataflow.sale;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.BaseTest;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailService;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;
import com.yiling.dataflow.sjms.service.SjmsUserDatascopeService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2023/4/14
 */
@Slf4j
public class DeptSubTargetDetailServiceTest extends BaseTest {

    @Autowired
    SaleDepartmentTargetService saleDepartmentTargetService;

    @Test
    public void listAuthorizedEids() {
        saleDepartmentTargetService.generateMould(45L,10527L);
        System.err.println("aaaaaa");
    }
}
