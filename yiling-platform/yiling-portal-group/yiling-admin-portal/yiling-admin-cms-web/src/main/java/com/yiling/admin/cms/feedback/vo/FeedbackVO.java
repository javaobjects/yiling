package com.yiling.admin.cms.feedback.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class FeedbackVO extends BaseVO {


    /**
     * 反馈文本
     */
    @ApiModelProperty(value = "反馈文本")
    private String feedbackText;

    /**
     * 反馈图片
     */
    @ApiModelProperty(value = "反馈图片")
    private List<String> feedbackPicList;

    /**
     * 反馈人名字
     */
    @ApiModelProperty(value = "反馈人名字")
    private String name;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 反馈来源
     */
    @ApiModelProperty(value = "反馈来源枚举值")
    private Integer source;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
