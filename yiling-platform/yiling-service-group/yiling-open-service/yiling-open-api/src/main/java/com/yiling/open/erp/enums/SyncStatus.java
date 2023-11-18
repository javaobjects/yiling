package com.yiling.open.erp.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 同步状态，0未同步，1正在同步 2同步成功 3同步失败
 *                       
 * @Filename: SyncStatus.java
 * @Version: 1.0
 * @Author: wanfei.zhang 张万飞
 * @Email: wanfei.zhang@rogrand.com
 *
 */
public enum SyncStatus {
	UNSYNC(0,"未同步"),
    SYNCING(1,"正在同步"),
    SUCCESS(2,"同步成功"),
    FAIL(3,"同步失败"),
    RETRY(10,"重新同步"),
    UNMATCH(21,"未分配子公司信息")
	;
	
	private Integer code;
	private String message;
	
	private static final Map<Integer,String> map ;
	
	static {
		map = new HashMap<Integer,String>();
		for (SyncStatus type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
	}

	public static SyncStatus getFromCode(Integer code) {
		for(SyncStatus e: SyncStatus.values()) {
			if(e.getCode().equals(code)) {
				return e;
			}
		}
		return null;
	}
	
	private SyncStatus(Integer code, String message) {
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
    	return map.get(code);
    }
}
