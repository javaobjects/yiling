package com.yiling.open.user.esb.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 同步ESB人员信息 Form
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@Data
public class SyncEsbEmployeeForm {

    /**
     * UID
     */
    @JsonProperty("empuid")
    private Long empUid;

    /**
     * 姓名
     */
    @NotEmpty
    @JsonProperty("empname")
    private String empName;

    /**
     * 工号
     */
    @NotEmpty
    @JsonProperty("empid")
    private String empId;

    /**
     * 生日
     */
    @JsonProperty("birthday")
    private Date birthday;

    /**
     * 性别
     */
    @JsonProperty("sex")
    private String sex;

    /**
     * 在职状态
     */
    @JsonProperty("status")
    private String status;

    /**
     * 地址
     */
    @JsonProperty("homeaddress")
    private String homeAddress;

    /**
     * 手机号
     */
    @JsonProperty("mobilephone")
    private String mobilePhone;

    /**
     * 电子邮箱
     */
    @JsonProperty("email")
    private String email;

    /**
     * 所属部门ID
     */
    @JsonProperty("deptid")
    private Long deptId;

    /**
     * 所属部门名称
     */
    @JsonProperty("deptname")
    private String deptName;

    /**
     * 所属部门岗位ID
     */
    @JsonProperty("jobid")
    private Long jobId;

    /**
     * 所属部门岗位名称
     */
    @JsonProperty("jobname")
    private String jobName;

    /**
     * 证件类型
     */
    @JsonProperty("certificatetypeid")
    private String certificateTypeId;

    /**
     * 证件号码后八位
     */
    @JsonProperty("certificatenumber")
    private String certificateNumber;

    /**
     * 直接上级的工号
     */
    @JsonProperty("superior")
    private String superior;

    /**
     * 直接上级的姓名
     */
    @JsonProperty("superiorName")
    private String superiorName;

    /**
     * 直接上级部门ID
     */
    @JsonProperty("superiorDept")
    private Long superiorDept;

    /**
     * 直接上级岗位ID
     */
    @JsonProperty("superiorJob")
    private Long superiorJob;

    /**
     * 兼职部门1的部门ID
     */
    @JsonProperty("partDept1")
    private Long partDept1;

    /**
     * 兼职部门2的部门ID
     */
    @JsonProperty("partDept2")
    private Long partDept2;

    /**
     * 兼职部门3的部门ID
     */
    @JsonProperty("partDept3")
    private Long partDept3;

    /**
     * 兼职岗位1的岗位ID
     */
    @JsonProperty("partJob1")
    private Long partJob1;

    /**
     * 兼职岗位2的岗位ID
     */
    @JsonProperty("partJob2")
    private Long partJob2;

    /**
     * 兼职岗位3的岗位ID
     */
    @JsonProperty("partJob3")
    private Long partJob3;

    /**
     * 初始来源系统
     */
    @JsonProperty("sourceSys")
    private String sourceSys;

    /**
     * 来源数据ID
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
     * 营销省区名称
     */
    @JsonProperty("yxProvince")
    private String yxProvince;

    /**
     * 营销部门名称
     */
    @JsonProperty("yxDept")
    private String yxDept;

    /**
     * 营销区办名称
     */
    @JsonProperty("yxArea")
    private String yxArea;

    /**
     * 职级名称
     */
    @JsonProperty("dutygrade")
    private String dutyGrade;

    /**
     * 职级编码
     */
    @JsonProperty("dutygradeid")
    private String dutyGredeId;

    /**
     * 全路径
     */
    @JsonProperty("fullpath")
    private String fullpath;

    /**
     * 标准岗ID
     */
    @JsonProperty("jobid1")
    private Long jobId1;

    /**
     * 标准岗名称
     */
    @JsonProperty("jobnames")
    private String jobNames;

    /**
     * 营销区域类别
     */
    @JsonProperty("saleareatype")
    private String saleAreaType;
}
