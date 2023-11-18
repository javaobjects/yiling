package com.yiling.sjms.flee.vo;

import java.util.List;

import com.yiling.sjms.form.vo.FormVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/14 0014
 */
@Data
public class FleeingFormDetailVO extends FormVO {

    @ApiModelProperty(value = "申报类型 1-电商、2-非电商")
    private Integer fleeingType;

    /**
     * 主流程表单id
     */
    @ApiModelProperty(value = "主流程表单id")
    private Long formId;


    /**
     * 申诉描述
     */
    @ApiModelProperty(value = "申诉描述")
    private String fleeingDescribe;

    /**
     * 确认状态：1-待提交 2-已提交
     */
    @ApiModelProperty(value = "确认状态：1-待提交 2-已提交")
    private Integer confirmStatus;

    /**
     * 确认时的备注意见
     */
    @ApiModelProperty(value = "确认时的备注意见")
    private String confirmDescribe;
}
