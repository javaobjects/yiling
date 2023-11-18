package com.yiling.cms.question.entity;

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
 * 医药代表回复信息关联表
 * </p>
 *
 * @author wei.wang
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("question_reply_resource")
public class QuestionReplyResourceDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 回复id
     */
    private Long replyId;

    /**
     * 关联类型 1-关联文献 2-关联链接 3-图片说明 4-关联附件 
     */
    private Integer type;

    /**
     * 文献ID
     */
    private Long documentId;

    /**
     * 链接
     */
    private String url;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件图片key
     */
    private String resourceKey;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
