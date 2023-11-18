package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 诊后评价
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "诊后评价对象")
public class DiagnosisCommentVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "整体满意度_星级")
    private Integer starLevel;

    @ApiModelProperty(value = "整体满意度_星级满意说明")
    private String starLevelExplan;

    @ApiModelProperty(value = "用户评价描述")
    private String userDescribe;

    @ApiModelProperty(value = "选中标签项")
    private String selectLabelItem;

}
