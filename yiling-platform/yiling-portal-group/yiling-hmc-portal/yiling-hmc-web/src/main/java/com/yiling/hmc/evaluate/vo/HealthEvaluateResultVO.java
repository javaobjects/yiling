package com.yiling.hmc.evaluate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 测评结果
 *
 * @author: fan.shen
 * @date: 2022-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "web端测评结果")
public class HealthEvaluateResultVO {

    private static final long serialVersionUID = -333710312121608L;

    @ApiModelProperty(value = "测评结果")
    private String evaluateResult;

    @ApiModelProperty(value = "结果描述")
    private String resultDesc;

    @ApiModelProperty(value = "健康小贴士")
    private String healthTip;

    /**
     * 科室id
     */
    @ApiModelProperty(value = "科室id")
    private Integer departmentId;

    /**
     * 	父级科室id
     */
    @ApiModelProperty(value = "父级科室id")
    private Integer departmentParentId;

    @ApiModelProperty(value = "改善建议")
    private List<HealthEvaluateMarketAdviceVO> marketAdviceList;

    @ApiModelProperty(value = "关联药品")
    private List<HealthEvaluateMarketGoodsVO> marketGoodsList;

    @ApiModelProperty(value = "其他测评")
    private List<HealthEvaluateVO> otherEvaluateList;

    @ApiModelProperty(value = "推荐医生")
    private List<RecommendDoctorVO> recommendDoctorList;

}
