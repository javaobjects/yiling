package com.yiling.cms.content.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 医患问答DTO
 * </p>
 *
 * @author fan.shen
 * @date 2023-03-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QaDTO extends BaseDTO {

    private static final long serialVersionUID = -8755429127432498324L;

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
     * 引用业务线id
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
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
