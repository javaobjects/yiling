package com.yiling.sales.assistant.task.enums;

/**
 * @author gxl
 * 规则key
 */
public enum RuleKeyEnum {
    /**
     * 参与条件
     */
    //最大参与人数 -1代表不限制
    MAX_TAKE_PERSON,
    //每人可参与次数 -1代表不限制
    EACH_PERSON_TIMES,

    //最多可申领终端数 -1代表不限制
    MAX_SELECTED_TERMINAL_NUM,

    //任务承接人群 1-小三元 2-医药自然人
    TAKE_USER_GROUP,
    /**
     * 任务完成条件
     */
    //类型  1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员购买
    FINISH_TYPE,

    //时间限制
    TIME_LIMIT,

    //销售条件 1-阶梯 2-非阶梯
     STEP_CONDITION,
    //销售额或者销售数量 多个值用 ,号分隔
    SALE_CONDITION,

    //计算方式 1-单品计算 2-多品计算
    COMPUTE_MODE,

    //新客户是否有条件限制：-1 代表无限制 1代表有限制
    NEW_CUSTOMER_LIMIT,
    //0-首单 1-累计
    NEW_CUSTOMER_CONDITION,

    //采购额满足多少元
    NEW_CUSTOMER_AMOUNT,

    //支付方式 1-线下支付 2-账期  4-在线支付 多个用逗号分隔
    PAYMENT_METHOD,

    //是否会员 1-是 0-否
    IS_MEMBER,




    /**
     * 佣金设置
     */
    //佣金政策 0 多品统一执行（每个品佣金一样） 1 单独执行（每个品不一样）
    EXECUTE_TYPE,

    //1-固定金额 2-销售额%设置
    COMMISSION_MODE,

    //佣金 多个用,号分隔  交易额交易量情况下不需要前端传参
    COMMISSION,



    //是否有上下线分成 -1 否 是（分成比例）
    GIVE_INVITEE_AWARD,

}
