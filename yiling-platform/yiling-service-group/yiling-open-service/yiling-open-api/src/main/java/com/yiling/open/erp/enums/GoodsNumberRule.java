package com.yiling.open.erp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 药品库存规则：0读取库存批次表1读取药品表
 *                       
 * @Filename: OperType.java
 * @Version: 1.0
 * @Author: wanfei.zhang 张万飞
 * @Email: wanfei.zhang@rogrand.com
 *
 */
public enum GoodsNumberRule {
	GOODS_BATCH(0,"库存批次表"),
    GOODS(1,"药品表"),
	;
	
	private Integer code;
	private String message;
	
	private static final Map<Integer,String> map ;
	
	static {
		map = new HashMap<Integer,String>();
		for (GoodsNumberRule type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
	}
	
	private GoodsNumberRule(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public static Map<Integer,String> getMap() {
		return map;
	}
    
    public static String getMessage(Integer code) {
    	if(code == null) {
    		return "";
    	}
    	return map.get(code);
    }
}
