package com.yiling.open.erp.entity;


import java.io.Serializable;

/**  
 * 表sys_parameter对应的Model信息
 *
 *@author wanfei.zhang
 *@date 2017-03-22 13:46:48
 */
public class SysParameterDO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** 订单发货监控*/
    public final static String OrderSendMonitor = "orderSendPushMonitor";
    
    /** 订单推送监控*/
    public final static String OrderPushMonitor = "orderPushMonitor";
    
    /** 客户统计监控 */
    public final static String StatCustomerMonitor = "statCustomerMonitor";
    
    /** 商品统计监控 */
    public final static String StatGoodsMonitor = "statGoodsMonitor";
    
    /** 商品批次统计监控 */
    public final static String StatGoodsBatchMonitor = "statGoodsBatchMonitor";
    
    /** 订单发货单统计监控 */
    public final static String StatOrderSendMonitor = "statOrderSendMonitor";
    
    /** 客户定价统计监控 */
    public final static String StatCustomerPriceMonitor = "statCustomerPriceMonitor";
    
    /** 心跳超时发送邮件通知的商业公司配置 */
    public final static String HeartEmailNotice = "heartEmailNotice";
    
    /** 监控未同步的时间配置 */
    public final static String MonitorSyncTime = "monitorSyncTime";

    /** 刷新库存 */
    public final static String REFRESHERPINVENTORY = "refreshErpInventory";
    
    /** 同一商户同一个客户同一个商品订单监控 */
    public final static String OrderGoodsMonitor = "orderGoodsMonitor";
    
    /** 务分组数据监控 */
    public final static String ShopCustomerTypeMonitor = "shopCustomerTypeMonitor";

    /** 数据监控 */
    public final static String FlowDataMonitor = "flowDataMonitor";
    
    /** 客户证照号匹配正则表达式 */
    public final static String RegularMatchingCustomerCert = "regularMatchingCustomerCert";
    
    /** 浙江英特接口配置 */
    public final static String YtInterFaceConfigure = "ytInterFaceConfigure";

    /** 金华英特接口配置 */
    public final static String JhYtInterFaceConfigure = "jhYtInterFaceConfigure";
    
    /** 澳洋接口配置 */
    public final static String OyInterFaceConfigure = "oyInterFaceConfigure";
    
    /** 白洋接口配置 */
    public final static String LyInterFaceConfigure = "lyInterFaceConfigure";
    
    /** 广药一致接口配置 */
    public final static String GzyzInterFaceConfigure = "gzyzInterFaceConfigure";
    
    /** 国药重庆接口配置 */
    public final static String SinopharmcqInterFaceConfigure = "sinopharmcqInterFaceConfigure";
    
    /** 国药天津接口配置 */
    public final static String GktjInterFaceConfigure = "gktjInterFaceConfigure";
    
    /** 九州通本溪接口配置 */
    public final static String JztzxInterFaceConfigure = "jztzxInterFaceConfigure";
    
    /** 杭州万丰接口配置 */
    public final static String HzwfInterFaceConfigure = "hzwfInterFaceConfigure";
    
    /** 国药湖北接口配置 */
    public final static String GkhbInterFaceConfigure = "gkhbInterFaceConfigure";
    
    /** 国药山东接口配置 */
    public final static String GksdInterFaceConfigure = "gksdInterFaceConfigure";
    
    /** 国控云南接口配置 */
    public final static String GkynInterFaceConfigure = "gkynInterFaceConfigure";
    
    /** 广药大众接口配置 */
    public final static String GydzInterFaceConfigure = "gydzInterFaceConfigure";
    
    /** 广州振康接口配置 */
    public final static String GzzkInterFaceConfigure = "gzzkInterFaceConfigure";

    /** 朔州尔康接口配置 */
    public final static String SzekInterFaceConfigure = "szekInterFaceConfigure";

    /** 华源康信接口配置 */
    public final static String HykxInterFaceConfigure = "hykxInterFaceConfigure";

    /** 同步接口调用监控 */
    public final static String InterfaceMonitor = "interfaceMonitor";

    /** 安徽华源股份接口配置 */
    public final static String AhshyInterFaceConfigure = "ahshyInterFaceConfigure";

    /** 定时清理任务配置 */
    public final static String TimingCleanerConfigure = "timingCleanerConfigure";


    private Integer  id;
    		
    private String  paramKey;
    		
    private String  paramValue1;
    		
    private String  paramValue2;
    		
    private String  paramValue3;
    		
    private String  paramValue4;
    		
    private String  paramValue5;
    
    private String  paramValue6;
    
    private String  paramValue7;
    
    private String  paramValue8;
    
    private String  paramValue9;
    
    private String  paramValue10;
    
    private String  remark;
    
    public String getParamValue6() {
		return paramValue6;
	}

	public String getParamValue7() {
		return paramValue7;
	}

	public String getParamValue8() {
		return paramValue8;
	}

	public String getParamValue9() {
		return paramValue9;
	}

	public String getParamValue10() {
		return paramValue10;
	}

	public void setParamValue6(String paramValue6) {
		this.paramValue6 = paramValue6;
	}

	public void setParamValue7(String paramValue7) {
		this.paramValue7 = paramValue7;
	}

	public void setParamValue8(String paramValue8) {
		this.paramValue8 = paramValue8;
	}

	public void setParamValue9(String paramValue9) {
		this.paramValue9 = paramValue9;
	}

	public void setParamValue10(String paramValue10) {
		this.paramValue10 = paramValue10;
	}

	public Integer  getId() {
        return id;
    }
    
    public void setId(Integer  id) {
        this.id = id;
    }
        		
    public String  getParamKey() {
        return paramKey;
    }
    
    public void setParamKey(String  paramKey) {
        this.paramKey = paramKey;
    }
        		
    public String  getParamValue1() {
        return paramValue1;
    }
    
    public void setParamValue1(String  paramValue1) {
        this.paramValue1 = paramValue1;
    }
        		
    public String  getParamValue2() {
        return paramValue2;
    }
    
    public void setParamValue2(String  paramValue2) {
        this.paramValue2 = paramValue2;
    }
        		
    public String  getParamValue3() {
        return paramValue3;
    }
    
    public void setParamValue3(String  paramValue3) {
        this.paramValue3 = paramValue3;
    }
        		
    public String  getParamValue4() {
        return paramValue4;
    }
    
    public void setParamValue4(String  paramValue4) {
        this.paramValue4 = paramValue4;
    }
        		
    public String  getParamValue5() {
        return paramValue5;
    }
    
    public void setParamValue5(String  paramValue5) {
        this.paramValue5 = paramValue5;
    }
        		
    public String  getRemark() {
        return remark;
    }
    
    public void setRemark(String  remark) {
        this.remark = remark;
    }
        		
}