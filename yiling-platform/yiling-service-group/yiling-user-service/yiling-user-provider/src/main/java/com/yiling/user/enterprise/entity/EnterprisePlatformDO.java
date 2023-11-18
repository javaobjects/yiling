package com.yiling.user.enterprise.entity;

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
 * 企业开通平台表
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("enterprise_platform")
public class EnterprisePlatformDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 是否开通商城：0-否 1-是
     */
    private Integer mallFlag;

    /**
     * 是否开通POP：0-否 1-是
     */
    private Integer popFlag;

    /**
     * 是否开通销售助手：0-否 1-是
     */
    private Integer salesAssistFlag;

    /**
     * 是否开通互联网医院：0-否 1-是
     */
    private Integer internetHospitalFlag;

    /**
     * 是否开通数据中台：0-否 1-是
     */
    private Integer dataCenterFlag;

    /**
     * 是否开通HMC：0-否 1-是
     */
    private Integer hmcFlag;

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
