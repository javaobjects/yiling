package com.yiling.mall.banner.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.VajraPositionApi;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionDeleteRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionWeightRequest;
import com.yiling.mall.banner.dto.request.SaveB2bAppVajraPositionRequest;
import com.yiling.mall.banner.entity.B2bAppVajraPositionDO;
import com.yiling.mall.banner.service.B2bAppVajraPositionService;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@DubboService
public class VajraPositionApiImpl implements VajraPositionApi {

    @Autowired
    private B2bAppVajraPositionService service;

    @Override
    public B2bAppVajraPositionDTO queryById(Long id) {
        B2bAppVajraPositionDO b2bAppVajraPositionDO = service.getById(id);
        return PojoUtils.map(b2bAppVajraPositionDO, B2bAppVajraPositionDTO.class);
    }

    @Override
    public boolean saveB2bAppVajraPosition(SaveB2bAppVajraPositionRequest request) {
        return service.saveB2bAppVajraPosition(request);
    }

    @Override
    public boolean editB2bAppVajraPositionWeight(B2bAppVajraPositionWeightRequest request) {
        return service.editWeight(request.getId(), request.getSort(), request.getOpUserId());
    }

    @Override
    public boolean editB2bAppVajraPositionStatus(B2bAppVajraPositionStatusRequest request) {
        return service.editStatus(request.getId(), request.getVajraStatus(), request.getOpUserId());
    }

    @Override
    public boolean deleteB2bAppVajraPosition(B2bAppVajraPositionDeleteRequest request) {
        return service.deleteById(request.getId(), request.getOpUserId());
    }

    @Override
    public Page<B2bAppVajraPositionDTO> pageList(B2bAppVajraPositionPageRequest request) {
        return service.pageList(request);
    }

    @Override
    public List<B2bAppVajraPositionDTO> listAll(int source) {
        return service.listByStatus(1, source);
    }
}
