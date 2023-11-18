package com.yiling.sjms.agency.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EsbEmployVO extends BaseVO {

    /**
     * UID
     */
    @ApiModelProperty("机构名称")
    private Long empUid;

    /**
     * 姓名
     */
    @ApiModelProperty("业务代表姓名")
    private String empName;

    /**
     * 工号
     */
    @ApiModelProperty("业务代表工号")
    private String empId;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别
     */
    private String sex;

    /**
     * 在职状态
     */
    private String status;

    /**
     * 地址
     */
    private String homeAddress;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 所属部门ID
     */
    private Long deptId;

    /**
     * 所属部门名称
     */
    private String deptName;

    /**
     * 所属部门岗位ID
     */
    @ApiModelProperty("岗位编码")
    private Long jobId;

    /**
     * 所属部门岗位名称
     */
    @ApiModelProperty("岗位名称")
    private String jobName;

    /**
     * 证件类型
     */
    private String certificateTypeId;

    /**
     * 证件号码后八位
     */
    private String certificateNumber;

    /**
     * 直接上级的工号
     */
    @ApiModelProperty("上级主管工号")
    private String superior;

    /**
     * 直接上级的姓名
     */
    @ApiModelProperty("上级主管姓名")
    private String superiorName;

    /**
     * 直接上级部门ID
     */
    private Long superiorDept;

    /**
     * 直接上级岗位ID
     */
    @ApiModelProperty("直接上级岗位ID")
    private Long superiorJob;

    /**
     * 兼职部门1的部门ID
     */
    private Long partDept1;

    /**
     * 兼职部门2的部门ID
     */
    private Long partDept2;

    /**
     * 兼职部门3的部门ID
     */
    private Long partDept3;

    /**
     * 兼职岗位1的岗位ID
     */
    private Long partJob1;

    /**
     * 兼职岗位2的岗位ID
     */
    private Long partJob2;

    /**
     * 兼职岗位3的岗位ID
     */
    private Long partJob3;

    /**
     * 初始来源系统
     */
    private String sourceSys;

    /**
     * 来源数据ID
     */
    private String sourceDetailId;

    /**
     * 状态：0-正常 1-失效
     */
    private String state;

    /**
     * 营销省区名称
     */
    @ApiModelProperty("业务省区")
    private String yxProvince;

    /**
     * 营销省区名称
     */
    @ApiModelProperty("省区")
    private String provinceArea;

    /**
     * 营销部门名称
     */
    @ApiModelProperty("业务部门")
    private String yxDept;

    /**
     * 营销区办名称
     */
    @ApiModelProperty("业务区域")
    private String yxArea;

    /**
     * 职级名称
     */
    private String dutyGrade;

    /**
     * 职级编码
     */
    private String dutyGredeId;

    /**
     * 全路径
     */
    private String fullpath;

    /**
     * 标准岗ID
     */
    private Long jobId1;

    /**
     * 标准岗名称
     */
    private String jobNames;

    /**
     * 营销区域类别
     */
    private String saleAreaType;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 前端页面部门
     */
    @ApiModelProperty("部门")
    private String department;

    /**
     * 省区经理岗位代码
     */
    @ApiModelProperty("省区经理岗位代码")
    private Long provincialManagerPostCode;

    /**
     * 省区经理岗位名称
     */
    @ApiModelProperty("省区经理岗位名称")
    private String provincialManagerPostName;

    /**
     * 省区经理工号
     */
    @ApiModelProperty("省区经理工号")
    private String provincialManagerCode;

    /**
     * 省区经理姓名
     */
    @ApiModelProperty("省区经理姓名")
    private String provincialManagerName;

    /**
     * 上级主管岗位名称
     */
    @ApiModelProperty("上级主管岗位名称")
    private String superiorJobName;
}
