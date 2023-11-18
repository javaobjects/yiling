package com.yiling.user.integral.entity;

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
 * 积分发放/扣减记录表
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("integral_give_use_record")
public class IntegralGiveUseRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 类型：1-发放 2-扣减
     */
    private Integer type;

    /**
     * 积分值
     */
    private Integer integralValue;

    /**
     * 发放/扣减时间
     */
    private Date operTime;

    /**
     * 类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    private Integer changeType;

    /**
     * 行为ID
     */
    private Long behaviorId;

    /**
     * 行为名称
     */
    private String behaviorName;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 操作备注
     */
    private String opRemark;

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
