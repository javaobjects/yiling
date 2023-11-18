package com.yiling.user.common;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户模块错误码枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements IErrorCode {

    /**
     * 用户模块错误码
     */
    NOT_LOGIN(100000, "您还未登录或会话已过期"),

    CUSTOMER_HAS_JOINED_GROUP(100101, "部分客户已加入其它分组"),
    CUSTOMER_GROUP_NAME_EXISTS(100102, "分组名称已存在"),
    CUSTOMER_GROUP_NOT_EXISTS(100103, "企业客户分组不存在"),
    ENTERPRISE_CHANNEL_CUSTOMER_NOT_EXIST(100104, "企业渠道商信息不存在"),
    CUSTOMER_GROUP_NOT_CUSTOMER(100105, "原分组中没有客户"),

    UNIONID_EXISTS(100200, "unionID已存在"),
    USERNAME_EXISTS(100201, "用户名已存在"),
    MOBILE_EXISTS(100202, "手机号已存在"),
    STAFF_EXISTS(100203, "员工账号信息已存在"),
    EMPLOYEE_EXISTS(100204, "员工信息已存在"),
    STAFF_NOT_EXISTS(100205, "员工账号信息不存在"),
    STAFF_DISABLED(100206, "员工账号已被停用"),
    EMPLOYEE_NOT_EXISTS(100207, "员工信息不存在"),
    EMPLOYEE_IS_ADMIN_CANNOT_REMOVE(10028, "该员工为企业管理员，不允许被移出"),
    EMPLOYEE_CODE_EXISTS(10029,"当前输入的工号已存在，请更换工号"),
    EMPLOYEE_DISABLED(10030, "员工账号已被停用"),
    USER_OLD_PASSWORD_ERROR(10031, "当前用户原密码不正确"),
    PASSWORD_ERROR(10032,"您的账号或密码有误，请重新输入"),
    ACCOUNT_NOT_EXIST(10033,"您的账号尚未加入企业，暂时无法登录"),
    IMAGE_CAPTCHA_ERROR(10034,"图形验证码输入错误"),
    CAPTCHA_ERROR(10035,"验证码输入错误"),
    SUBMIT_REPEAT(10036,"请勿重复提交"),
    SALER_INFO_INCOMPLETE(10037,"为了您的权益，请完成推广人信息备案"),
    ACCOUNT_HAD_STOP_USE(10038,"您的账号已被停用，请联系客服或企业管理员"),
    PHONE_OR_PASSWORD_ERROR(10039,"您填写的手机号或密码有误，请重新填写"),
    ACCOUNT_UNDER_REVIEW(10040,"账号正在审核中"),
    LOGIN_DYH_GO_BUY(10041,"请登录大运河APP进行采购"),
    VERIFY_CODE_ERROR(10042,"验证码错误或失效，请重新填写"),
    ACCOUNT_STOP(10043,"账号已被停用"),
    ACCOUNT_NOT_REGISTERED(10044,"您的账号尚未注册，请注册后登录"),
    PLEASE_LOGIN_POP_BUY(10045,"工业、商业用户请登录POP采购"),
    CHANGE_MOBILE_EXIST(10046,"手机号已有人使用，请确认您的新手机号"),

    PAYMENT_DAYS_UNREPAYMENT(10047,"有账期未还款"),
    HAVE_RETURN_ORDER_EXIST(10048,"有退货订单未完结"),
    HAVE_ORDER_NOT_SIGN(10049,"有未签收订单"),
    ACCOUNT_HAD_LOGOUT(10050,"您的账号已注销"),
    DEREGISTER_ACCOUNT_STATUS_ERROR(10051,"当前注销账号状态异常"),
    HAVE_NO_ANSWER_QUESTION(10052,"有未解答的疑问"),
    HAVE_UNDO_SALE_ORDER(10053,"有未签收的销售订单"),
    HAVE_UNDO_PURCHASE_ORDER(10054,"有未签收的采购订单"),
    YILING_ADMIN_DEREGISTER_FAIL(10055,"注销管理员账号请联系平台服务人员"),
    PAYMENT_DAYS_UNRECEIVE(10056,"有账期未收款"),

    ENTERPRISE_NOT_EXISTS(100301, "企业信息不存在"),
    ENTERPRISE_DISABLED(100302, "企业已被停用"),
    ENTERPRISE_NOT_AUTH_SUCCESS(100303, "企业未认证通过"),
    ENTERPRISE_LICENSE_NUMBER_EXISTS(100304, "社会统一信用代码已存在"),
    ENTERPRISE_OPEN_POP_PLATFORM_CHANNEL_NOT_NULL(100305, "开通POP时，渠道类型不能为空"),
    ENTERPRISE_CERTIFICATE_NOT_PASS(100306, "企业资质校验未通过"),
    ENTERPRISE_AUTH_GOING(100307, "企业审核中，无法修改"),
    ENTERPRISE_NOT_FIND_CERTIFICATE_TYPE(100308, "未找到企业类型对应的资质信息"),
    ENTERPRISE_TAG_NAME_EXIST(100309,"标签名称已存在"),
    ENTERPRISE_DEPARTMENT_NAME_EXISTS(100310, "部门名称已存在"),
    ENTERPRISE_DEPARTMENT_HAS_EMPLOYEE(100311, "当前部门或子部门下有员工，移除后才能停用"),
    ENTERPRISE_AUTH_STATUS_ERROR(100312, "企业审核状态不正确"),
    ENTERPRISE_POSITION_NAME_EXISTS(100312, "职位名称已存在"),
    NETERPRISE_POSITIOIN_NOT_EXISTS(100313, "职位信息不存在"),
    NETERPRISE_POSITIOIN_HAS_EMPLOYEE(100314, "职位下有员工"),
    ENTERPRISE_AUTH_INFO_NOT_FIND(100315, "企业审核信息未找到"),
    SHOP_NOT_EXISTS(100316, "店铺信息不存在"),
    ENTERPRISE_DEPARTMENT_CODE_EXISTS(100317, "部门编码已存在"),
    PROMOTER_ENTERPRISE_NOT_EXISTS(100318, "推广企业不存在"),
    ENTERPRISE_DEPARTMENT_PARENT_IN_SUBLIST(100319, "上级部门不能是当前部门或当前部门的下级部门"),
    SHOP_NOT_EXISTS_PLEASE_SET(100320, "店铺不存在，请先设置店铺基础信息"),
    ENTERPRISE_NOT_AUTH(100321, "您所在的企业认证通过之后，可登录"),
    ENTERPRISE_HAD_ESTABLISHED_PURCHASE(100322, "当前企业已经建立采购关系"),
    ENTERPRISE_NOT_EXIST_PURCHASE(100323, "当前采购关系不存在"),
    ENTERPRISE_CAN_NOT_OPEN_SALES_ASSISTANT(100324, "开通销售助手前需先开通POP或B2B"),
    ENTERPRISE_TERMINAL_CAN_NOT_OPEN_SALES_ASSISTANT(100325, "终端类型企业不允许开通销售助手"),
    ENTERPRISE_GOING_AUTH(100326,"您入驻的客户正在审核中，不能添加"),
    PURCHASE_AUTH_STATUS_ERROR(100327, "当前采购关系审核状态不正确"),
    ENTERPRISE_UPDATE_MANAGER_MOBILE_SAME(100328, "新账号不能与原账号相同"),
    ENTERPRISE_UPDATE_MANAGER_MOBILE_MANAGER_YET(100329, "新账号已是管理员，无需更换"),
    ADD_CUSTOMER_GET_LOCK_OUT_TIME(100330, "添加企业客户获取锁超时"),
    ENTERPRISE_MANAGER_NOT_EXIST(100331, "企业管理员为空不可启用，先维护管理员才可启用企业"),
    ENTERPRISE_NOT_EXIST_PAYMENT_METHOD(100332, "当前企业未开通线下支付"),
    SHOP_FLOOR_OVER_MAX(100333, "店铺楼层数量超过最大限制"),
    SHOP_FLOOR_HAVE_EXIST(100334, "店铺楼层名称重复"),
    SHOP_FLOOR_GOODS_OVER_MAX(100335, "店铺楼层商品数量超过最大限制"),

    ROLE_DEACTIVATE_ILLEGAL(100401, "角色下还有成员，不可停用，您需要先将成员转移至其他角色下才可停用"),
    ROLE_EDIT_ILLEGAL(100402, "角色不可修改"),
    ROLE_NOT_FOUND(100403, "角色不存在"),
	AUTH_ILLEGAL(100404, "当前操作权限，无法操作系统用户或角色"),
    ROLE_TYPE_ERROR(100405,"只能删除自定义角色"),
    ROLE_EXIST(100406,"您填写的名称已存在，避免角色重复请更换名称"),
    ROLE_MOVE(100406,"当前角色无员工使用，无需批量转移"),

    PAYMENT_DAYS_ACCOUNT_NOT_EXISTS(100501, "无账期信息"),
	PAYMENT_DAYS_ACCOUNT_DISABLED(100502, "账期账户已停用"),
    PAYMENT_DAYS_ACCOUNT_OUT_OF_CYCLE(100503, "账期账户不在有效期内"),
    PAYMENT_DAYS_ACCOUNT_CUSTOMER_BALANCE_NOT_ENOUGH(100504, "采购商可用余额不足"),
    PAYMENT_DAYS_ACCOUNT_GROUP_BALANCE_NOT_ENOUGH(100505, "集团可用余额不足"),
    PAYMENT_DAYS_ACCOUNT_SHORT_BALANCE_EXIST(100506, "存在待审核状态的临时额度申请，请在财务审核后再提交"),
    PAYMENT_DAYS_ACCOUNT_CHECK_FALSE(100507, "审核失败"),
    PAYMENT_DAYS_ACCOUNT_EXISTS(100508, "已存在账期信息"),
    PAYMENT_DAYS_ORDER_NOT_EXISTS(100509, "账期订单不存在"),
    PAYMENT_DAYS_ORDER_STATUS_ERROR(100560, "账期订单非待还款状态"),
    PAYMENT_DAYS_TEMPORARY_STATUS_ERROR(100561, "账期临时额度非待审核状态"),
    PAYMENT_DAYS_ACCOUNT_COMPANY_MORE_THAN(100562, "当前申请额度已经超过可申请上限，请调整后重新申请"),
    PAYMENT_DAYS_TEMPORARY_NOT_NULL(100563,"临时额度不能为空或者0"),
    PAYMENT_DAYS_RETURN_ERROR(100564,"账期退款金额超过支付金额"),
    PAYMENT_DAYS_ORDER_ALL_RETURN(100565,"该账期订单已经全部退款"),
    PAYMENT_DAYS_ORDER_TICKET_MORE(100566,"该账期订单票折金额高于支付金额"),
    PAYMENT_DAYS_ORDER_HAD_PAY(100567,"该账期订单已经支付"),
    PAYMENT_DAYS_AMOUNT_LESS(100568,"账期余额不足，请更换支付方式"),
    PAYMENT_DAYS_COMPANY_ERROR(100569,"账期异常，请联系负责商务处理"),

    // 销售助手
    SA_LOGIN_ACCOUNT_NOT_EXISTS(100601, "手机号不存在"),

    /**
     * 销售助手-客户管理相关错误码
     */
    CUSTOMER_EXIST(100701, "当前客户已存在，请勿重复添加"),
    CUSTOMER_NOT_EXIST(100702, "当前客户信息不存在"),
    USER_TEAM_NO_EXIST(100703, "当前团队不存在"),
    CUSTOMER_STATUS_ERROR(100704, "当前客户状态不正确"),
    LEADER_NOT_EXIST(100705, "队长信息不存在"),
    INVITE_MEMBER_NOT_EXIST(100706, "当前邀请用户不存在"),
    CUSTOMER_NOT_IN_SALES_AREA(100707,"客户不在您的可售范围之内，禁止添加"),
    CUSTOMER_NOT_OWNER(100708,"您不能添加自己为客户"),
    TEAM_USER_NO_EXIST(100709, "当前团队用户信息不存在"),
    LOCK_CUSTOMER_EXIST(100710, "锁定用户已存在"),
    ADD_LOCK_CUSTOMER_AREA_ERROR(100711, "添加锁定客户的省市区编码错误"),
    INVITE_MEMBER_HAD_EXIST_TEAM(100712, "该账号已绑定团队"),

    //会员管理
    MEMBER_NOT_EXIST(100801,"会员信息不存在"),
    EQUITY_NOT_EXIST(100802,"权益信息不存在"),
    BUY_STAGE_NOT_EXIST(100803,"会员购买条件不存在"),
    MEMBER_ORDER_NOT_EXIST(100804,"会员订单不存在"),
    MEMBER_ORDER_STATUS_ERROR(100805,"会员订单状态不正确"),
    MEMBER_EXIST(100806,"无法同时存在多个会员"),
    EQUITY_NAME_EXIST(100807,"权益名称重复"),
    MEMBER_TRADE_NOT_NULL(100808,"交易单号不能为空"),
    MEMBER_STATUS_ERROR(100809,"当前会员状态为停止获取"),
    MEMBER_BUY_RECORD_NOT_EXIST(100810,"会员购买记录不存在"),
    MEMBER_HAD_RETURN_SUCCESS(100811,"当前会员已经退款成功"),
    MEMBER_RETURNING(100812,"当前会员正在退款中"),
    MEMBER_RETURN_AMOUNT_MORE_THAN_PAY(100813,"会员退款金额不能大于支付金额"),
    MEMBER_RETURN_RECORD_NOT_EXIST(100814,"会员退款记录不存在"),
    MEMBER_ORDER_PAY_AMOUNT_ERROR(100815,"实付金额必须大于0"),
    MEMBER_ORDER_COUPON_USED(100816,"当前会员优惠券已被使用"),
    MEMBER_NAME_EXIST(100817,"会员名称重复"),
    ENTERPRISE_MEMBER_NOT_EXIST(100818,"企业会员不存在"),
    MEMBER_EQUITY_GOING(100819,"已有会员使用该权益"),
    MEMBER_CANCEL_ERROR(100820,"只有导入类型的记录才能取消"),

    REPEATED_BONUS_POINTS(100901,"相同订单请勿重复赠送积分"),

    // 协议2.0
    HAD_ADD_MANUFACTURER(101001, "该厂家已经添加，请勿重复添加"),
    MANUFACTURER_NOT_EXIST(101002, "厂家信息不存在"),
    HAD_RELATION_MANUFACTURER(101003, "该商品已经关联当前类型厂家"),
    FIRST_AGREEMENT_TYPE_ERROR(101004, "甲方关联协议类型不正确"),
    SPECIAL_BUSINESS_MUST_SET(101005, "指定商业公司购进时必须设置商业公司"),
    MUST_SET_PRODUCT_GROUP(101006, "非全系列品种下必须设置商品组"),
    MORE_THAN_SET_PRODUCT_GROUP(101007, "超过了非全系列品种设置商品组最大数"),
    SUPPLY_SALES_PRODUCT_NOT_NULL(101008, "供销商品不能为空"),
    CONTROL_SALES_AREA_TYPE_NOT_NULL(101009, "控销条件设置区域或客户类型数据不能为空"),
    MORE_THAN_SET_BUSINESS_ENTERPRISE(101010, "超过了返利条款指定商业公司最大数"),
    MORE_THAN_SET_OTHER_REBATE(101011, "超过了非商品返利阶梯设置最大数"),
    FULL_NOT_MORE_THAN_BACK(101012, "协议返利满返时，满不能大于返"),
    REBATE_TIME_SEGMENT_TIME_ERROR(101013, "协议返利子时段开始/结束时间不正确"),
    MANUFACTURER_HAD_RELATION_AGREEMENT(101014, "该厂家已经关联协议，无法删除"),
    MANUFACTURER_GOODS_HAD_RELATION_AGREEMENT(101015, "该商品已经关联有效协议，无法删除"),
    MORE_THAN_REBATE_SCOPE_NUM(101016, "超过了返利范围设置最大数"),
    REBATE_SCOPE_SET_ERROR(101017, "返利范围设置不正确"),
    SAME_SEGMENT_EXIST_SAME_GOODS(101018, "同个时段内存在重复商品"),
    SIGN_USER_NAME_EXIST(101019, "签订人名称已经存在"),
    AGREEMENT_NOT_EXIST(101020, "协议不存在"),
    AGREEMENT_REBATE_GOODS_EXIST(101021, "协议返利同个时段内存在重复商品"),
    AGREEMENT_REBATE_GOODS_NOT_IN_SUPPLY_SALES(101022, "协议返利商品：{} 不在返利供销商品内"),
    AGREEMENT_NOT_CAN_ARCHIVE(101023, "当前协议不能进行归档"),
    AGREEMENT_SPECIFICATION_GOODS_EXIST(101024, "规格商品ID：{}，商品名称：{}，已经存在{}"),
    AGREEMENT_SUPPLY_GOODS_NOT_IN_MANUFACTURER(101025, "协议供销商品：{} 不在厂家商品内"),
    AGREEMENT_EXIST(101026, "与协议编号{}重复，不允许新建"),

    // 收货地址
    DELIVERY_ADDRESS_OVER_MAX_NUM(102001, "收货地址数量达到上限"),
    DELIVERY_ADDRESS_NOT_EXIST(102002, "收货地址不存在"),

    // 积分模块
    INTEGRAL_RULE_NOT_EXIST(102101, "当前积分规则不存在"),
    INTEGRAL_RULE_START_TIME_ERROR(102102, "规则生效时间必须大于当前时间"),
    INTEGRAL_RULE_NAME_EXIST(102103, "积分规则名称重复"),
    SIGN_PERIOD_DAYS_ERROR(102104, "签到周期和天数配置不符"),
    TIME_RANGE_NOT_MORE_SIGN_RULE(102105, "当前规则所选行为与其他规则冲突，请重新配置"),
    PLATFORM_GOODS_TO_MANY(102106, "平台商品数量太多，请分批操作"),
    ENTERPRISE_GOODS_TO_MANY(102107, "店铺商品数量太多，请分批操作"),
    CUSTOMER_TO_MANY(102108, "客户数量太多，请分批操作"),
    INTEGRAL_EXCHANGE_GOODS_NOT_EXIST(102109, "积分兑换商品不存在"),
    INTEGRAL_EXCHANGE_REAL_GOODS_RECEIPT(102110, "兑付真实物品收货信息不能为空"),
    ORDER_UN_EXCHANGE(102111, "该订单还未兑付"),
    EXCHANGE_GOODS_INVENTORY_FAIL(102112, "兑换失败，商品库存不足，请到兑换页面查看"),
    EXCHANGE_GOODS_INTEGRAL_LESS(102113, "兑换失败，积分不足，请到兑换页面查看"),
    EXCHANGE_GOODS_FAIL(102114, "兑换失败，扣减商品库存出现异常"),
    INTEGRAL_USER_SIGN_ERROR(102115, "积分用户签到异常"),
    INTEGRAL_SIGN_DETAIL_LOOK_ERROR(102116, "仅支持查看下一月信息"),
    LOTTERY_ACTIVITY_NOT_UNION_RULE(102117, "当前抽奖活动未关联积分规则"),
    INTEGRAL_LESS(102118, "您的当前积分不足，无法使用积分抽奖啦"),
    INTEGRAL_JOIN_HAVE_TOP(102119, "积分参与抽奖次数已达上限"),
    TODAY_INTEGRAL_JOIN_HAVE_TOP(102120, "当日积分参与抽奖次数已达上限"),
    INTEGRAL_ORDER_GIVE_EXIST(102121, "当前规则所选行为与其他规则冲突，请重新配置"),
    INTEGRAL_EXCHANGE_GOODS_HAVE_TOP(102122, "抱歉，该商品已超过您的兑换上限，暂时无法兑换"),
    INTEGRAL_EXCHANGE_GOODS_ONLY_MEMBER(102123, "抱歉，该商品为指定会员类型专属，您暂时无法兑换"),
    INTEGRAL_EXCHANGE_GOODS_EXIST(102124, "当前兑换商品已存在"),
    INTEGRAL_EXCHANGE_GOODS_VALID_TIME_ERROR(102125, "请重新设置上下架有效期后再操作"),
    INTEGRAL_EXCHANGE_GOODS_OUT_VALID(102126, "兑换失败，不在有效期内"),
    INTEGRAL_EXCHANGE_GOODS_SOLD_OUT(102127, "兑换失败，该兑换商品已下架"),
    ;

    private Integer code;
    private String message;
}