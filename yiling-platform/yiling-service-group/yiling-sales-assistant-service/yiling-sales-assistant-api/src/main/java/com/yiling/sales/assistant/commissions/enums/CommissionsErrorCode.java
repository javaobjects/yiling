package com.yiling.sales.assistant.commissions.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 佣金异常信息
 *
 * @author: dexi.yao
 * @date: 2021/09/18
 */
@Getter
@AllArgsConstructor
public enum CommissionsErrorCode implements IErrorCode {

	//更新用户佣金余额失败
	UPDATE_USER_COMMISSIONS(160001, "更新用户佣金余额失败"),
	//保存佣金明细失败
	SAVE_COMMISSIONS_DETAIL(160002, "保存佣金明细失败"),
	//佣金金额必须大于0
	SAVE_COMMISSIONS_CASH(160003, "佣金金额必须大于0"),
	//拉新类的佣金必须立即生效
	EFFECT_STATUS_INVALID(160005, "拉新类的佣金必须立即生效"),
	//该交易类任务对应的佣金记录已经是有效状态，不能在向其中添加佣金明细
	COMMISSIONS_EFFECT_INVALID(160006, "该交易类任务对应的佣金记录已经是有效状态"),
	//佣金记录不存在
	COMMISSIONS_NOTFOUND(160007, "佣金记录不存在"),
	//佣金记录存在多条
	COMMISSIONS_TO_MANY(160008, "佣金记录存在多条"),
	//更新佣金记录为生效时失败
    UPDATE_COMMISSIONS_EFFECT_FAIL(160009, "更新佣金记录为生效时失败"),
	//参数detailList不能为空
    ADD_COMMISSIONS_LIST_NOT_NULL(160010, "参数detailList不能为空"),
	;

	private final Integer code;
	private final String  message;
}
