package com.yiling.dataflow.wash.entity;

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
 * 非锁客户分类规则表
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("unlock_customer_class_detail")
public class UnlockCustomerClassDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    private String ename;

    /**
     * 原始客户名称
     */
    private String customerName;

    /**
     * 是否分类：0-未分类 1-已分类
     */
    private Integer classFlag;

    /**
     * 非锁客户分类：1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    private Integer customerClassification;

    /**
     * 非锁客户分类：1-规则 2-人工
     */
    private Integer classGround;

    /**
     * 规则id
     */
    private Long ruleId;


    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 最后操作人id
     */
    private Long lastOpUser;

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
