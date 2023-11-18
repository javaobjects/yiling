package com.yiling.user.esb.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * esb岗位
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("esb_job")
public class EsbJobDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 部门岗ID
     */
    private Long jobDeptId;

    /**
     * 部门刚名称
     */
    private String jobDeptName;

    /**
     * 标准岗ID
     */
    private Long jobId;

    /**
     * 标准岗名称
     */
    private String jobName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门形成
     */
    private String deptName;

    /**
     * HRID
     */
    private String sourceDetailId;

    /**
     * 编制数
     */
    private String postPrepare;

    /**
     * 是否失效：0-正常，其他失效
     */
    @TableField("`state`")
    private String state;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
