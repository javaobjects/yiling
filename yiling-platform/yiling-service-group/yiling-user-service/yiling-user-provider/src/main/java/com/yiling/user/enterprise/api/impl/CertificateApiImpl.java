package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.CertificateApi;
import com.yiling.user.enterprise.dto.EnterpriseCertificateDTO;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.entity.EnterpriseCertificateDO;
import com.yiling.user.enterprise.service.EnterpriseCertificateService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * 企业资质 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@DubboService
public class CertificateApiImpl implements CertificateApi {

    @Autowired
    EnterpriseCertificateService enterpriseCertificateService;

    @Override
    public List<EnterpriseCertificateDTO> listByEid(Long eid) {
        Assert.notNull(eid, "参数eid不能为空");
        return PojoUtils.map(enterpriseCertificateService.listByEid(eid), EnterpriseCertificateDTO.class);
    }

    @Override
    public boolean save(List<CreateEnterpriseCertificateRequest> certificateList) {
        return enterpriseCertificateService.saveCertificateList(certificateList);
    }

    @Override
    public boolean deleteByEid(Long eid) {
        return enterpriseCertificateService.deleteByEid(eid);
    }
}
