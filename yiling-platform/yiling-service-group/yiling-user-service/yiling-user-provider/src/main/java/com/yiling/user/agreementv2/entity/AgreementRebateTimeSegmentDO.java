package com.yiling.user.agreementv2.entity;

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
 * 协议时段表
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_time_segment")
public class AgreementRebateTimeSegmentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 时段类型：1-全时段 2-子时段
     */
    private Integer type;

    /**
     * 时段开始时间
     */
    private Date startTime;

    /**
     * 时段结束时间
     */
    private Date endTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否规模返利：0-否 1-是（ka协议时才存在）
     */
    private Integer scaleRebateFlag;

    /**
     * 是否基础服务奖励：0-否 1-是（ka协议时才存在）
     */
    private Integer basicServiceRewardFlag;

    /**
     * 是否项目服务奖励：0-否 1-是（ka协议时才存在）
     */
    private Integer projectServiceRewardFlag;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
