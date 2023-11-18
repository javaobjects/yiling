package com.yiling.cms.content.entity;

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
 * 问答表
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cms_qa")
public class QaDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 问答来源 1-内容管理
     */
    private Integer qaSource;

    /**
     * 问答类型 1-提问，2-解答
     */
    private Integer qaType;

    /**
     * cms_content主键
     */
    private Long contentId;

    /**
     * cms_qa问答表主键
     */
    private Long qaId;

    /**
     * 业务线id 1-HMC,2-IH-doc,3-IH-patient
     */
    private Integer lineId;

    /**
     * 展示状态 1-展示，2-关闭
     */
    private Integer showStatus;

    /**
     * 问答内容
     */
    private String content;

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
