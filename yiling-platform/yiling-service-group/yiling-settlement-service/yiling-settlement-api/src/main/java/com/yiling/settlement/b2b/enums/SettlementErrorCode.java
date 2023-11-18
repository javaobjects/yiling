package com.yiling.settlement.b2b.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 结算单API操作码
 *
 * @author dexi.yao
 * @date 2021/10/21
 */
@Getter
@AllArgsConstructor
public enum SettlementErrorCode implements IErrorCode {

	//保存结算单失败
    SAVE_SETTLEMENT_FAIL(16001, "保存结算单失败"),
	//保存结算单明细失败
	SAVE_SETTLEMENT_DETAIL_FAIL(16002, "保存结算单明细失败"),
	//更新订单状态为以生成结算单失败
	UPDATE_ORDER_SETTLEMENT_GENERATE_FAIL(16003, "更新订单状态为以生成结算单失败"),
	//保存结算单-订单对账单失败
	SAVE_ORDER_SETTLEMENT_FAIL(16004, "保存结算单-订单对账单失败"),
	//结算单状态非法
	SETTLEMENT_STATUS_INVALID(16005, "结算单状态非法"),
	//更新订单的结算状态失败
	UPDATE_ORDER_SETTLEMENT_STATUS_FAIL(16006, "更新订单的结算状态失败"),
	//更新结算单明细的订单的结算状态失败
	UPDATE_ORDER_SETTLEMENT_DETAIL_STATUS_FAIL(16007, "更新结算单明细的订单的结算状态失败"),
	//更新结算单打款状态失败
	UPDATE_SETTLEMENT_STATUS_FAIL(16008, "更新结算单打款状态失败"),
	//更新结算单状态为锁定失败
	UPDATE_SETTLEMENT_LOCK_FAIL(16009, "更新结算单状态为锁定失败"),
	//结算单被锁定
	SETTLEMENT_LOCKED(16010, "结算单被锁定"),
	//收款账户的eid与当前更者的eid不匹配
	ACCOUNT_EID_INVALID(17001, "收款账户的eid与当前更者的eid不匹配"),
	//状态为失效的企业收款账户不允许修改
	ACCOUNT_INVALID(17002, "状态为失效的企业收款账户不允许修改"),
	//状态为审核中的企业收款账户不允许修改
	ACCOUNT_STATUS_INVALID(17003, "状态为审核中的企业收款账户不允许修改"),
	//更新企业收款账户为失效状态操作失败
	ACCOUNT_UPDATE_FAIL(17004, "更新企业收款账户为失效状态操作失败"),
	//更新企业收款账户为失效状态操作失败
	ACCOUNT_TOO_MANY(17005, "当前企业存在企业收款账户，不能再新增"),
	//企业收款账户不存在
	ACCOUNT_NOT_FOUND(17006, "企业收款账户不存在"),
	//企业收款账户状态不能打款
	ACCOUNT_CHECK_IN(17007, "企业收款账户状态不能打款"),

    ;

    private final Integer code;
    private final String  message;
}
