package com.yiling.dataflow.sale.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2023/4/12
 */
@Getter
@AllArgsConstructor
public enum DeptTargetErrorCode implements IErrorCode {

    /**
     * 月份数据存在重复
     */
    MONTH_ERR(210001, "月份数据存在重复"),
    /**
     * 部门指标不存在
     */
    TARGET_NOT_FIND(210002, "部门指标不存在"),
    /**
     * 部门不存在
     */
    DEPT_NOT_FIND(210003, "部门不存在"),
    /**
     * 省区不存在
     */
    PROVINCE_NOT_FIND(210004, "省区不存在"),
    /**
     * 月份不存在
     */
    MONTH_NOT_FIND(210005, "月份不存在"),
    /**
     * 品种不存在
     */
    GOODS_NOT_FIND(210006, "品种不存在"),
    /**
     * 部门配置数据不存在
     */
    DEPT_CONFIG_NOT_FIND(210007, "部门配置数据不存在"),
    /**
     * 部门配置数据不存在
     */
    DEPT_CONFIG_STATUS(210008, "部门配置状态不允许配置"),
    ;
    private final Integer code;
    private final String message;
}
