package com.yiling.sales.assistant.commissions.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.commissions.dao.CommissionsDetailMapper;
import com.yiling.sales.assistant.commissions.dto.CommissionsDetailDTO;
import com.yiling.sales.assistant.commissions.dto.request.BatchQueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.dto.request.QueryCommissionsDetailPageListRequest;
import com.yiling.sales.assistant.commissions.entity.CommissionsDetailDO;
import com.yiling.sales.assistant.commissions.service.CommissionsDetailService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 销售助手佣金明细表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Service
public class CommissionsDetailServiceImpl extends BaseServiceImpl<CommissionsDetailMapper, CommissionsDetailDO> implements CommissionsDetailService {

    @Override
    public Page<CommissionsDetailDTO> queryCommissionsDetailPageList(QueryCommissionsDetailPageListRequest request) {
        if (ObjectUtil.isNull(request.getCommissionsId())) {
            return new Page<>();
        }
        LambdaQueryWrapper<CommissionsDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDetailDO::getCommissionsId, request.getCommissionsId());
        Page<CommissionsDetailDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, CommissionsDetailDTO.class);
    }

    @Override
    public Page<CommissionsDetailDTO> batchQueryCommissionsDetailPageList(BatchQueryCommissionsDetailPageListRequest request) {
        if (CollUtil.isEmpty(request.getCommissionsIdList())) {
            return new Page<>();
        }
        LambdaQueryWrapper<CommissionsDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CommissionsDetailDO::getCommissionsId, request.getCommissionsIdList());
        if (request.getStatusEnum() != null) {
            wrapper.eq(CommissionsDetailDO::getStatus, request.getStatusEnum().getCode());
        }
        Page<CommissionsDetailDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, CommissionsDetailDTO.class);
    }

    @Override
    public CommissionsDetailDTO queryCommDetail(Long commId, String orderCode) {
        LambdaQueryWrapper<CommissionsDetailDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(CommissionsDetailDO::getCommissionsId, commId);
        wrapper.eq(CommissionsDetailDO::getOrderCode, orderCode);
        CommissionsDetailDO detailDO = getOne(wrapper);
        return PojoUtils.map(detailDO, CommissionsDetailDTO.class);
    }
}
