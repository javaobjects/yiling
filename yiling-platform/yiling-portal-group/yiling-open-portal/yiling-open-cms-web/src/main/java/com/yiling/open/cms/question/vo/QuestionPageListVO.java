package com.yiling.open.cms.question.vo;

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
public class QuestionPageListVO extends BaseDTO {

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
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 是否回复 1-未回复 2-已回复 3-不需要回复
     */
    @ApiModelProperty("是否回复 1-未回复 2-已回复 ")
    private Integer replyFlag;

    /**
     *  标准库药品名称
     */
    @ApiModelProperty("标准库药品名称")
    private String name;

    /**
     *标准库药品规格
     */
    @ApiModelProperty("标准库药品规格")
    private String sellSpecifications;


}
