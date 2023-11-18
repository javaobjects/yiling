package com.yiling.user.esb.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.dao.EsbJobMapper;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbJobRequest;
import com.yiling.user.esb.entity.EsbJobDO;
import com.yiling.user.esb.service.EsbJobService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * esb岗位 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-25
 */
@Service
public class EsbJobServiceImpl extends BaseServiceImpl<EsbJobMapper, EsbJobDO> implements EsbJobService {

    @Override
    public EsbJobDO getByJobDetpId(Long jobDeptId) {
        QueryWrapper<EsbJobDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EsbJobDO::getJobDeptId, jobDeptId)
                .eq(EsbJobDO::getState, JOB_STATE_NORMAL)
                .last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean saveOrUpdate(SaveOrUpdateEsbJobRequest request) {
        Assert.notNull(request, "参数request不能为空");

        Long jobDetpId = request.getJobDeptId();
        EsbJobDO entity = this.getByJobDetpId(jobDetpId);
        if (entity == null) {
            entity = PojoUtils.map(request, EsbJobDO.class);
            return this.save(entity);
        } else {
            PojoUtils.map(request, entity);
            return this.updateById(entity);
        }
    }

    @Override
    public Map<Long, List<Long>> getJobByDeptListId(List<Long> deptIdList) {
        if (CollUtil.isEmpty(deptIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<EsbJobDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EsbJobDO::getDeptId, deptIdList);
        wrapper.eq(EsbJobDO::getState, JOB_STATE_NORMAL);
        List<EsbJobDO> esbJobDOList = this.list(wrapper);
        return esbJobDOList.stream().collect(Collectors.groupingBy(EsbJobDO::getDeptId, Collectors.mapping(EsbJobDO::getJobDeptId, Collectors.toList())));
    }

    @Override
    public Map<Long, String> getJobDeptNameByJobDeptIdList(List<Long> jobDeptIdList) {
        if (CollUtil.isEmpty(jobDeptIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<EsbJobDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EsbJobDO::getState, JOB_STATE_NORMAL);
        wrapper.in(EsbJobDO::getJobDeptId, jobDeptIdList);
        List<EsbJobDO> esbJobDOList = this.list(wrapper);
        return esbJobDOList.stream().collect(Collectors.toMap(EsbJobDO::getJobDeptId, EsbJobDO::getJobDeptName, (k1, k2) -> k1));
    }

}
