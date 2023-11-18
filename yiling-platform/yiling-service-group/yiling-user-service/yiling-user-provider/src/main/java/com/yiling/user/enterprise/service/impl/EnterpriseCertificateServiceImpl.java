package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dao.EnterpriseCertificateMapper;
import com.yiling.user.enterprise.dto.request.CreateEnterpriseCertificateRequest;
import com.yiling.user.enterprise.entity.EnterpriseCertificateDO;
import com.yiling.user.enterprise.service.EnterpriseCertificateService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业资质 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-21
 */
@Service
public class EnterpriseCertificateServiceImpl extends BaseServiceImpl<EnterpriseCertificateMapper, EnterpriseCertificateDO> implements EnterpriseCertificateService {

    @Override
    public List<EnterpriseCertificateDO> listByEid(Long eid) {
        QueryWrapper<EnterpriseCertificateDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnterpriseCertificateDO::getEid, eid);
        List<EnterpriseCertificateDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public boolean saveCertificateList(List<CreateEnterpriseCertificateRequest> certificateList) {
        if(CollUtil.isNotEmpty(certificateList)){
            List<EnterpriseCertificateDO> certificateDoList = PojoUtils.map(certificateList, EnterpriseCertificateDO.class);
            certificateDoList = certificateDoList.stream().filter(enterpriseCertificateDO -> StrUtil.isNotEmpty(enterpriseCertificateDO.getFileKey())).collect(Collectors.toList());
            return this.saveBatch(certificateDoList);
        }
        return false;
    }

    @Override
    public boolean deleteByEid(Long eid) {
        EnterpriseCertificateDO certificateDO = new EnterpriseCertificateDO();

        LambdaQueryWrapper<EnterpriseCertificateDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseCertificateDO::getEid,eid);

        return this.batchDeleteWithFill(certificateDO,queryWrapper) >= 0;

    }
}
