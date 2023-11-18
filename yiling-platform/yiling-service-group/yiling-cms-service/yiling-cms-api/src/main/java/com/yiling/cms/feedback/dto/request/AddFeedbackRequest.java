package com.yiling.cms.feedback.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddFeedbackRequest extends BaseRequest {

    private static final long serialVersionUID = -6581619375714909994L;
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

    /**
     * 反馈来源
     */
    private Integer source;
}