package com.yiling.dataflow.crm.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmManorRepresentativeDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmManorRepresentativePageRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationManorDO;
import com.yiling.dataflow.crm.entity.CrmManorRepresentativeDO;
import com.yiling.dataflow.crm.dao.CrmManorRepresentativeMapper;
import com.yiling.dataflow.crm.service.CrmManorRepresentativeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 辖区代表 服务实现类
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-12
 */
@Service
public class CrmManorRepresentativeServiceImpl extends BaseServiceImpl<CrmManorRepresentativeMapper, CrmManorRepresentativeDO> implements CrmManorRepresentativeService {

    @Override
    public Page<CrmManorRepresentativeDTO> pageList(QueryCrmManorRepresentativePageRequest request) {
        return baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public CrmManorRepresentativeDTO getByManorId(Long manorId) {
        QueryWrapper<CrmManorRepresentativeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(CrmManorRepresentativeDO::getId,CrmManorRepresentativeDO::getManorId, CrmManorRepresentativeDO::getRepresentativePostCode);
        queryWrapper.lambda().eq(CrmManorRepresentativeDO::getManorId,manorId);
        return PojoUtils.map(getOne(queryWrapper),CrmManorRepresentativeDTO.class);
    }

    @Override
    public void batchDeleteWithManorId(Long manorId,Long opUserId,String message) {
        QueryWrapper<CrmManorRepresentativeDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmManorRepresentativeDO::getManorId, manorId);
        CrmManorRepresentativeDO deleteDO = new CrmManorRepresentativeDO();
        deleteDO.setRemark(message);
        deleteDO.setOpUserId(opUserId);
         baseMapper.batchDeleteWithFill(deleteDO, queryWrapper);
    }
}
