package com.yiling.sales.assistant.app.mr.question.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 疑问库列表
 * @author wei.wang
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionDelegatePageListVO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 问题标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 内容详情
     */
    @ApiModelProperty("内容详情")
    private String content;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
