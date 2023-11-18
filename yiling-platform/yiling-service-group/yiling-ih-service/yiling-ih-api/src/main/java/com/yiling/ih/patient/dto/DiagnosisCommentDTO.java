package com.yiling.ih.patient.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊后评价对象
 */
@Data
@Accessors(chain = true)
public class DiagnosisCommentDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;


    // @ApiModelProperty(value = "id")
    private Integer id;

    // @ApiModelProperty(value = "整体满意度_星级")
    private Integer starLevel;

    // @ApiModelProperty(value = "整体满意度_星级满意说明")
    private String starLevelExplan;

    // @ApiModelProperty(value = "用户评价描述")
    private String userDescribe;

    // @ApiModelProperty(value = "选中标签项")
    private String selectLabelItem;



}