package com.yiling.sjms.form;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2023/3/15
 */
@Getter
@AllArgsConstructor
public enum FleeFormErrorCode implements IErrorCode {

    /**
     * 表单数据不存在
     */
    FORM_NOT_FIND(190001, "表单数据不存在"),
    /**
     * 发起人不存在
     */
    START_USER_NOT_FIND(190002, "发起人不存在"),
    /**
     * 表单状态已改变
     */
    FORM_STATUS_CHANGE(190003, "表单状态已改变"),
    /**
     * 请求参数不能为空
     */
    REQUEST_NOT_NULL(19004, "请求参数不能为空"),
    /**
     * 生成流向表单数据不能为空
     */
    FORM_REQUEST_NOT_NULL(19005, "生成流向表单数据不能为空"),
    /**
     * 提交生成表单时的校验
     */
    FORM_DETAIL_ALL_RIGHT(19006, "请确保数据检查全部“通过”，导入状态全部“导入成功”后再生成流向表单"),
    /**
     * 此流向表单已经提交，不允许重复提交
     */
    FORM_DETAIL_NOT_RESUBMIT(19007, "此流向表单已经提交，不允许重复提交"),
    /**
     * 流向收集工作未开始/已结束
     */
    FORM_DETAIL_IS_CLOSE(19007, "流向收集工作未开始/已结束"),
    /**
     * 机构名称(申诉后)不能与原流向一致
     */
    NAME_IS_SAME(19008, "机构名称(申诉后)不能与原流向一致"),
    /**
     * 申诉数量不能为0或者负数
     */
    Error_19009(19009, "申诉数量不能为0或者负数"),
    /**
     * 申诉数量必须小于等于历史数量
     */
    Error_19010(19010, "申诉数量必须小于等于历史数量"),
    /**
     * 申诉数据必须为同一商业
     */
    Error_19011(19011, "申诉数据必须为同一商业"),
    /**
     * 申诉数据（ID）不能重复
     */
    Error_19012(19012, "申诉数据（ID）不能重复"),
    /**
     * 请先添加需要申诉的流向数据后再提交审核
     */
    Error_19013(19013, "请先添加需要申诉的流向数据后再提交审核"),
    /**
     * 申诉列表中已有流向数据被他人锁定申诉
     */
    FLOW_IS_LOCK(19014, "申诉列表中已有流向数据被他人锁定申诉"),
    /**
     * 申诉数据必须为同一商业
     */
    ERROR_19015(19015, "申诉数据必须为同一商业"),
    ERROR_19016(19016, "产品品名(申诉后)不能与原流向一致"),
    ERROR_19017(19017, "终端类型(申诉后)不能与原流向一致"),
    ERROR_19018(19018, "选择流向销售日期已超出审核范围"),
    ERROR_19019(19019, "申诉数量/标准客户名称/标准产品名称/机构属性未填写"),
    ;

    private final Integer code;
    private final String message;
}
