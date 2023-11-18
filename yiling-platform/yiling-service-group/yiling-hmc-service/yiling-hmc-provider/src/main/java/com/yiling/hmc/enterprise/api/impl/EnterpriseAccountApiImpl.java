package com.yiling.hmc.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.enterprise.api.EnterpriseAccountApi;
import com.yiling.hmc.enterprise.dto.EnterpriseAccountDTO;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountPageRequest;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountSaveRequest;
import com.yiling.hmc.enterprise.entity.EnterpriseAccountDO;
import com.yiling.hmc.enterprise.service.EnterpriseAccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseAccountApiImpl implements EnterpriseAccountApi {

    private final EnterpriseAccountService enterpriseAccountService;

    @Override
    public boolean saveEnterpriseAccount(EnterpriseAccountSaveRequest request) {
        return enterpriseAccountService.saveEnterpriseAccount(request);
    }

    @Override
    public EnterpriseAccountDTO queryById(Long id) {
        EnterpriseAccountDO enterpriseAccountDO = enterpriseAccountService.getById(id);
        return PojoUtils.map(enterpriseAccountDO, EnterpriseAccountDTO.class);
    }

    @Override
    public EnterpriseAccountDTO queryByEid(Long eid) {
        EnterpriseAccountDO enterpriseAccountDO = enterpriseAccountService.queryByEid(eid);
        return PojoUtils.map(enterpriseAccountDO, EnterpriseAccountDTO.class);
    }

    @Override
    public Page<EnterpriseAccountDTO> pageList(EnterpriseAccountPageRequest request) {
        Page<EnterpriseAccountDO> doPage = enterpriseAccountService.pageList(request);
        return PojoUtils.map(doPage, EnterpriseAccountDTO.class);
    }

    @Override
    public List<EnterpriseAccountDTO> getAll() {
        // todo
        return null;
    }
}
