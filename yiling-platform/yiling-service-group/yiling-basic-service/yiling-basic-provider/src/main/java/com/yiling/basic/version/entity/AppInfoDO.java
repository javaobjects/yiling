package com.yiling.basic.version.entity;

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
 * 应用信息
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("app_info")
public class AppInfoDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 应用编号
     */
    private String code;

    /**
     * 应用名称(暂时只有销售助手和B2B移动端)
     */
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * App类型：1-android 2-ios
     */
    private Integer appType;

    /**
     * 应用状态：1-上架中 2-已下架
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
