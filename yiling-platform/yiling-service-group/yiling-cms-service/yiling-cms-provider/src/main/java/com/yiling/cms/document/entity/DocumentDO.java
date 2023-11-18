package com.yiling.cms.document.entity;

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
 * 文献
 * </p>
 *
 * @author gxl
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_document")
public class DocumentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * cms_document_category表id
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 简述
     */
    private String resume;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 状态：1-未发布 2-已发布
     */
    private Integer status;

    /**
     * 引用业务线：多个用，号分隔
     */
    private String displayLine;

    /**
     * 点击量
     */
    private Integer pageView;

    /**
     * 内容
     */
    private String content;

    /**
     * 文献pdf oss key
     */
    private String documentFileUrl;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;

    /**
     * 发布时间
     */
    private Date publishTime;

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

    private String documentFileName;
}
