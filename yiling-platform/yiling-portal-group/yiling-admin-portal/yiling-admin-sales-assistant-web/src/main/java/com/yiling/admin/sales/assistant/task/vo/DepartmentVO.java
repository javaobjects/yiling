package com.yiling.admin.sales.assistant.task.vo;

import java.util.List;

import lombok.Data;

/**
 * 创建企业任务选择部门
 * @author: ray
 * @date: 2021/10/12
 */
@Data
public class DepartmentVO {
    private String name;

    private Long id;

    private List<DepartmentVO> children;
}
