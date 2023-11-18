package com.yiling.dataflow.sjms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.sjms.dao.SjmsRoleDatascopeMapper;
import com.yiling.dataflow.sjms.entity.SjmsRoleDatascopeDO;
import com.yiling.dataflow.sjms.service.SjmsRoleDatascopeService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 数据洞察系统角色数据权限表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-03-27
 */
@Service
public class SjmsRoleDatascopeServiceImpl extends BaseServiceImpl<SjmsRoleDatascopeMapper, SjmsRoleDatascopeDO> implements SjmsRoleDatascopeService {

    @Override
    public List<SjmsRoleDatascopeDO> listByRoleIds(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return ListUtil.empty();
        }

        QueryWrapper<SjmsRoleDatascopeDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SjmsRoleDatascopeDO::getRoleId, roleIds);
        return this.list(queryWrapper);
    }
}
