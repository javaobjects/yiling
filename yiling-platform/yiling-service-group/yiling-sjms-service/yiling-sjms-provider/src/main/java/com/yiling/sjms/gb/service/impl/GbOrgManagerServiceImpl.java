package com.yiling.sjms.gb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.dao.GbOrgManagerMapper;
import com.yiling.sjms.gb.entity.GbOrgManagerDO;
import com.yiling.sjms.gb.service.GbOrgManagerService;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 业务部负责人关系 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-28
 */
@Slf4j
@Service
public class GbOrgManagerServiceImpl extends BaseServiceImpl<GbOrgManagerMapper, GbOrgManagerDO> implements GbOrgManagerService {

    @Override
    public SimpleEsbEmployeeInfoBO getByOrgId(Long orgId) {
        List<SimpleEsbEmployeeInfoBO> list = this.listByOrgIds(ListUtil.toList(orgId));
        if (CollUtil.isEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public List<SimpleEsbEmployeeInfoBO> listByOrgIds(List<Long> orgIds) {
        QueryWrapper<GbOrgManagerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(GbOrgManagerDO::getOrgId, orgIds);
        List<GbOrgManagerDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }

        return PojoUtils.map(list, SimpleEsbEmployeeInfoBO.class);
    }

    @Override
    public List<Long> listOrgIds() {
        List<GbOrgManagerDO> list = this.list();
        return list.stream().map(GbOrgManagerDO::getOrgId).distinct().collect(Collectors.toList());
    }
}
