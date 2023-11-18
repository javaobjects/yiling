package com.yiling.mall.banner.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.dao.B2bAppVajraPositionMapper;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.mall.banner.dto.request.B2bAppVajraPositionPageRequest;
import com.yiling.mall.banner.dto.request.SaveB2bAppVajraPositionRequest;
import com.yiling.mall.banner.entity.B2bAppVajraPositionDO;
import com.yiling.mall.banner.service.B2bAppVajraPositionService;

/**
 * <p>
 * 金刚位表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
@Service
public class B2bAppVajraPositionServiceImpl extends BaseServiceImpl<B2bAppVajraPositionMapper, B2bAppVajraPositionDO> implements B2bAppVajraPositionService {

    @Override
    public boolean saveB2bAppVajraPosition(SaveB2bAppVajraPositionRequest request) {
        B2bAppVajraPositionDO b2bAppVajraPositionDO = PojoUtils.map(request, B2bAppVajraPositionDO.class);
        if (null == request.getId()) {
            b2bAppVajraPositionDO.setCreateTime(new Date());
            b2bAppVajraPositionDO.setCreateUser(request.getOpUserId());
            return this.save(b2bAppVajraPositionDO);
        } else {
            b2bAppVajraPositionDO.setUpdateTime(new Date());
            b2bAppVajraPositionDO.setUpdateUser(request.getOpUserId());
            return this.updateById(b2bAppVajraPositionDO);
        }
    }

    @Override
    public boolean editWeight(Long id, Integer sort, Long currentUserId) {
        B2bAppVajraPositionDO b2bAppVajraPositionDO = new B2bAppVajraPositionDO();
        b2bAppVajraPositionDO.setId(id);
        b2bAppVajraPositionDO.setSort(sort);
        b2bAppVajraPositionDO.setUpdateUser(currentUserId);
        b2bAppVajraPositionDO.setUpdateTime(new Date());
        return this.updateById(b2bAppVajraPositionDO);
    }

    @Override
    public boolean editStatus(Long id, Integer vajraStatus, Long currentUserId) {
        B2bAppVajraPositionDO b2bAppVajraPositionDO = new B2bAppVajraPositionDO();
        b2bAppVajraPositionDO.setId(id);
        b2bAppVajraPositionDO.setVajraStatus(vajraStatus);
        b2bAppVajraPositionDO.setUpdateUser(currentUserId);
        b2bAppVajraPositionDO.setUpdateTime(new Date());
        return this.updateById(b2bAppVajraPositionDO);
    }

    @Override
    public boolean deleteById(Long id, Long currentUserId) {
        B2bAppVajraPositionDO b2bAppVajraPositionDO = new B2bAppVajraPositionDO();
        b2bAppVajraPositionDO.setId(id);
        b2bAppVajraPositionDO.setUpdateUser(currentUserId);
        b2bAppVajraPositionDO.setUpdateTime(new Date());
        return this.deleteByIdWithFill(b2bAppVajraPositionDO) > 0;
    }

    @Override
    public Page<B2bAppVajraPositionDTO> pageList(B2bAppVajraPositionPageRequest request) {
        QueryWrapper<B2bAppVajraPositionDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(request.getTitle())) {
            wrapper.lambda().like(B2bAppVajraPositionDO::getTitle, request.getTitle());
        }
        if (null != request.getVajraStatus() && 0 != request.getVajraStatus()) {
            wrapper.lambda().eq(B2bAppVajraPositionDO::getVajraStatus, request.getVajraStatus());
        }
        if (null != request.getSource()) {
            wrapper.lambda().eq(B2bAppVajraPositionDO::getSource, request.getSource());
        }
        wrapper.lambda()
                .orderByAsc(B2bAppVajraPositionDO::getVajraStatus)
                .orderByDesc(B2bAppVajraPositionDO::getSort, B2bAppVajraPositionDO::getCreateTime);
        Page<B2bAppVajraPositionDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, B2bAppVajraPositionDTO.class);
    }

    @Override
    public List<B2bAppVajraPositionDTO> listByStatus(Integer vajraStatus, int source) {
        QueryWrapper<B2bAppVajraPositionDO> wrapper = new QueryWrapper<>();
        if (null != vajraStatus && 0 != vajraStatus) {
            wrapper.lambda().eq(B2bAppVajraPositionDO::getVajraStatus, vajraStatus);
        }
        wrapper.lambda().eq(B2bAppVajraPositionDO::getSource, source)
                .orderByDesc(B2bAppVajraPositionDO::getSort, B2bAppVajraPositionDO::getCreateTime);
        List<B2bAppVajraPositionDO> list = this.list(wrapper);
        return PojoUtils.map(list, B2bAppVajraPositionDTO.class);
    }
}
