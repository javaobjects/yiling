package com.yiling.user.esb.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.esb.api.EsbJobApi;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbJobRequest;
import com.yiling.user.esb.service.EsbJobService;

/**
 * ESB岗位 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@DubboService
public class EsbJobApiImpl implements EsbJobApi {

    @Autowired
    EsbJobService esbJobService;

    @Override
    public Boolean saveOrUpdate(SaveOrUpdateEsbJobRequest request) {
        return esbJobService.saveOrUpdate(request);
    }

    @Override
    public Map<Long, List<Long>> getJobByDeptListId(List<Long> deptIdList) {
        return esbJobService.getJobByDeptListId(deptIdList);
    }

    @Override
    public Map<Long, String> getJobDeptNameByJobDeptIdList(List<Long> jobDeptIdList) {
        return esbJobService.getJobDeptNameByJobDeptIdList(jobDeptIdList);
    }
}
