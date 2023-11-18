package com.yiling.sjms.oa.todo.feign;

import lombok.Data;

/**
 * API Result
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@Data
public class ApiResult implements java.io.Serializable {

    private static final long serialVersionUID = 7944789822838179571L;

    /**
     * 异构系统标识
     */
    private String syscode;

    /**
     * 数据类型
     * IsUse：统一待办中心
     * OtherSys：异构系统
     * WfType：流程类型
     * WfData：流程数据
     * SetParam：参数设置
     */
    private String dateType;

    /**
     * 操作类型
     * AutoNew：自动创建
     * New：新建
     * AutoEdit：自动更新
     * Edit：编辑
     * Del：删除
     * Check：检测
     * Set：设置
     */
    private String operType;

    /**
     * 操作结果
     * 1：成功
     * 0：失败
     */
    private String operResult;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 是否成功
     */
    public boolean isSuccessful() {
        return "1".equals(this.operResult);
    }
}
