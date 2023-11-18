package com.yiling.user.procrelation.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Getter
@AllArgsConstructor
public enum ProcRelationErrorCode implements IErrorCode {

	/**
	 * 更新协议失败
	 */
    RELATION_ALREADY(230001, "相同的工业主体、配送商、渠道商、在同一时间点不能存在多个"),
    VID_CONFLICT(230002, "系统繁忙，请稍后重试"),
    END_TIME_INVALID(230003, "结束日期不能小于当前日期"),
    RELATION_GOODS_ALREADY(230004, "向采购关系中新增的商品已存在"),
    RELATION_NOT_FIND(230005, "采购关系不存在"),
    RELATION_SNAPSHOT_FIND(230006, "采购关系快照不存在"),
    RELATION_GOODS_NOT_FIND(230007, "更新采购关系中的商品折扣时数据不存在"),
    RELATION_NOT_FIND_DELIVERY_TYPE(230008, "采购关系不存在配送类型"),
    RELATION_CUSTOMER_RELATION_NOT_FOUND(230009, "客户关系不存在"),
    IMPORT_GOODS_TEMPLATE_NOT_FOUND(230010, "模板不存在"),
    TEMPLATE_GOODS_ALREADY(230011, "商品已存在"),
    TEMPLATE_TEMPLATE_ALREADY(230012, "模板名称以存在"),
	AGREEMENT_APPLY_SAVE_AMOUNT(150014, "申请单金额与当前计算金额不一致请重新计算");

    private final Integer code;
    private final String message;
}
