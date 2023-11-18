package com.yiling.dataflow.order.util;

/**
 * @author: houjie.sun
 * @date: 2021/8/30
 */
public class Constants {

    /**
     * MQ消费成功
     */
    public static String MQ_LISTENER_SUCCESS = "MQ消费成功, MsgId:{0}, Topic:{1}, Tag:{2}，Keys:{3}, Body:{4}";

    /**
     * MQ消费失败
     */
    public static String MQ_LISTENER_FAILE = "MQ消费失败, MsgId:{0}, Topic:{1}, Tag:{2}，Keys:{3}, Body:{4}";

}
