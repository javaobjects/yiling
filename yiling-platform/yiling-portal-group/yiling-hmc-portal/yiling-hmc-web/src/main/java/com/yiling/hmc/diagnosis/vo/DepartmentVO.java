package com.yiling.hmc.diagnosis.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 科室对象
 *
 * @author: fan.shen
 * @date: 2023-12-27
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "科室对象")
public class DepartmentVO {

    private static final long serialVersionUID = -333710312121608L;

    private Integer id;

    @ApiModelProperty(value = "科室名")
    private String departmentName;

    private Integer parentId;

    private String icon;

    private Integer displayWeighty;

    private Integer displayFront;

    private Integer displayFirst;

    private Integer forbidden;

    private String presentation;

    private Integer hotRanking;

    private Integer countryDepartmentId;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "子节点")
    private List<DepartmentVO> children = Lists.newArrayList();

}
