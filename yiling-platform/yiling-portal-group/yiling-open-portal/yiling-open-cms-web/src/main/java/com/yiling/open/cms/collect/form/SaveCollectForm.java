package com.yiling.open.cms.collect.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCollectForm extends BaseForm {
    /**
     * 内容/会议/文献id
     */
    @NotNull
    private Long collectId;

    /**
     * 收藏的类型：1-文章 2-视频 3-文献 4-会议
     */
    @NotNull
    private Integer collectType;

    /**
     * 收藏状态：1-收藏 2-取消收藏
     */
    @NotNull
    private Integer status;


    @NotNull
    private Long wxDoctorId;

    /**
     * 各业务线引用id
     */
    @NotNull
    private Long cmsId;
}