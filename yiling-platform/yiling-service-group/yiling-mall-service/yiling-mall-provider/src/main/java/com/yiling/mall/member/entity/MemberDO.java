package com.yiling.mall.member.entity;

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
 * B2B-会员表
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_member")
public class MemberDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 会员名称
     */
    private String name;

    /**
     * 会员描述
     */
    private String description;

    /**
     * 背景颜色
     */
    private String bgColor;

    /**
     * 获取条件：1-付费 2-活动赠送
     */
    private Integer getType;

    /**
     * 是否续卡提醒：0-否 1-是
     */
    private Integer renewalWarn;

    /**
     * 到期前提醒天数
     */
    private Integer warnDays;

    /**
     * 是否停止获取：0-否 1-是
     */
    private Integer stopGet;

    /**
     * 背景图
     */
    private String bgPicture;

    /**
     * 会员点亮图
     */
    private String lightPicture;

    /**
     * 会员熄灭图
     */
    private String extinguishPicture;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
