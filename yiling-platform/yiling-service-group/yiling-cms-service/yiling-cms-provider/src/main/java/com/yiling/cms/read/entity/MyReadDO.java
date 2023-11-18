package com.yiling.cms.read.entity;

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
 * 我的阅读
 * </p>
 *
 * @author gxl
 * @date 2022-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("my_read")
public class MyReadDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 阅读的内容/文献/会议id
     */
    private Long readId;

    /**
     * 阅读类型：1-文章 2-视频 3-文献 4-会议
     */
    private Integer readType;

    /**
     * 标题
     */
    private String title;

    /**
     * 阅读来源
     */
    private Integer source;

    /**
     * cms 各个业务线表主键
     */
    private Long cmsId;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 内容创建时间
     */
    private Date contentTime;

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
