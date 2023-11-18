package com.yiling.user.esb.api;

import java.util.List;
import java.util.Map;

import com.yiling.user.esb.dto.request.SaveOrUpdateEsbJobRequest;

/**
 * ESB岗位 API
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
public interface EsbJobApi {

    /**
     * 保存或更新岗位信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    Boolean saveOrUpdate(SaveOrUpdateEsbJobRequest request);

    /**
     * 根据部门ID批量获取部门岗位ID
     *
     * @param deptIdList 部门ID集合
     * @return key为部门ID，value为部门对应的岗位ID集合
     */
    Map<Long, List<Long>> getJobByDeptListId(List<Long> deptIdList);

    /**
     * 根据部门岗位ID批量获取部门岗位名称
     *
     * @param jobDeptIdList 部门岗位ID集合
     * @return key为部门岗位ID，value为部门岗位名称
     */
    Map<Long, String> getJobDeptNameByJobDeptIdList(List<Long> jobDeptIdList);

}
