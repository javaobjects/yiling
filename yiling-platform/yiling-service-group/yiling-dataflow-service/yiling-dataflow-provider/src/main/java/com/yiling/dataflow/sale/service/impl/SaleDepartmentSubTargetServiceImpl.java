package com.yiling.dataflow.sale.service.impl;

import java.util.List;

import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentSubTargetRequest;
import com.yiling.dataflow.sale.dto.request.SaveBathSaleDepartmentSubTargetRequest;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.dataflow.sale.dao.SaleDepartmentSubTargetMapper;
import com.yiling.dataflow.sale.dto.SaleDepartmentSubTargetDTO;
import com.yiling.dataflow.sale.entity.SaleDepartmentSubTargetDO;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 部门销售指标子项配置 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Service
public class SaleDepartmentSubTargetServiceImpl extends BaseServiceImpl<SaleDepartmentSubTargetMapper, SaleDepartmentSubTargetDO> implements SaleDepartmentSubTargetService {

    @Override
    public List<SaleDepartmentSubTargetDTO> queryListByTargetIdAndDeptId(Long targetId,Long deptId) {
        if (ObjectUtil.isNull(targetId) || ObjectUtil.equal(targetId, 0L)) {
            return ListUtil.toList();
        }
        if (ObjectUtil.isNull(deptId) || ObjectUtil.equal(deptId, 0L)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<SaleDepartmentSubTargetDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SaleDepartmentSubTargetDO::getSaleTargetId, targetId);
        wrapper.eq(SaleDepartmentSubTargetDO::getDepartId, deptId);
        List<SaleDepartmentSubTargetDO> list = list(wrapper);
        return PojoUtils.map(list, SaleDepartmentSubTargetDTO.class);
    }

    @Override
    public List<SaleDepartmentSubTargetDTO> listByParam(QuerySaleDepartmentSubTargetRequest request) {
        LambdaQueryWrapper<SaleDepartmentSubTargetDO> wrapper = Wrappers.lambdaQuery();
        if(ObjectUtil.isNotNull(request.getSaleTargetId())){
            wrapper.eq(SaleDepartmentSubTargetDO::getSaleTargetId, request.getSaleTargetId());
        }
        if(ObjectUtil.isNotNull(request.getDepartId())){
            wrapper.eq(SaleDepartmentSubTargetDO::getDepartId, request.getDepartId());
        }
        if(ObjectUtil.isNotNull(request.getType())){
            wrapper.eq(SaleDepartmentSubTargetDO::getType, request.getType());
        }
        List<SaleDepartmentSubTargetDO> list = list(wrapper);
        return PojoUtils.map(list, SaleDepartmentSubTargetDTO.class);
    }

    @Override
    public int removeBySaleTargetAndDepartId(SaveBathSaleDepartmentSubTargetRequest request) {
        LambdaQueryWrapper<SaleDepartmentSubTargetDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SaleDepartmentSubTargetDO::getSaleTargetId,request.getSaleTargetId());
        wrapper.eq(SaleDepartmentSubTargetDO::getDepartId,request.getDepartId());
        SaleDepartmentSubTargetDO saleDepartmentSubTargetDO = new SaleDepartmentSubTargetDO();
        saleDepartmentSubTargetDO.setOpUserId(request.getOpUserId());
        saleDepartmentSubTargetDO.setOpTime(request.getOpTime());
        return  batchDeleteWithFill(saleDepartmentSubTargetDO, wrapper);
    }
}
