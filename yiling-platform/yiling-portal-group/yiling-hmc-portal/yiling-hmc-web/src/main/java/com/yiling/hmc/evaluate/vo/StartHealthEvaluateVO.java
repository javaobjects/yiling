package com.yiling.hmc.evaluate.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 开始测评
 *
 * @author: fan.shen
 * @date: 2022-12-21
 */
@Data
@Accessors(chain = true)
public class StartHealthEvaluateVO {

    private static final long serialVersionUID = -333710312121608L;

    /**
     * 开始测评id
     */
    @ApiModelProperty(value = "开始测评id")
    private Long startEvaluateId;

    /**
     * 量表名称
     */
    @ApiModelProperty(value = "量表名称")
    private String healthEvaluateName;

    /**
     * 测评题目
     */
    @ApiModelProperty(value = "测评题目")
    private List<HealthEvaluateQuestionVO> questionList;

}
