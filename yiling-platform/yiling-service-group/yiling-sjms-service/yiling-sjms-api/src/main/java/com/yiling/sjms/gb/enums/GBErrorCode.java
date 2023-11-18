package com.yiling.sjms.gb.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Getter
@AllArgsConstructor
public enum GBErrorCode implements IErrorCode {

    GB_FORM_STATUS_CHANGE(60001, "表单状态已改变"),
    GB_FORM_INFO_NOT_EXISTS(60002,"流程不存在"),
    GB_FORM_GOODS_QUANTITY_NOT_TIGHT(60003,"团购商品数量不能小于0"),
    GB_FORM_FILE_NOT_EMPTY(60004,"团购证据文件不能为空"),
    GB_FORM_GOODS_INFO_NOT_EMPTY(60005,"团购商品不能为空"),
    GB_FORM_MAIN_INFO_NOT_EMPTY(60006,"团购信息请填写完整"),
    GB_FORM_EVIDENCE_INFO_NOT_EMPTY(60007,"团购证据请选择完整"),
    GB_FORM_PARAMETER_ERROR(60008,"团购参数不正确"),
    GB_FORM_BASE_INFO_NOT_EMPTY(60009,"团购基础信息请填写完整"),
    GB_FORM_GOODS_INFO_NOT_COMPLETE(60010,"团购商品信息不完整"),
    GB_FORM_INFO_SUBMIT_START(60011,"团购通道已关闭,无法提交"),
    GB_FORM_CUSTOMER_NAME_EXISTS(60012,"团购单位已存在"),
    GB_FORM_CREDIT_CODE_ERROR(60013,"团购统一信用代码不符合规范"),
    GB_FORM_CREDIT_CODE_EXISTS(60014,"团购统一信用代码存在"),
    GB_FORM_COMPANY_INFO_NOT_EMPTY(60015,"团购出库终端和商业不能为空"),
    GB_FORM_FILE_INFO_NOT_REPEAT(60016,"文件已在系统中存在，不允许重复上传！"),
    GB_FORM_GOODS_INFO_LAPSE_ERROR(60017,"团购商品信息失效"),
    ;

    private final Integer code;
    private final String  message;
}
