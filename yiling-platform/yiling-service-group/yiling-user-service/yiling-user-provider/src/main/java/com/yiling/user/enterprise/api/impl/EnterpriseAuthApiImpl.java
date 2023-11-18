package com.yiling.user.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.api.EnterpriseAuthApi;
import com.yiling.user.enterprise.dto.EnterpriseAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseAuthInfoRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseAuthPageRequest;
import com.yiling.user.enterprise.dto.request.UpdateEnterpriseAuthRequest;
import com.yiling.user.enterprise.service.EnterpriseAuthInfoService;

/**
 * 企业副本 API 实现
 *
 * @author: lun.yu
 * @date: 2021/11/2
 */
@DubboService
public class EnterpriseAuthApiImpl implements EnterpriseAuthApi {

    @Autowired
    EnterpriseAuthInfoService enterpriseAuthInfoService;

    @Override
    public Long addEnterpriseAuth(EnterpriseAuthInfoRequest request) {

        return enterpriseAuthInfoService.addEnterpriseAuth(request);
    }

    @Override
    public EnterpriseAuthInfoDTO getByEid(Long eid) {

        return enterpriseAuthInfoService.getByEid(eid);
    }

    @Override
    public List<EnterpriseAuthInfoDTO> queryListByEid(Long eid) {

        return enterpriseAuthInfoService.queryListByEid(eid);
    }

    @Override
    public Page<EnterpriseAuthInfoDTO> pageList(QueryEnterpriseAuthPageRequest request) {

        return enterpriseAuthInfoService.pageList(request);
    }

    @Override
    public Boolean updateAuth(UpdateEnterpriseAuthRequest request) {

        return enterpriseAuthInfoService.updateAuth(request);
    }
}
