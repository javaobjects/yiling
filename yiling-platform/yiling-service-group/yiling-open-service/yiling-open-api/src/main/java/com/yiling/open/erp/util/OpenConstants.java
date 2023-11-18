package com.yiling.open.erp.util;

/**
 * 公用常量类
 *                       
 * @Filename: OpenConstants.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu.liu@rograndec.com
 *
 */
public abstract class OpenConstants {

    /**http请求参数app_key **/
    public static final String APP_KEY               = "app_key";

    /**http请求参数format **/
    public static final String FORMAT                = "format";

    /**http请求参数method **/
    public static final String METHOD                = "method";

    /**http请求参数timestamp **/
    public static final String TIMESTAMP             = "timestamp";

    /**http请求参数v **/
    public static final String VERSION               = "v";

    /**http请求参数sign **/
    public static final String SIGN                  = "sign";

    /**http请求参数sign_method **/
    public static final String SIGN_METHOD           = "sign_method";

    /**http请求参数partner_id **/
    public static final String PARTNER_ID            = "partner_id";

    /**http请求参数access_token **/
    public static final String ACCESS_TOKEN               = "access_token";

    /** TOP默认时间格式 **/
    public static final String DATE_TIME_FORMAT      = "yyyy-MM-dd HH:mm:ss";

    /** TOP Date默认时区 **/
    public static final String DATE_TIMEZONE         = "GMT+8";

    /** UTF-8字符集 **/
    public static final String CHARSET_UTF8          = "UTF-8";

    /** GBK字符集 **/
    public static final String CHARSET_GBK           = "GBK";

    /** OPEN JSON 应格式 */
    public static final String JSON_FORMAT           = "json";
    
    /** OPEN xml 应格式 */
    public static final String XML_FORMAT           = "xml";

    /** MD5签名方式 */
    public static final String MD5_SIGN_METHOD       = "md5";
    /** HMAC签名方式 */
    public static final String HMAC_SIGN_METHOD      = "hmac";

    public static final String DATA_PARAM = "data_param";

    /** 授权地址 */
    public static final String PRODUCT_CONTAINER_URL = "http://open.api.mypharma.com/rest";

    /** SDK版本号 */
    public static final String SDK_VERSION           = "1.0";

    /** 返回的错误码 */
    public static final String ERROR_RESPONSE        = "error";
    public static final String ERROR_CODE            = "code";
    public static final String ERROR_MSG             = "msg";
    public static final String ERROR_SUB_CODE        = "sub_code";
    public static final String ERROR_SUB_MSG         = "sub_msg";

    public static final String SU_ID = "su_id";
    public static final String ERP_PRIMARY_KEY="erp_primary_key";
    public static final String SUCCESS="success";
    public static final String FAILURE="failure";

    public static final String SPLITE_SYMBOL="$$$";
    public static final String SPLITE_SYMBOL_FALG="\\$\\$\\$";
    public static final String ORDER_ID="order_id";
    public static final String ORDER_BILL_ID="order_bill_id";
    public static final String ORDER_RETURN_ID="return_id";
    public static final String ERP_SN="erp_sn";

    /** 订单来源 类型是“大运河采购”的公司 */
    public static final String[] YILING_ENTERPRISE_NAMES = {"河北大运河医药物流有限公司", "泰州市京东医药有限责任公司"};

    public static final String status_flag = "status";
    public static final String task_flag = "task";
}
