package com.yiling.open.erp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.open.erp.dao.ClientToolVersionMapper;
import com.yiling.open.erp.dto.ClientToolVersionDTO;
import com.yiling.open.erp.dto.request.ClientToolVersionQueryRequest;
import com.yiling.open.erp.entity.ClientToolVersionDO;
import com.yiling.open.erp.service.ClientToolVersionService;

/**
 * @author shuan
 */
@Service
public class ClientToolVersionServiceImpl extends BaseServiceImpl<ClientToolVersionMapper, ClientToolVersionDO> implements ClientToolVersionService {

    @Override
    public Page<ClientToolVersionDTO> getAppVersions(ClientToolVersionQueryRequest request) {
        Page<ClientToolVersionDO> page = new Page<>(request.getCurrent(), request.getSize());

        QueryWrapper<ClientToolVersionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(ClientToolVersionDO::getCreateTime);
        page = this.page(page, queryWrapper);

        Page<ClientToolVersionDTO> newPage = new Page<>(request.getCurrent(), request.getSize());
        newPage.setRecords(this.toDTOList(page.getRecords()));
        newPage.setTotal(page.getTotal());
        return newPage;
    }

    @Override
    public ClientToolVersionDTO getAppVersionByLast() {
        QueryWrapper<ClientToolVersionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ClientToolVersionDO::getDelFlag, 0).orderByDesc(ClientToolVersionDO::getCreateTime);
        queryWrapper.last("limit 1");
        return toDTO(this.getOne(queryWrapper));
    }

    private List<ClientToolVersionDTO> toDTOList(List<ClientToolVersionDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<ClientToolVersionDTO> dtoList = new ArrayList<>(list.size());
        for (ClientToolVersionDO record : list) {
            dtoList.add(this.toDTO(record));
        }

        return dtoList;
    }

    private ClientToolVersionDTO toDTO(ClientToolVersionDO clientToolVersion) {
        if (clientToolVersion == null) {
            return null;
        }

        ClientToolVersionDTO appVersionDTO = new ClientToolVersionDTO();
        BeanUtils.copyProperties(clientToolVersion, appVersionDTO);

        return appVersionDTO;
    }

}
