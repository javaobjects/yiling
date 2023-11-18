package com.yiling.erp.client.api;

import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @Author: fei.wu
 * @Email:
 * @CreateDate: 2019-11-09
 * @Version: 1.0
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(HttpServletResponse.SC_OK, "Operation is Successful"),

    FAILURE(HttpServletResponse.SC_BAD_REQUEST, "Biz Exception"),

    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Request Unauthorized"),

    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "404 Not Found"),

    MSG_NOT_READABLE(HttpServletResponse.SC_BAD_REQUEST, "Message Can't be Read"),

    METHOD_NOT_SUPPORTED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Supported"),

    MEDIA_TYPE_NOT_SUPPORTED(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Media Type Not Supported"),

    REQ_REJECT(HttpServletResponse.SC_FORBIDDEN, "Request Rejected"),

    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),

    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Required Parameter"),

    PARAM_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Mismatch"),

    PARAM_BIND_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Binding Error"),

    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数验证错误"),
    
	REPETITIVE_OPERATION(1100, "请勿重复操作"),
    
    NEED_CAPTCHA(1000,"需要图形验证码"),

    // 业务信息不存在
    SHOP_NOT_FOUND(100404, "店铺不存在"),
    ITEM_NOT_FOUND(200404, "商品信息不存在"),

    PASSWORD_ERROR(1001,"您的账号或密码有误，请重新输入"),
    ACCOUNT_NOT_EXIST(1003,"您的手机号尚未注册或未加入企业"),
    IMAGE_CAPTCHA_ERROR(1007,"图形验证码输入错误"),
    CAPTCHA_ERROR(1002,"验证码输入错误"),
    SUBMIT_REPEAT(1004,"请勿重复提交"),
    SALER_INFO_INCOMPLETE(1005,"为了您的权益，请完成推广人信息备案"),
    NOT_JOIN_ENTERPRISE(1006,"您尚未加入企业，暂时无法登录商城"),

    //任务相关
    TASK_NOT_EXIST(1200,"任务不存在"),
    TAKE_RULE_NOT_SET(1201,"未设置参与条件"),
    FINISH_RULE_NOT_SET(1202,"未设置任务完成条件"),
    COMMISSION_RULE_NOT_SET(1203,"未设置佣金规则"),
    TASK_GOODS_NOT_SET(1204,"未设置任务商品"),
    GOOD_SERVICE_ERROR(1205,"商品查询接口异常"),
    USER_AREA_QUERY_ERROR(1206,"用户区域查询接口异常"),
    TASK_NOT_IN_PROGRESS(1207,"任务非进行中状态"),
    TASK_GOODS_NOT_IN_SET(1208,"任务未设置关联商品"),
    TERMINAL_QUERY_ERROR(1209,"终端查询服务异常"),
    USER_INFO_QUERY_ERROR(1210,"用户信息查询服务异常"),
    TASK_ORDER_NOT_EXIST(1211,"任务订单不存在"),
    TASK_ORDER_GOODS_NOT_EXIST(1212,"任务订单关联商品不存在"),
    COMMISSION_SAVE_ERROR(1213,"佣金保存异常"),
    TO_SELECT_TERMINAL(1214,"请选择终端"),
    UNFINISH_TASK(1216,"任务未完成不可再次承接"),



    CART_OVER_NUM(87101,"商品剩余库存不足"),
    CART_NUM_ERROR(87102,"购物车中的商品数量不能超过99个品种"),

    ERP_SYNC_ERROR(20001,"erp同步信息失败"),
    /**
     * 缺少方法名参数
     */
    Missing_Method(10, "缺少方法名参数"),
    /**
     * 不存在的方法名
     */
    Invalid_Method(11, "不存在的方法名"),
    /**
     * 缺少数据格式参数
     */
    Missing_Format(12, "缺少数据格式参数"),
    /**
     * 非法数据格式
     */
    Invalid_Format(13, "非法数据格式"),
    /**
     * 缺少签名参数
     */
    Missing_Signature(14, "缺少签名参数"),
    /**
     * 非法签名
     */
    Invalid_Signature(15, "非法签名"),
    /**
     * 缺少SessionKey参数
     */
    Missing_Access_Token(16, "缺少access_token参数"),
    /**
     * 无效的SessionKey参数
     */
    Invalid_Access_Token(17, "无效的access_token参数"),
    /**
     * 缺少AppKey参数
     */
    Missing_AppKey(18, "缺少AppKey参数"),
    /**
     * 非法的AppKey参数
     */
    Invalid_AppKey(19, "非法的AppKey参数"),
    /**
     * 缺少时间戳参数
     */
    Missing_Timestamp(20, "缺少时间戳参数"),
    /**
     * 非法的时间戳参数
     */
    Invalid_Timestamp(21, "非法的时间戳参数"),
    /**
     * 缺少版本参数
     */
    Missing_Version(22, "缺少版本参数"),
    /**
     * 非法的版本参数
     */
    Invalid_Version(23, "非法的版本参数"),
    /**
     * 不支持的版本号
     */
    Unsupported_Version(24, "不支持的版本号"),
    /**
     * 缺少必选参数
     */
    Missing_Required_Arguments(25, "缺少必选参数"),
    /**
     * 非法的参数
     */
    Invalid_Arguments(26, "非法的参数"),
    /**
     * 请求被禁止
     */
    Forbidden_Request(27, "请求被禁止"),
    /**
     * 参数错误
     */
    Parameter_Parameter(28, "参数错误"),
    /**
     * 远程服务出错
     */
    Remote_Service_Error(29, "远程服务出错"),
    /**
     * 缺少签名方法参数
     */
    Missing_Sign_Method(30, "缺少签名方法参数"),
    /**
     * 非法签名方法参数
     */
    Invalid_Sign_Method(31, "非法签名方法参数"),
    /**
     * 找不到对应的记
     */
    Find_Not_Value(32,"找不到对应的记录"),

    /**
     * 过期的session参数
     */
    Expire_Session(33, "过期的access_token参数"),
    /**
     * 过期的session参数
     */
    Invalid_AppSecret(34, "无效的appSecret"),

    // 会员模块（代码规则：13xx）
    VIP_GET_DISABLED(1301, "VIP获取方式已停用"),
    VIP_ORDER_ERROR(1302, "VIP单据信息有误"),
    VIP_PRODUCT_NOT_EXISTS(1303, "VIP产品信息不存在"),

    // 企业模块（代码规则：14xx）
    ENTERPRISE_DISABLED(1401, "当前企业已被停用"),
    ENTERPRISE_AUDIT_NOT_PASS(1402, "当前企业信息未审核通过"),

    // 用户模块（代码规则：15xx）
    USER_DISABLED(1501, "当前账号已被停用")
    ;

    final int code;

    final String msg;

}
