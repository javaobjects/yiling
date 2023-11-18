package com.yiling.dataflow.sjms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.sjms.dao.SjmsRoleDatascopeProvinceMapper;
import com.yiling.dataflow.sjms.entity.SjmsRoleDatascopeProvinceDO;
import com.yiling.dataflow.sjms.service.SjmsRoleDatascopeProvinceService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 数据洞察系统角色数据权限明细 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-03-27
 */
@Service
public class SjmsRoleDatascopeProvinceServiceImpl extends BaseServiceImpl<SjmsRoleDatascopeProvinceMapper, SjmsRoleDatascopeProvinceDO> implements SjmsRoleDatascopeProvinceService {

    @Override
    public List<String> listProvinceNamesByRoleIdEmpId(Long roleId, String empId) {
        QueryWrapper<SjmsRoleDatascopeProvinceDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SjmsRoleDatascopeProvinceDO::getRoleId, roleId)
                .eq(SjmsRoleDatascopeProvinceDO::getEmpId, empId);

        List<SjmsRoleDatascopeProvinceDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return list.stream().map(SjmsRoleDatascopeProvinceDO::getProvinceName).distinct().collect(Collectors.toList());
    }
}
