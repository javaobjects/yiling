package com.yiling.open.user.esb.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 同步ESB岗位信息 Form
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@Data
public class SyncEsbJobForm {

    /**
     * 部门岗ID
     */
    @NotNull
    @JsonProperty("jobdeptid")
    private Long jobDeptId;

    /**
     * 部门刚名称
     */
    @NotEmpty
    @JsonProperty("postname")
    private String jobDeptName;

    /**
     * 标准岗ID
     */
    @JsonProperty("jobid")
    private Long jobId;

    /**
     * 标准岗名称
     */
    @JsonProperty("jobname")
    private String jobName;

    /**
     * 部门ID
     */
    @JsonProperty("deptid")
    private Long deptId;

    /**
     * 部门形成
     */
    @JsonProperty("deptname")
    private String deptName;

    /**
     * HRID
     */
    @JsonProperty("sourceDataid")
    private String sourceDetailId;

    /**
     * 编制数
     */
    @JsonProperty("postPrepare")
    private String postPrepare;

    /**
     * 是否失效：0-正常，其他失效
     */
    @NotEmpty
    @JsonProperty("state")
    private String state;
}
