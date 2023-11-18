package com.yiling.hmc.enterprise.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.enterprise.dao.EnterpriseAccountMapper;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountPageRequest;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountSaveRequest;
import com.yiling.hmc.enterprise.entity.EnterpriseAccountDO;
import com.yiling.hmc.enterprise.enums.HmcEnterpriseErrorCode;
import com.yiling.hmc.enterprise.service.EnterpriseAccountService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.OpenPlatformRequest;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;

import cn.hutool.core.collection.CollUtil;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * <p>
 * 保险药品商家结算账号表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Service
public class EnterpriseAccountServiceImpl extends BaseServiceImpl<EnterpriseAccountMapper, EnterpriseAccountDO> implements EnterpriseAccountService {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    @GlobalTransactional
    public boolean saveEnterpriseAccount(EnterpriseAccountSaveRequest request) {
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
        EnterpriseHmcTypeEnum enterpriseHmcTypeEnum = EnterpriseHmcTypeEnum.getByCode(enterpriseDTO.getHmcType());
        // 若是次商家没有开通C端药+险业务，进行创建的时候会给其开通药+险的销售与兑付功能
        // 若是已经开通了C端药+险业务中的保险销售业务，是不允许选择此商家的。
        if (null == enterpriseHmcTypeEnum) {
            OpenPlatformRequest openPlatformRequest = new OpenPlatformRequest();
            openPlatformRequest.setEid(request.getEid());
            openPlatformRequest.setEnterpriseHmcTypeEnum(EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK);
            List<PlatformEnum> platformEnumList = CollUtil.newArrayList();
            platformEnumList.add(PlatformEnum.HMC);
            openPlatformRequest.setPlatformEnumList(platformEnumList);
            openPlatformRequest.setOpUserId(request.getOpUserId());
            openPlatformRequest.setOpTime(request.getOpTime());
            enterpriseApi.openPlatform(openPlatformRequest);
        }

        // 此商家未开通'C端药+险的销售与药品兑付'功能请先设置
        if (EnterpriseHmcTypeEnum.MEDICINE_INSURANCE == enterpriseHmcTypeEnum) {
            throw new BusinessException(HmcEnterpriseErrorCode.ENTERPRISE_ACCOUNT_TYPE_ERROR);
        }

        EnterpriseAccountDO enterpriseAccountDO = PojoUtils.map(request, EnterpriseAccountDO.class);
        if (null != request.getId()) {
            return this.updateById(enterpriseAccountDO);
        } else {
            return this.save(enterpriseAccountDO);
        }
    }

    @Override
    public EnterpriseAccountDO queryByEid(Long eid) {
        QueryWrapper<EnterpriseAccountDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(EnterpriseAccountDO::getEid, eid);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public Page<EnterpriseAccountDO> pageList(EnterpriseAccountPageRequest request) {
        QueryWrapper<EnterpriseAccountDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(EnterpriseAccountDO::getEname, request.getEname());
        }
        wrapper.lambda().orderByDesc(EnterpriseAccountDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }
}
