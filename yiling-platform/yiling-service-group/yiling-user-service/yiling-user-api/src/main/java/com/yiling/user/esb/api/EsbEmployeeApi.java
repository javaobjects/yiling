package com.yiling.user.esb.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.QueryEsbEmployeePageListRequest;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbEmployeeRequest;

/**
 * ESB人员 API
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
public interface EsbEmployeeApi {

    /**
     * 查询员工分页列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.user.esb.entity.EsbEmployeeDO>
     * @author xuan.zhou
     * @date 2023/2/17
     **/
    Page<EsbEmployeeDTO> pageList(QueryEsbEmployeePageListRequest request);

    /**
     * 根据人员工号获取人员信息
     *
     * @param empId esb人员工号
     * @return
     */
    EsbEmployeeDTO getByEmpId(String empId);

    /**
     * 根据人员工号批量获取人员信息
     *
     * @param empIds 人员工号列表
     * @return java.util.List<com.yiling.user.esb.entity.EsbEmployeeDO>
     * @author xuan.zhou
     * @date 2022/12/9
     **/
    List<EsbEmployeeDTO> listByEmpIds(List<String> empIds);

    /**
     * 获取部门下的员工信息列表
     *
     * @param deptId 部门ID
     * @return java.util.List<com.yiling.user.esb.entity.EsbEmployeeDO>
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    List<EsbEmployeeDTO> listByDeptId(Long deptId);

    /**
     * 根据部门ID和岗位名称获取人员信息
     *
     * @param deptId 所属部门ID
     * @param jobName 所属部门岗位名称
     * @return com.yiling.user.esb.bo.EsbEmployeeBO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    EsbEmployeeDTO getByDeptIdAndJobName(Long deptId, String jobName);

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
     * 通过岗位id或者工号id获取业务省区，业务部门，上级主管工号，名称等字段
     * 当一个岗位id下面包含（离职，退休，试用，正式）的时候，优先取正式和试用的。
     * 当多个正式的时候取第一个，当多个离职活退休的时候，取第一个同时工号和名称赋值null，（empId和empName赋值null）
     * @param empId esb人员工号 JobId 岗位id tableSuffix 备份表后缀
     * @return hr系统人员信息
     */
    EsbEmployeeDTO getByEmpIdOrJobId(String empId,String JobId,String tableSuffix);

    /**
     *
     */
    List<EsbEmployeeDTO> listByJobIds(List<Long> jobIds);


    /**
     * 根据名称模糊搜索集合获取人员信息
     *
     * @param name
     * @return
     */
    List<EsbEmployeeDTO> getEmpInfoByName(String name);

    /**
     * 通过岗位id获取业务省区，业务部门，上级主管工号，名称等字段
     * 当一个岗位id下面包含（离职，退休，试用，正式）的时候，优先取正式和试用的。
     * 当多个正式的时候取第一个，当多个离职活退休的时候，取第一个同时工号和名称赋值null，（empId和empName赋值null）
     *
     * @param jobIds
     * @return hr系统人员信息
     */
    List<EsbEmployeeDTO> listByJobIdsForAgency(List<Long> jobIds);

    /**
     * 获取事业部信息 查备份表
     *
     * @param longs
     * @return
     */
    Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> longs, List<Long> orgIds, String tableSuffix);

    /**
     * 用岗位id查备份表人员信息
     *
     * @param jobIds
     * @param tableSuffix
     * @return hr系统人员信息
     */
    List<EsbEmployeeDTO> listSuffixByJobIds(List<Long> jobIds, String tableSuffix);

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
     * @param jobNamesList 标准岗位名称列表，index0='省区经理' 、index1='省区主管'
     * @return
     */
    List<EsbEmployeeDTO> getProvincialManagerByYxDeptAndYxProvinceAndJobNames(String yxDept, String yxProvince, String yxArea, List<String> jobNamesList);

    List<EsbEmployeeDTO> getEsbEmployeeDTOList();
}
