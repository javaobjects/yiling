package com.yiling.open.cms.feedback.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
public class AddFeedbackForm extends BaseForm {
    /**
     * 反馈文本
     */
    private String feedbackText;

    /**
     * 反馈图片
     */
    private List<String> feedbackPicList;

    /**
     * 反馈人名字
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String mobile;

    private Integer wxDoctorId;
}