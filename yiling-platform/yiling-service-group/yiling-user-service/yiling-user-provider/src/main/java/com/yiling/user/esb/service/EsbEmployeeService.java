package com.yiling.user.esb.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryEsbEmployeePageListRequest;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbEmployeeRequest;
import com.yiling.user.esb.entity.EsbEmployeeDO;

/**
 * <p>
 * esb人员 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-24
 */
public interface EsbEmployeeService extends BaseService<EsbEmployeeDO> {

    public static final String EMP_STATE_NORMAL = "0";

    /**
     * 查询员工分页列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.user.esb.entity.EsbEmployeeDO>
     * @author xuan.zhou
     * @date 2023/2/17
     **/
    Page<EsbEmployeeDO> pageList(QueryEsbEmployeePageListRequest request);

    /**
     * 根据人员工号获取人员信息
     *
     * @param empId 人员工号
     * @return com.yiling.user.esb.entity.EsbEmployeeDO
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    EsbEmployeeDO getByEmpId(String empId);

    /**
     * 根据人员工号批量获取人员信息
     *
     * @param empIds 人员工号列表
     * @return java.util.List<com.yiling.user.esb.entity.EsbEmployeeDO>
     * @author xuan.zhou
     * @date 2022/12/9
     **/
    List<EsbEmployeeDO> listByEmpIds(List<String> empIds);

    /**
     * 获取部门下的员工信息列表
     *
     * @param deptId 部门ID
     * @return java.util.List<com.yiling.user.esb.entity.EsbEmployeeDO>
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    List<EsbEmployeeDO> listByDeptId(Long deptId);

    /**
     * 根据部门ID和岗位名称获取人员信息
     *
     * @param deptId 所属部门ID
     * @param jobName 所属部门岗位名称
     * @return com.yiling.user.esb.bo.EsbEmployeeBO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    EsbEmployeeDO getByDeptIdAndJobName(Long deptId, String jobName);

    /**
     * 保存或更新人员信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    Boolean saveOrUpdate(SaveOrUpdateEsbEmployeeRequest request);

    /**
     * 判断人员工号对应的人员是否为省区经理
     *
     * @param empId 人员ID
     * @return boolean
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    boolean isProvinceManager(String empId);

    /**
     * 根据人员工号获取人员信息
     *
     * @param empId       人员工号
     * @param tableSuffix
     * @return com.yiling.user.esb.entity.EsbEmployeeDO
     * @author shiixng.sun
     * @date 2023/2/16
     **/
    EsbEmployeeDO getByEmpIdOrJobId(String empId, String jobId, String tableSuffix);
    /**
     * 根据岗位ID查询人员信息
     * @return com.yiling.user.esb.entity.EsbEmployeeDO
     * @author shiixng.sun
     * @date 2023/2/16
     **/
    List<EsbEmployeeDO> listByJobIds(List<Long> jobIds);

    /**
     * 根据岗位id集合获取人员信息
     *
     * @param postIds
     * @return
     */

    /**
     * 根据人员工号获取人员信息
     *
     * @param postIds 岗位id
     * @author shiixng.sun
     * @date 2023/2/16
     **/
    List<EsbEmployeeDO> getEmpInfoByJobIds(List<Long> postIds);

    /**
     * 根据人员姓名模糊搜索获取人员信息
     *
     * @param name 人员姓名
     * @author shiixng.sun
     * @date 2023/2/16
     **/
    List<EsbEmployeeDO>  getEmpInfoByName(String name);

    /**
     * 根据人员姓名模糊搜索获取人员信息
     *
     * @param jobIds 人员姓名
     * @author shiixng.sun
     * @date 2023/2/16
     **/
    List<EsbEmployeeDO>  listByJobIdsForAgency(List<Long> jobIds);

    /**
     * 根据人员姓名模糊搜索获取人员信息
     *
     * @param longs 人员姓名
     * @author shiixng.sun
     * @date 2023/2/16
     **/
    Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> longs, List<Long> orgIds,String tableSuffix);

    /**
     * 根据岗位ID查询人员信息(备份呢表信息)
     * @return com.yiling.user.esb.entity.EsbEmployeeDO
     * @author shiixng.sun
     * @date 2023/5/10
     **/
    List<EsbEmployeeDO> listSuffixByJobIds(List<Long> jobIds,String tableSuffix);

    /**
     * 通过业务部门+业务省区获取省区经理人员信息
     * 匹配规则：
     *   1.限制员工在职状态，正式、试用
     *   2.先取省区经理 job_names = '省区经理' ，查到就直接返回
     *   3.如果查不到省区经理，则再取省区主管 job_names = '省区主管'
     *   4.都取不到则返回空list
     *
     * @param yxDept 业务部门
     * @param yxProvince 业务省区
     * @param yxArea 业务区域
     * @param jobNames 标准岗位名称
     * @return
     */
    List<EsbEmployeeDO> getProvincialManagerByYxDeptAndYxProvinceAndJobNames(String yxDept, String yxProvince, String yxArea, String jobNames);

    List<EsbEmployeeDO> getEsbEmployeeDTOList();
}
