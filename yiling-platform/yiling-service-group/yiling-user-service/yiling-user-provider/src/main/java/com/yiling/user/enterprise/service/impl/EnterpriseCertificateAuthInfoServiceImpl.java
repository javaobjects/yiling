package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dao.EnterpriseAuthInfoMapper;
import com.yiling.user.enterprise.dao.EnterpriseCertificateAuthInfoMapper;
import com.yiling.user.enterprise.dto.EnterpriseCertificateAuthInfoDTO;
import com.yiling.user.enterprise.dto.request.EnterpriseCertificateAuthInfoRequest;
import com.yiling.user.enterprise.entity.EnterpriseCertificateAuthInfoDO;
import com.yiling.user.enterprise.service.EnterpriseCertificateAuthInfoService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业资质副本 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2021/11/2
 */
@Service
public class EnterpriseCertificateAuthInfoServiceImpl extends BaseServiceImpl<EnterpriseCertificateAuthInfoMapper, EnterpriseCertificateAuthInfoDO> implements EnterpriseCertificateAuthInfoService {

    @Autowired
    EnterpriseAuthInfoMapper enterpriseAuthInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addEnterpriseCertificateAuth(List<EnterpriseCertificateAuthInfoRequest> request) {
        if(CollUtil.isEmpty(request)){
            return true;
        }

        List<EnterpriseCertificateAuthInfoDO> certificateAuthInfoList = PojoUtils.map(request,EnterpriseCertificateAuthInfoDO.class);
        certificateAuthInfoList = certificateAuthInfoList.stream().filter(enterpriseCertificateAuthInfoDO -> StrUtil.isNotEmpty(enterpriseCertificateAuthInfoDO.getFileKey())).collect(Collectors.toList());

        return this.saveBatch(certificateAuthInfoList);
    }

    @Override
    public List<EnterpriseCertificateAuthInfoDTO> getCertificateAuthInfoListByAuthId(Long enterpriseAuthId) {
        LambdaQueryWrapper<EnterpriseCertificateAuthInfoDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseCertificateAuthInfoDO::getEnterpriseAuthId,enterpriseAuthId);

        return PojoUtils.map(this.list(queryWrapper),EnterpriseCertificateAuthInfoDTO.class);

    }

}
