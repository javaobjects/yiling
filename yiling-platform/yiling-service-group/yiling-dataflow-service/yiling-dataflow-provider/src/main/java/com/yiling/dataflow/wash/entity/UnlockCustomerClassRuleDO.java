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
@TableName("unlock_customer_class_rule")
public class UnlockCustomerClassRuleDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 排序序号 数值越小，排序越高
     */
    private Long orderNo;

    /**
     * 条件1 1-客户名称 2-客户名称结尾
     */
    private Integer condition1;

    /**
     * 运算符1 1-包含 2-不包含 3-等于
     */
    private Integer operator1;

    /**
     * 条件1的值
     */
    private String conditionValue1;

    /**
     * 条件2 1-客户名称 2-客户名称结尾
     */
    private Integer condition2;

    /**
     * 运算符2 1-包含 2-不包含 3-等于
     */
    private Integer operator2;

    /**
     * 条件2的值
     */
    private String conditionValue2;

    /**
     * 条件关系 1-且 2-或
     */
    private Integer conditionRelation;

    /**
     * 非锁终端分类 1-零售机构 2-商业公司 3-医疗机构 4-政府机构
     */
    private Integer customerClassification;

    /**
     * 最后操作时间
     */
    private Date lastOpTime;

    /**
     * 最后操作人id
     */
    private Long lastOpUser;

    /**
     * 状态 1-开启 0-关闭
     */
    private Integer status;

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
