package com.yiling.open.user.esb.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 同步ESB组织架构 Form
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@Data
public class SyncEsbOrganizationForm {

    /**
     * 部门ID
     */
    @NotNull
    @JsonProperty("orgid")
    private Long orgId;

    /**
     * 部门名称
     */
    @NotEmpty
    @JsonProperty("orgname")
    private String orgName;

    /**
     * 组织类型：0-公司 1-部门
     */
    @JsonProperty("orgtype")
    private String orgType;

    /**
     * 上级部门ID
     */
    @JsonProperty("orgpid")
    private Long orgPid;

    /**
     * 所属公司
     */
    @JsonProperty("compid")
    private Long compId;

    /**
     * 所在地（全部是石家庄）
     */
    @JsonProperty("orgarea")
    private String orgArea;

    /**
     * HRID
     */
    @JsonProperty("sourceDataid")
    private String sourceDetailId;

    /**
     * 是否失效：0-正常，其他失效
     */
    @NotEmpty
    @JsonProperty("state")
    private String state;

    /**
     * 全路径
     */
    @JsonProperty("fullpath")
    private String fullpath;

    /**
     * 组织状态：-1-停用 0-正常 1-保存未生效 2-删除
     */
    @JsonProperty("status")
    private String status;

    /**
     * 一级部门ID
     */
    @JsonProperty("yxplate")
    private String plate;

    /**
     * 二级部门ID
     */
    @JsonProperty("yxtwodeptid")
    private Long twoDeptId;

    /**
     * 二级部门名称
     */
    @JsonProperty("yxtwodept")
    private String twoDeptName;

    /**
     * 三级部门ID
     */
    @JsonProperty("yxdeptid")
    private Long deptId;

    /**
     * 三级部门名称
     */
    @JsonProperty("yxdept")
    private String deptName;

    /**
     * 一级省区ID
     */
    @JsonProperty("yxprovinceid")
    private String provinceId;

    /**
     * 一级省区名称
     */
    @JsonProperty("yxprovince")
    private String provinceName;

    /**
     * 二级省区ID
     */
    @JsonProperty("yxbusinessprovinceid")
    private String bizProvinceId;

    /**
     * 二级省区名称
     */
    @JsonProperty("yxbusinessprovince")
    private String bizProvinceName;

    /**
     * 四级省区ID
     */
    @JsonProperty("yxbusinessdeptid")
    private Long bizDeptId;

    /**
     * 四级省区名称
     */
    @JsonProperty("yxbusinessdept")
    private String bizDeptName;

    /**
     * 财务负责人
     */
    @JsonProperty("manager")
    private String financeManager;

    /**
     * 片区
     */
    @JsonProperty("area")
    private String area;

}
