package com.yiling.cms.content.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * IH医生端内容表
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_ih_doctor_content")
public class IHDoctorContentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * cms_content主键
     */
    private Long contentId;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 栏目列表排序
     */
    private Integer categoryRank;

    /**
     * 浏览量
     */
    private Integer view;

    /**
     * 内容权限 1-仅登录，2-需认证
     */
    private Integer contentAuth;

    /**
     * 是否置顶 0-否，1-是
     */
    private Integer topFlag;

    /**
     * 引用状态 1-引用，2-取消引用
     */
    private Integer referStatus;

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
