package com.yiling.sales.assistant.task.constant;

public interface TaskConstant {
    /**
     * 任务列表置顶key  type
     */
    String SET_TOP_KEY = "task:top:%s";
    /**
     * 平台任务数key
     */
    String PLATFORM_TASK_COUNT_KEY = "task:platform:count";
    /**
     * 企业任务数
     */
    String ENTERPRISE_TASK_COUNT_KEY = "task:enterprise:count";
    /**
     * 无限制
     */
    String UNLIMIT = "-1";

    String ONE = "1";

    String USER_SELECTED_TERMINAL_KEY = "task:%s:%s";

    /**
     * 最大时间
     */
    String MAX_DATE = "9999-12-31 23:59:59";

    /**
     * 任务停用消息模板
     */
    String STOP_TASK_MSG_TEMPLATE = "您所参与的%s任务已停用，已产生的交易还是会按照原有任务设定进行发放";
    /**
     * 任务订单添加锁key
     */
    String ADD_TASK_ORDER_LOCK = "task:order:add:%s";

    /**
     * 任务订单完结通知锁key
     */
    String NOTIFY_TASK_ORDER_LOCK = "task:order:notify:%s";
    /**
     * 用户承接任务锁key
     */
    String ADD_TASK_LOCK = "usertask:add:%s:%s";

    String FINISH_TYPE_CAL ="com";

    int COMMISSION_FLAG = 2;

    /**
     * 任务规则缓存key前缀
     */
     String TASK_RULE_KEY_PRE = "task:rule";

    /**
     * 任务商品缓存key前缀
     */
     String TASK_GOODS_KEY_PRE = "task:";

     String ADD_GOODS_OR_DISTRIBUTOR_NOTIFY = "task:notify:%s";
}
