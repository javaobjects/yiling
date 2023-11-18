package com.yiling.dataflow.crm.dto;


import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmManorRepresentativeDTO extends BaseDTO {
    /**
     * 辖区编码
     */
    private Long manorId;
    private String manorNo;
    private String name;

    /**
     * 代表岗位编码
     */
    private Long representativePostCode;

    /**
     * 代表岗位名称
     */
    private String representativePostName;

    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新日期
     */
    private Date updateTime;

    private String remark;
    private String updateUserName;
    /**
     * 代表编码
     */
    private String representativeCode;

    /**
     * 代表名称
     */
    private String representativeName;
    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 业务省区
     */
    private String businessProvince;

    /**
     * 业务区域代码
     */
    private String businessAreaCode;

    /**
     * 业务区域
     */
    private String businessArea;
    private String dutyGredeId;
    /**
     * 上级主管岗位代码
     */
    private Long superiorJob;

    /**
     * 上级主管岗位名称
     */
    private String superiorJobName;
    /**
     * 上级主管编码
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    private String superiorSupervisorName;
    /**
     * 部门
     */
    private String department;
}
