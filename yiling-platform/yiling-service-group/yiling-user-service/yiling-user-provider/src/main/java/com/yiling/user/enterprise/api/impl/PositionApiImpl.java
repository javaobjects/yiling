package com.yiling.user.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.PositionApi;
import com.yiling.user.enterprise.dto.EnterprisePositionDTO;
import com.yiling.user.enterprise.dto.request.QueryPositionPageListRequest;
import com.yiling.user.enterprise.dto.request.SavePositionRequest;
import com.yiling.user.enterprise.dto.request.UpdatePositionStatusRequest;
import com.yiling.user.enterprise.entity.EnterprisePositionDO;
import com.yiling.user.enterprise.service.EnterprisePositionService;

/**
 * 职位模块 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@DubboService
public class PositionApiImpl implements PositionApi {

    @Autowired
    private EnterprisePositionService enterprisePositionService;

    @Override
    public Page<EnterprisePositionDTO> pageList(QueryPositionPageListRequest request) {
        Page<EnterprisePositionDO> page = enterprisePositionService.pageList(request);
        return PojoUtils.map(page, EnterprisePositionDTO.class);
    }

    @Override
    public EnterprisePositionDTO getById(Long id) {
        return PojoUtils.map(enterprisePositionService.getById(id), EnterprisePositionDTO.class);
    }

    @Override
    public List<EnterprisePositionDTO> listByIds(List<Long> ids) {
        return PojoUtils.map(enterprisePositionService.listByIds(ids), EnterprisePositionDTO.class);
    }

    @Override
    public boolean save(SavePositionRequest request) {
        return enterprisePositionService.save(request);
    }

    @Override
    public boolean updateStatus(UpdatePositionStatusRequest request) {
        return enterprisePositionService.updateStatus(request);
    }

}
