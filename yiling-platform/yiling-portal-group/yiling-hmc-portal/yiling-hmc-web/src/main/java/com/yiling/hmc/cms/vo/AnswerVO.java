package com.yiling.hmc.cms.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: fan.shen
 * @data: 2023-03-14
 */
@Data
public class AnswerVO extends BaseVO {

    @ApiModelProperty("回复结果")
    private Integer qaType;

    @ApiModelProperty("问答内容")
    private String content;

}
