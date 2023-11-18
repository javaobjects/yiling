package com.yiling.settlement.report.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品错误码枚举
 *
 * @author: dexi.yao
 * @date: 2021/6/18
 */
@Getter
@AllArgsConstructor
public enum ReportErrorCode implements IErrorCode {

    //运营后台-数据报表-报表参数相关
    GOODS_CATEGORY_EXIST(122000,"此商品类型已存在"),
    GOODS_PRICE_EXIST_OF_TIME(1220001,"该时间段已存在此类型商品价格"),
    CATEGORY_GOODS_EXIST_OF_TIME(1220002,"该时间段已存在此类型商品"),
    CATEGORY_GOODS_EXIST_OF_CATEGORY(1220003,"此商品已在其他类型中存在"),
    LADDER_GOODS_EXIST_OF_TIME(1220003,"该时间段内在此阶梯中已存在此商品"),
    LADDER_GOODS_RANGE_INVALID(1220004,"阶梯数量冲突"),
    ACTIVITY_GOODS_EXIST_OF_TIME(1220005,"该时间段已存在此活动商品"),
    ACTIVITY_GOODS_EXIST_OF_ACTIVITY(1220006,"此时间段此商品已在其他活动中存在"),
    MEMBER_EXIST(1220007,"同一时间相同价格不能有多条返利"),
    SAVE_REPORT_FAIL(1220008,"保存报表失败"),
    PAR_NOT_NONE(1220009,"参数不能为空"),
    UPDATE_GOODS_ID_FAIL(1220010,"更新以岭商品id失败"),
    REPORT_NOT_FOUND(1220011,"报表不存在"),
    PURCHASE_NOT_ENTIRE(1220012,"购进库存不完整"),
    REPORT_REJECT_FAIL(1220013,"驳回报表失败"),
    REPORT_STATUS_EXCEPTION(1220014,"报表状态异常"),
    REPORT_CONFIRM_FAIL(1220015,"确认报表失败"),
    ADJUST_FAIL(1220016,"调整报表金额失败"),
    UPDATE_PURCHASE_FAIL(1220017,"更新采购流向库存失败"),
    UPDATE_PURCHASE_ORDER_FAIL(1220018,"更新销售流向订单失败"),
    PAR_SUB_GOODS_NOT_FOUND(1220019,"子参数商品不存在"),
    REPORT_TIME_SLOT_INVALID(1220020,"报表计算周期不能超过90天"),
    REPORT_B2B_ORDER_SYNC_INFO_INCOMPLETE(1220021,"返利报表的b2b订单同步时订单的收货信息残缺"),
    REPORT_REBATE_ILLEGAL(1220022,"标识返利的订单中包含已返利的报表明细"),
    REPORT_REBATE_DETAIL_NOT_FOUND(1220023,"当前筛选条件下没有可标记为已返利的报表明细"),
    REPORT_REBATE_INVALID(1220024,"报表状态非财务已确认的不能勾选返利"),
    ;

    private Integer code;
    private String message;
}
