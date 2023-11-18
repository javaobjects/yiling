package com.yiling.sales.assistant.task.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码 统一规则 11***
 *
 * @author: ray
 * @date: 2021/9/14
 */
@Getter
@AllArgsConstructor
public enum AssistantErrorCode implements IErrorCode {
    RULE_REPEAT(11000, "规则重复"),
    TASK_NOT_EXIST(11001, "任务不存在"), TAKE_RULE_NOT_SET(11002, "未设置参与条件"), FINISH_RULE_NOT_SET(11003, "未设置任务完成条件"),
    COMMISSION_RULE_NOT_SET(11000, "未设置佣金规则"), TASK_GOODS_NOT_SET(11000, "未设置任务商品"), GOOD_SERVICE_ERROR(11000, "商品查询接口异常"),
    USER_AREA_QUERY_ERROR(11004, "用户区域查询接口异常"), TASK_NOT_IN_PROGRESS(1105, "任务非进行中状态"),
    TASK_GOODS_NOT_IN_SET(11006, "任务未设置关联商品"), TERMINAL_QUERY_ERROR(11007, "终端查询服务异常"),
    USER_INFO_QUERY_ERROR(11008, "用户信息查询服务异常"), TASK_ORDER_NOT_EXIST(11009, "任务订单不存在"),
    TASK_ORDER_GOODS_NOT_EXIST(11010, "任务订单关联商品不存在"), COMMISSION_SAVE_ERROR(11011, "佣金保存异常"),
    TO_SELECT_TERMINAL(11012, "请选择终端"), UNFINISH_TASK(11013, "此任务承接过不可再次承接"),
    OVER_TAKE_LIMIT(11014,"已超出承接人数限制"),OVER_TAKE_TIMES_LIMIT(11015,"已超出承接次数限制"),
    DATA_NOT_EXIST(11016,"数据不存在"),
    DISTRIBUTOR_NOT_SET(11017,"请选择配送商"),
    TASK_AREA_NOT_SET(11018,"请选择任务区域"),
    TASK_AREA_NOT_MATCH(11019,"销售区域和任务区域不匹配"),
    SAME_TYPE_EXIST(11020,"已存在同类型的任务"),
    TASK_TASK_REPEAT(11021,"请勿重复承接任务"),
    BILL_EXIST(12000,"单据编号已存在"),
    BILL_STATUS_CHANGED(12001,"单据状态已改变请刷新"),
    /**
	 * 1102**佣金相关
	 */
    COMMISSIONS_DETAIL_NOT_FOUNT(11021, "佣金明细不存在"),

	COMMISSIONS_PAID(11022, "佣金以兑付"),
	COMMISSIONS_PAY_AMOUNT_INVALID(11023, "兑付金额必须等于佣金明细金额"),
	COMMISSIONS_NOT_FOUNT(11024, "佣金记录不存在"),
	COMMISSIONS_UN_EFFECT(11025, "佣金记录无效"),
	COMMISSIONS_IS_PAID(11026, "佣金记录已结清"),
	COMMISSIONS_PAY_FAILD(11027, "佣金兑付失败"),
	COMMISSIONS_DETAIL_UPDATE_FAILD(11028, "更新佣金明细异常"),
	COMMISSIONS_USER_UPDATE_FAILD(11029, "更新用户佣金异常"),
	COMMISSIONS_UPDATE_FAILD(11020, "更新佣金记录异常"),
    ;

    private final Integer code;
    private final String  message;
}
