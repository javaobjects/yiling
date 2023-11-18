package com.yiling.mall.banner.entity;

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
 * 热词管理表
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_app_hot_words")
public class B2bAppHotWordsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 热词名称
     */
    private String content;

    /**
     * 启用状态：0-启用 1-停用
     */
    private Integer useStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date stopTime;

    /**
     * 排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序
     */
    private Integer sort;

    /**
     * 来源1-POP 2-销售助手 3-B2B
     */
    private Integer hotWordsSource;

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


}
