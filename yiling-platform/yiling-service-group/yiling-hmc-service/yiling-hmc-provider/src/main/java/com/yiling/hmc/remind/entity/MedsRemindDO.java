package com.yiling.hmc.remind.entity;

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
 * 用药提醒主表
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_meds_remind")
public class MedsRemindDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 单次用量（例如：3粒、1袋）
     */
    private String useAmount;

    /**
     * 用法用量单位（例如：粒、袋）
     */
    private String useAmountUnit;

    /**
     * 用药次数
     */
    private Integer useTimesType;

    /**
     * 用药天数
     */
    private Integer useDaysType;

    /**
     * 提醒状态 1-有效，2-无效
     */
    private Integer remindStatus;

    /**
     * 过期时间
     */
    private Date expireDate;

    /**
     * 清除状态 1-未清除，2-已清除
     */
    private Integer clearStatus;

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
