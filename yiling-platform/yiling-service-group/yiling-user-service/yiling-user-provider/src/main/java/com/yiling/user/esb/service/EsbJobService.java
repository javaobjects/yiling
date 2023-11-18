package com.yiling.user.esb.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbJobRequest;
import com.yiling.user.esb.entity.EsbJobDO;

/**
 * <p>
 * esb岗位 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-25
 */
public interface EsbJobService extends BaseService<EsbJobDO> {

    public static final String JOB_STATE_NORMAL = "0";

    /**
     * 根据部门ID获取岗位信息
     *
     * @param jobDeptId 部门岗ID
     * @return com.yiling.user.esb.entity.EsbJobDO
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    EsbJobDO getByJobDetpId(Long jobDeptId);

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
     * @param jobDeptIdList
     * @return
     */
    Map<Long, String> getJobDeptNameByJobDeptIdList(List<Long> jobDeptIdList);
}
