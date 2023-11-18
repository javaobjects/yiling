package com.yiling.mall.banner.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.dao.B2bAppHotWordsMapper;
import com.yiling.mall.banner.dto.B2bAppHotWordsDTO;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppHotWordsSaveRequest;
import com.yiling.mall.banner.entity.B2bAppHotWordsDO;
import com.yiling.mall.banner.service.B2bAppHotWordsService;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 热词管理表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
@Service
public class B2bAppHotWordsServiceImpl extends BaseServiceImpl<B2bAppHotWordsMapper, B2bAppHotWordsDO> implements B2bAppHotWordsService {

    @Override
    public boolean saveB2bAppHotWords(B2bAppHotWordsSaveRequest request) {
        B2bAppHotWordsDO b2bAppHotWordsDO = PojoUtils.map(request, B2bAppHotWordsDO.class);
        if (null == request.getId()) {
            b2bAppHotWordsDO.setCreateUser(request.getOpUserId());
            b2bAppHotWordsDO.setCreateTime(new Date());
            b2bAppHotWordsDO.setUpdateUser(request.getOpUserId());
            b2bAppHotWordsDO.setUpdateTime(new Date());
            return this.save(b2bAppHotWordsDO);
        } else {
            b2bAppHotWordsDO.setUpdateUser(request.getOpUserId());
            b2bAppHotWordsDO.setUpdateTime(new Date());
            return this.updateById(b2bAppHotWordsDO);
        }
    }

    @Override
    public boolean editWeight(Long id, Integer sort, Long currentUserId) {
        B2bAppHotWordsDO b2bAppHotWordsDO = new B2bAppHotWordsDO();
        b2bAppHotWordsDO.setId(id);
        b2bAppHotWordsDO.setSort(sort);
        b2bAppHotWordsDO.setUpdateUser(currentUserId);
        b2bAppHotWordsDO.setUpdateTime(new Date());
        return this.updateById(b2bAppHotWordsDO);
    }

    @Override
    public boolean editStatus(Long id, Integer useStatus, Long currentUserId) {
        B2bAppHotWordsDO b2bAppHotWordsDO = new B2bAppHotWordsDO();
        b2bAppHotWordsDO.setId(id);
        b2bAppHotWordsDO.setUseStatus(useStatus);
        b2bAppHotWordsDO.setUpdateUser(currentUserId);
        b2bAppHotWordsDO.setUpdateTime(new Date());
        return this.updateById(b2bAppHotWordsDO);
    }

    @Override
    public Page<B2bAppHotWordsDTO> pageList(B2bAppHotWordsPageRequest request) {
        QueryWrapper<B2bAppHotWordsDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(request.getContent())) {
            wrapper.lambda().like(B2bAppHotWordsDO::getContent, request.getContent());
        }
        if (null != request.getUseStatus() && 0 != request.getUseStatus()) {
            wrapper.lambda().eq(B2bAppHotWordsDO::getUseStatus, request.getUseStatus());
        }
        if (null != request.getCreateStartTime() && null != request.getCreateEndTime()) {
            wrapper.lambda().ge(B2bAppHotWordsDO::getCreateTime, DateUtil.beginOfDay(request.getCreateStartTime()))
                    .le(B2bAppHotWordsDO::getCreateTime, DateUtil.endOfDay(request.getCreateEndTime()));
        }
        // 投放开始时间
        if (null != request.getUseStartTime() && null != request.getUseEndTime()) {
            wrapper.lambda().ge(B2bAppHotWordsDO::getStartTime, DateUtil.beginOfDay(request.getUseStartTime()))
                    .lt(B2bAppHotWordsDO::getStopTime, DateUtil.endOfDay(request.getUseEndTime()));
        }
        wrapper.lambda().orderByDesc(B2bAppHotWordsDO::getSort)
                .orderByAsc(B2bAppHotWordsDO::getStopTime)
                .orderByAsc(B2bAppHotWordsDO::getStartTime)
                .orderByAsc(B2bAppHotWordsDO::getCreateTime);
        Page<B2bAppHotWordsDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, B2bAppHotWordsDTO.class);
    }

    @Override
    public List<B2bAppHotWordsDTO> listByStatus(Integer useStatus, int source) {
        QueryWrapper<B2bAppHotWordsDO> wrapper = new QueryWrapper<>();
        if (null != useStatus && 0 != useStatus) {
            wrapper.lambda().eq(B2bAppHotWordsDO::getUseStatus, useStatus);
        }
        wrapper.lambda().le(B2bAppHotWordsDO::getStartTime, new Date())
                .ge(B2bAppHotWordsDO::getStopTime, new Date());
        wrapper.lambda().eq(B2bAppHotWordsDO::getHotWordsSource, source)
                .orderByDesc(B2bAppHotWordsDO::getSort)
                .orderByAsc(B2bAppHotWordsDO::getStopTime)
                .orderByAsc(B2bAppHotWordsDO::getStartTime)
                .orderByAsc(B2bAppHotWordsDO::getCreateTime)
                .last("limit 20");
        List<B2bAppHotWordsDO> list = this.list(wrapper);
        return PojoUtils.map(list, B2bAppHotWordsDTO.class);
    }
}
