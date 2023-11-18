package com.yiling.order.order.enums;

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
public enum OrderErrorCode implements IErrorCode {

    ORDER_NOT_EXISTS(10111, "订单不存在"),
    ORDER_INFO_STATUS_ERROR(10112, "订单状态不能进行操作"),
    ORDER_DETAIL_NOT_INFO(10113, "订单明细信息不存在"),
    ORDER_INFO_STATUS_CHANGE(10114, "订单状态已更改"),
    ORDER_INFO_PARAM_ERROR(10115, "参数不正确"),
    ORDER_INFO_DELIVERY_ERROR(10116, "订单发货信息不存在"),
    ORDER_INVOICE_STATUS_ERROR(10117, "发票状态不正确"),
    ORDER_DELIVERY_GOODS_NOT_ZERO(10118, "货物数量为0，不能使用票折"),
    ORDER_PRICE_LESS_THAN_RED_PRICE(10119, "商品折后单价已低于招标挂网价,请重新输入！"),
    ORDER_PRICE_LESS_THAN_GOODS_LOWEST_PRICE(10129, "该商品折扣比率不能小于设置最低价，请重新输入！"),
    ORDER_DISCOUNT_MORE_THAN_TICKET_DISCOUNT_AMOUNT(10120, "已超过票折的可用额度,请重新输入！"),
    ORDER_THAN_TICKET_DISCOUNT_NOT_EXISTS(10121, "票折信息不存在!"),
    ORDER_INVOICE_NOT_PASS_MONTH(10123, "发票不可跨月作废!"),
    ORDER_RETURN_NOT_ALL_AMOUNT(10124, "该订单没有全款退货，不可作废发票!"),
    ORDER_GOODS_NOT_RETURN_NOW(10125, "该订单的退货流程尚未走完，请联系供应商将退货处理完毕后才可申请收货！"),
    ORDER_GOODS_OPEN_INVOICE_NOT_RETURN(10126, "处于开票状态，无法进行退货"),
    ORDER_RETURN_GOODS_NOT_PASS_YEAR(10127, "跨年不可退货!"),
    ORDER_AUDIT_STATUS_INCORRECT(10128, "订单审核状态不正确，不能修改"),
    ORDER_NOT_AUDIT_STATUS_INCORRECT(10129, "订单反审核状态不正确，不能修改"),
    ORDER_NOT_AUDIT_BATCH_INCORRECT(10130, "订单反审核状态批次数据不正确"),
    ORDER_NOT_AUDIT_LOCK_INCORRECT(10131, "反审核获取Redis锁失败!"),
    RECEIVABLE_ORDER_STATUS_INVALID(10132, "应收单状态无效"),
    RECEIVABLE_ORDER_NOT_EXISTS(10133, "应收单不存在"),
    INVOICE_NO_ORDER_NOT_EXISTS(10134, "发票单号不存在"),
    ORDER_CANCEL_POWER_ERROR(10135, "没有权限不能取消"),
    ORDER_NOT_AUDIT_STATUS_REPEAT(10136, "订单反审核,相同订单明细批次重复"),
    ORDER_NOT_AUDIT_INVOICE_STATUS_INCORRECT(10137, "订单反审核开票状态不正确"),
    ERP_DELIVERY_NO_NOT_EXISTS(10138, "ERP出库单不存在"),
    ORDER_DELIVER_NUMBER_NOT_AMPLE(10147, "发货数量不足"),
    ORDER_INFO_CUSTOMER_STATUS_ERROR(10148, "客户确认状态已更改"),
    ORDER_INVOICE_QUANTITY_ERROR(10139, "开票数量超过，重新输入"),
    RECEIVABLE_ORDER_REPEAT_ERROR(10140, "应收单重复"),
    ERP_DELIVERY_NO_REPEAT_ERROR(10141, "ERP出库单重复"),
    ERP_DELIVERY_NO_TICKET_DISCOUNT_AMOUNT_ERROR(10142, "ERP出库单票折金额错误"),
    ERP_TRANSITION_RULE_CODE_ERROR(10143, "发票转换规则必须相同"),

    ORDER_CATEGORY_NOT_PAY(10144,"订单类型不正确不能支付"),

    SUBMIT_CART_CHANGED(102101, "购物车信息发生变化，请返回购物车重新下单"),
    SUBMIT_NO_PURCHASE_RELATION(102102, "您与部分配送商无采购关系，请您删除不可采购的商品，如有疑问请联系负责商务人员"),
    SUBMIT_NO_PURCHASE_AUTHORITY(102103, "部分商品无采购权限，请您删除不可采购的商品，如有疑问请联系负责商务人员"),
    SUBMIT_GOODS_OFF_SHELF(102104, "进货单中包含下架商品，请移除后重提提交"),
    SUBMIT_GOODS_INVENTORY_NOT_ENOUGH(102105, "进货单中商品存在库存不足，请重维护数量后再提交"),
    CART_NOT_EXIST(102106, "进货单不存在"),

    ORDER_RETURN_QUANTITY_OVERSTEP(10301, "批次号退货数量超出收货剩余数量"),
    ORDER_RETURN_QUANTITY_ZERO(10302, "批次号退货数量不能为0"),
    ORDER_RETURN_ORDER_STATUS_ERROR(10303, "订单未签收不能申请退货"),
    ORDER_RETURN_INVOICE_STATUS_ERROR(10304, "存在申请中的发票或已作废的发票"),
    ORDER_RETURN_STATUS_ERROR(10305, "退款单已被审核"),
    ORDER_RETURN_STATUS_IS_AUDIT_EXIST(10306, "已提交的退货单正在审核，请联系供应商加快审核，审核完毕后可再次提交退货申请！"),
    ORDER_RETURN_OTHER_YEAR(10307, "跨年订单不可退货！"),
    ORDER_RETURN_TYPE_ERROR(10308, "申请的退货单类型有错误！"),
    ORDER_RETURN_FORM_ERROR(10309, "申请的退货单数据有错误！"),
    B2B_ORDER_RETURN_ORDER_STATUS_ERROR(10310, "订单未发货或已收货，不允许线上申请退货，请联系供应商线下退货"),
    B2B_ORDER_RETURN_ORDER_TIME_ERROR(10311, "只有订单发货后7天内允许线上退货"),
    ORDER_PAYMENT_DAY_ERROR(10322, "账户账期余额不足！"),
    ORDER_SHOP_START_ERROR(10323, "部分企业未满足起配金额，请满足后重新提交！"),
    SKU_NOT_EXIST(10324, "商品规格不存在!"),
    SKU_PACKAGE_NUMBER_EXIST(10325, "请按包装规格购买商品!"),

    ORDER_COUPON_USE_NULL_ERROR(10326, "未查询到订单优惠劵使用记录，请检查"),
    ORDER_COUPON_USE_RETURNED_ERROR(10327, "订单取消优惠券已退还至账户"),
    COUPON_NULL_ERROR(10328, "优惠券信息不存在"),
    COUPON_NOT_USED_ERROR(10329, "此优惠券未使用，优惠券id：[{0}]"),

    ORDER_RETURN_DETAIL_NOT_ERROR(10330, "申请的退货单没有选择商品"),
    ORDER_RETURN_DETAIL_BATCH_NOT_ERROR(10331, "申请的退货单有商品没有明细批次信息"),
    ORDER_RETURN_DETAIL_BATCH_FALSE_ERROR(10335, "申请的退货单商品明细批次不存在"),
    ORDER_SALE_ERROR(10332, "包含不在供应商销售区域内的订单，请移除后重新提交"),
    LIMIT_GOODS_SALE_ERROR(10333, "订单中存在控销商品，请移除后重新提交!"),
    NOT_RELATION_SHIP_ERROR(10334, "商品未建立采购关系!"),

    REFUND_HAVE_FINISH(10336, "此退款单已经退款，不允许重新退款"),
    REFUND_FAIL(10337, "申请退款失败"),

    ORDER_GOODS_NOT_STOCK(10338, "订单库存不满足，无需审核！"),

    ORDER_PROMOTION_EXPIRED(10339, "商品:[{0}],对应的促销活动已失效"),
    ORDER_PROMOTION_LIMITD(10340, "商品:[{0}],已达到秒杀上限"),
    ORDER_PROMOTION_SPECIAL_ERROR(10341, "秒杀特价扣减失败!"),
    SUBMIT_GOODS_DISABLE(10342, "进货单中包含失效商品，请移除后重提提交"),
    ORDER_PROMOTION_ERROR(10343, "促销活动已失效"),
    ORDER_PROMOTION_MINIMUM_NUM(10344, "加购数量少于活动最小起订量[{0}]"),
    ORDER_RETURN_UNION_ACTIVITY_QUANTITY_ERROR(10345, "组合包退货数量异常[{0}]"),
    ORDER_MONEY_ERROR(10346, "订单金额异常"),
    ORDER_PROMOTION_LIMIT_GOODS_ERROR(10347, "组合包活动以岭品未设置限价,请联系运营人员处理!"),
    SPLIT_ORDER_ERROR(10348, "拆单异常,请联系技术处理!"),
    SUBMIT_DEPARTMENT_ERROR(10349, "请选择正确的商务联系人部门!"),
    SUBMIT_PRESALE_ORDER_ERROR(10350, "预售活动信息发生变化,请重新下单!"),
    SUBMIT_PRESALE_GOODS_MIN_ERROR(10351, "最小预定量为[{0}],请重新修改!"),
    SUBMIT_PRESALE_GOODS_MAX_ERROR(10352, "当前客户最多还可以预定[{0}],请调整商品购买数量!"),
    SUBMIT_PRESALE_GOODS_ALL_ERROR(10353, "当前活动最多还可以预定[{0}],请调整商品购买数量!"),

    SUBMIT_GOODS_LIMIT_ONE_ERROR(10356, "限购{0}件,已达限购数!"),
    SUBMIT_GOODS_LIMIT_DAY_ERROR(10357, "每天限购{0}件,已达限购数!"),
    SUBMIT_GOODS_LIMIT_WEEK_ERROR(10358, "每周限购{0}件,已达限购数!"),
    SUBMIT_GOODS_LIMIT_MONTH_ERROR(10359, "每月限购{0}件,已达限购数!"),
    SUBMIT_GOODS_LIMIT_PERIOD_ERROR(10360, "{0}天内限购{1}件,已达限购数!"),
    SUBMIT_GOODS_LIMIT_ERROR(10361, "{0}商品超出限购!"),
    CART_COMBINATION_GOODS_LIMIT_ERROR(10362, "限购{0}包，已达限购数"),
    SUBMIT_COMBINATION_GOODS_LIMIT_ERROR(10363, "组合包{0}限购{1}包,已达限购数"),
    CART_COMBINATION_ORDER_LIMIT_ERROR(10364, "组合包数量不足!"),
    SUBMIT_COMBINATION_ORDER_LIMIT_ERROR(10365, "组合包{0}数量不足!"),
    ;

    private final Integer code;
    private final String  message;
}
