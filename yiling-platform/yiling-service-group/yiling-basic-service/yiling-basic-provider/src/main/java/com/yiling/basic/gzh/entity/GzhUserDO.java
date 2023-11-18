package com.yiling.basic.gzh.entity;

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
 * 健康管理中心公众号用户表
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("gzh_user")
public class GzhUserDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 公众号appId
     */
    private String appId;

    /**
     * 公众号openId
     */
    private String gzhOpenId;

    /**
     * 关注来源 1-自然流量，2-店员或者员工，3-药盒二维码
     */
    private Integer subscribeSource;

    /**
     * 订阅状态 1-订阅，2-取消订阅
     */
    private Integer subscribeStatus;

    /**
     * unionId
     */
    private String unionId;

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
