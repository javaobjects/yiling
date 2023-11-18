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
@ApiModel(value = "推荐医生")
public class RecommendDoctorVO {

    private static final long serialVersionUID = -333710312121608L;

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

    /**
     * 	问诊量
     */
    @ApiModelProperty(value = "问诊量")
    private Integer diagnosticCount;

    /**
     * 	医生名称
     */
    @ApiModelProperty(value = "医生名称")
    private String doctorName;

    /**
     * 	所在医疗机构
     */
    @ApiModelProperty(value = "所在医疗机构")
    private String hospitalName;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 	医生头像
     */
    @ApiModelProperty(value = "医生头像")
    private String picture;

    /**
     * 	职称
     */
    @ApiModelProperty(value = "职称")
    private String profession;

    /**
     * 	问诊费
     */
    @ApiModelProperty(value = "问诊费")
    private String registerFee;

}
