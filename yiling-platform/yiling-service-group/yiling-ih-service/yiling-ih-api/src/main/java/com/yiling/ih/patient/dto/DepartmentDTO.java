package com.yiling.ih.patient.dto;

import com.beust.jcommander.internal.Lists;
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
public class DepartmentDTO implements java.io.Serializable {

    private static final long serialVersionUID = -333710312121608L;

    private Integer id;

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

    private List<DepartmentDTO> children = Lists.newArrayList();

}
