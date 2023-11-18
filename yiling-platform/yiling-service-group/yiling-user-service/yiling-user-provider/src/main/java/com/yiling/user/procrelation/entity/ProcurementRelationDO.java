package com.yiling.user.procrelation.entity;

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
 * pop采购关系表
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pop_procurement_relation")
public class ProcurementRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 原版本号
     */
    @TableField(exist = false)
    private Integer originalVersion;

    /**
     * 版本id全局唯一，用于其他业务关联
     */
    private String versionId;

    /**
     * 采购关系编号
     */
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    private Long factoryEid;

    /**
     * 工业主体名称
     */
    private String factoryName;

    /**
     * 配送商eid
     */
    private Long deliveryEid;

    /**
     * 配送商名称
     */
    private String deliveryName;

    /**
     * 渠道商eid
     */
    private Long channelPartnerEid;

    /**
     * 渠道商名称
     */
    private String channelPartnerName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 客户关系表id
     */
    private Long enterpriseCustomerId;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    private Integer deliveryType;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    private Integer procRelationStatus;

    /**
     * 停用时间
     */
    private Date stopTime;

    /**
     * 停用人
     */
    private Long stopUser;

    /**
     * 执行过期的时间
     */
    private Date expireTime;

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
