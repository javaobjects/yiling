package com.yiling.open.erp.util;

/**
 * @author: houjie.sun
 * @date: 2021/8/30
 */
public class ErpConstants {

    /**
     * 以岭名称
     */
    public static final String YI_LING = "以岭";

    /**
     * MQ消费成功
     */
    public static String MQ_LISTENER_SUCCESS = "MQ消费成功, MsgId:{0}, Topic:{1}, Tag:{2}, Keys:{3}, Body:{4}";

    /**
     * MQ消费失败
     */
    public static String MQ_LISTENER_FAILE = "MQ消费失败, MsgId:{0}, Topic:{1}, Tag:{2}, Keys:{3}, Body:{4}";

    /**
     * 当月无销售的企业，Redis缓存key
     */
    public static final String erp_flow_sale_Statistics = "erp_flow_sale_Statistics";

    /**
     * 缓存5分钟的销售流线ID集合key
     */
    public static final String FLOW_SALE_ID_LIST_KEY = "flow_sale_id_list_key";
}
