package com.yiling.user.member.entity;

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
 * 企业会员变更记录表
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_enterprise_member_log")
public class EnterpriseMemberLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 单据编号
     */
    private String orderNo;

    /**
     * 单据购买规则
     */
    private String orderBuyRule;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 操作类型：1-开通 2-续费 3-退费 4-修改推广方 5-取消导入记录
     */
    private Integer opType;

    /**
     * 变更天数
     */
    private Integer validDays;

    /**
     * 修改前会员开始时间
     */
    private Date beforeStartTime;

    /**
     * 修改前会员结束时间
     */
    private Date beforeEndTime;

    /**
     * 修改后会员开始时间
     */
    private Date afterStartTime;

    /**
     * 修改后会员结束时间
     */
    private Date afterEndTime;

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
