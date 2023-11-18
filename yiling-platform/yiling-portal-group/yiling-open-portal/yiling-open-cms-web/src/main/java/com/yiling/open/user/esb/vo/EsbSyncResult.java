package com.yiling.open.user.esb.vo;

import lombok.Data;

/**
 * ESB同步接口返回结果对象
 * 
 * @author xuan.zhou
 * @date 2022/11/25
 */
@Data
public class EsbSyncResult {

    private EsbSyncResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static EsbSyncResult success() {
        return new EsbSyncResult(0, "成功");
    }

    public static EsbSyncResult fail(String message) {
        return new EsbSyncResult(1, message);
    }

    /**
     * 0-成功 1-失败
     */
    private Integer status;

    /**
     * 返回信息
     */
    private String message;
}