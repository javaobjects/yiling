package com.yiling.dataflow.agency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 省区与业务省区对应关系表
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_department_area_relation")
public class CrmDepartmentAreaRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 部门
     */
    private String department;

    /**
     * 业务部门
     */
    private String departmentBusiness;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 业务省区
     */
    private String provincialAreaBusiness;

    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新日期
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String remark;


}
