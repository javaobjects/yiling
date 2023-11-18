package com.yiling.cms.feedback.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 使用反馈
 * </p>
 *
 * @author gxl
 * @date 2022-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FeedbackDTO extends BaseDTO {


    private static final long serialVersionUID = -1832398342921020633L;
    /**
     * 反馈文本
     */
    private String feedbackText;

    /**
     * 反馈图片
     */
    private String feedbackPic;
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
