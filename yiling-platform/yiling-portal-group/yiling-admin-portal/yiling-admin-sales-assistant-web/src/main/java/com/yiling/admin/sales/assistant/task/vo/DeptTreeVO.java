package com.yiling.admin.sales.assistant.task.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业部门树
 * @author: gxl
 * @date: 2023/1/9
 */
@Data
public class DeptTreeVO {
    @ApiModelProperty(value = "部门id")
   private Long id;
    @ApiModelProperty(value = "父部门id")
   private Long parentId;

    /**
     * 部门编码
     */
    @ApiModelProperty(value = "父部门code")
    private String code;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;

    /**
     * 部门描述
     */
    private String description;
    @ApiModelProperty(value = "子部门")
    private List<DeptTreeVO> children;
}