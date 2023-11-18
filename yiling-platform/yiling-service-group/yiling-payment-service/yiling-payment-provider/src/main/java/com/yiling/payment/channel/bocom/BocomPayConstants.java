package com.yiling.payment.channel.bocom;

/**
 * @author zhigang.guo
 * @date: 2023/5/8
 */
public final class BocomPayConstants {

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String CREATE_ORDER_URL_V1 = "/api/pmssMpng/MPNG210005/v1";

    public static final String CLOSE_ORDER_URL_V1 = "/api/pmssMpng/MPNG020705/v1";

    public static final String QUERY_ORDER_URL_V1 = "/api/pmssMpng/MPNG020702/v1";

    public static final String REFUND_ORDER_URL_V1 = "/api/pmssMpng/MPNG020701/v1";

    public static final String REFUND_QUERY_ORDER_URL_V1  = "/api/pmssMpng/MPNG020703/v1";

    public static final String  QUERY_OPEN_ID_URL_V1 = "/api/pmssMpng/MPNG210006/v1";

    public static final String  SCAN_CODE_URL_V1 = "/api/pmssMpng/MPNG210001/v1";




    /**
     * 支付成功状态码
     */
    public static final String SUCCESS = "S";

    public static final String PROCESSING = "P";

    public static final String FAILED = "F";

    public static final String QUERYSUCCESS = "SUCCESS";


}
