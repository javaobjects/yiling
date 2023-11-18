package com.yiling.dataflow.sjms.bo;

import java.util.List;

import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;

import lombok.Data;

/**
 * 数据洞察系统用户数据权限BO
 *
 * @author: xuan.zhou
 * @date: 2023/4/10
 */
@Data
public class SjmsUserDatascopeBO implements java.io.Serializable {

    private static final long serialVersionUID = -2356323600345932912L;

    /**
     * 员工工号
     */
    private String empId;

    /**
     * 机构权限范围：1-无权限 2-部分权限 3-全部权限
     */
    private Integer orgDatascope;

    /**
     * 部分机构权限BO
     */
    private OrgPartDatascopeBO orgPartDatascopeBO;

    /**
     * 无权限
     *
     * @param empId 员工工号
     * @return com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO
     * @author xuan.zhou
     * @date 2023/4/10
     **/
    public static SjmsUserDatascopeBO noPermission(String empId) {
        SjmsUserDatascopeBO sjmsUserDatascopeBO = new SjmsUserDatascopeBO();
        sjmsUserDatascopeBO.setEmpId(empId);
        sjmsUserDatascopeBO.setOrgDatascope(OrgDatascopeEnum.NONE.getCode());
        return sjmsUserDatascopeBO;
    }

    /**
     * 全部权限
     *
     * @param empId 员工工号
     * @return com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO
     * @author xuan.zhou
     * @date 2023/4/10
     **/
    public static SjmsUserDatascopeBO allPermission(String empId) {
        SjmsUserDatascopeBO sjmsUserDatascopeBO = new SjmsUserDatascopeBO();
        sjmsUserDatascopeBO.setEmpId(empId);
        sjmsUserDatascopeBO.setOrgDatascope(OrgDatascopeEnum.ALL.getCode());
        return sjmsUserDatascopeBO;
    }

    @Data
    public static class OrgPartDatascopeBO implements java.io.Serializable {

        private static final long serialVersionUID = 3113498428719187438L;

        /**
         * 省份编码列表
         */
        private List<String> provinceCodes;

        /**
         * 省份名称列表
         */
        private List<String> provinceNames;

        /**
         * CRM企业ID列表
         */
        private List<Long> crmEids;
    }
}
