package com.yiling.dataflow.sjms.api;

import java.util.List;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;

/**
 * 数据洞察系统数据权限 API
 *
 * @author: xuan.zhou
 * @date: 2023/3/28
 */
public interface SjmsUserDatascopeApi {

    /**
     * 【已废弃】获取数据权限中用户对应的企业ID列表 <br/>
     * 该方法已被 getByEmpId 方法所替代。<br/>
     *
     * @param empId 员工工号
     * @return java.util.List<java.lang.Long> crm_enterprise表ID列表。1、如果返回null，则表示无权限。2、如果返回为空，则表示所有数据。3、返回有数据。
     * @author xuan.zhou
     * @date 2023-3-27
     **/
    @Deprecated
    List<Long> listAuthorizedEids(String empId);

    /**
     * 获取员工对应的数据权限信息
     *
     * @param empId 员工ID
     * @return com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO
     * @author xuan.zhou
     * @date 2023/4/10
     **/
    SjmsUserDatascopeBO getByEmpId(String empId);
}
