package com.yiling.user.agreement.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: dexi.yao
 * @date: 2021/7/12
 */
@Getter
@AllArgsConstructor
public enum AgreementErrorCode implements IErrorCode {

	/**
	 * 更新协议失败
	 */
    AGREEMENT_UPDATE(150001, "更新协议失败"),
    AGREEMENT_STOP(150002, "进行中的才能停用"),
    AGREEMENT_DELETE_FAILED(150003, "删除协议失败"),
    AGREEMENT_DELETE_INVALIDATE(150004, "未开始状态的协议才能删除"),
    AGREEMENT_USE_ADD(150005, "新增或更新返利申请使用异常"),
    AGREEMENT_USE_DETAIL_ADD(150006, "新增或更新返利申请使用明细异常"),
    AGREEMENT_REBATE_UPDATE_EAS(150007, "更新eas表已兑付金额出错"),
    AGREEMENT_REBATE_UPDATE_APPLY(150008, "更新返利申请单表金额出错"),
    AGREEMENT_REBATE_UPDATE_APPLY_DETAIL(150009, "更新返利申请单明细表金额出错"),
    AGREEMENT_REBATE_UPDATE_DETAIL(150010, "只有其它类型的返利明细可修改"),
    AGREEMENT_REBATE_NOT_ORDER(150011, "返利计算时协议下没有订单"),
    AGREEMENT_REBATE_DETAIL(150012, "返利申请时保存明细出错"),
	AGREEMENT_APPLY_SAVE(150013, "申请单金额小于或等于0不能申请"),
	AGREEMENT_APPLY_SAVE_AMOUNT(150014, "申请单金额与当前计算金额不一致请重新计算");

    private final Integer code;
    private final String message;
}
