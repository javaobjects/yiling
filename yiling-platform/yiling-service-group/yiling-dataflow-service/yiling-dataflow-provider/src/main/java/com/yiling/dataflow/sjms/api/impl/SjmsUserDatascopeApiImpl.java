package com.yiling.dataflow.sjms.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.service.SjmsUserDatascopeService;

import cn.hutool.core.lang.Assert;

/**
 * 数据洞察系统数据权限 API 实现
 *
 * @author: xuan.zhou
 * @date: 2023/3/28
 */
@DubboService
public class SjmsUserDatascopeApiImpl implements SjmsUserDatascopeApi {

    @Autowired
    SjmsUserDatascopeService sjmsUserDatascopeService;

    @Override
    public List<Long> listAuthorizedEids(String empId) {
        Assert.notEmpty(empId, "参数empId不能为空");
        return sjmsUserDatascopeService.listAuthorizedEids(empId);
    }

    @Override
    public SjmsUserDatascopeBO getByEmpId(String empId) {
        Assert.notEmpty(empId, "参数empId不能为空");
        return sjmsUserDatascopeService.getByEmpId(empId);
    }
}
