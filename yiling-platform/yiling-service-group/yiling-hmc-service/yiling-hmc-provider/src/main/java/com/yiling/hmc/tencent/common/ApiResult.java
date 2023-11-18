package com.yiling.hmc.tencent.common;

import cn.hutool.json.JSONUtil;
import com.yiling.framework.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * API Result
 *
 * @author: fan.shen
 * @date: 2023/4/28
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> implements java.io.Serializable {

    private static final long serialVersionUID = 7944789822838179571L;

    /**
     * 请求处理的结果，OK 表示处理成功，FAIL 表示失败
     */
    private String ActionStatus;

    /**
     * 错误信息
     */
    private String ErrorInfo;

    /**
     * 错误码，0表示成功，非0表示失败
     */
    private Integer ErrorCode;

    private T data;

    public boolean success() {
        if (this.ErrorCode != null && this.ErrorCode == 0) {
            return true;
        } else {
            log.error("API调用失败：code={}, msg={}, data={}", this.ErrorCode, this.ErrorInfo, JSONUtil.toJsonStr(this.data));
            throw new ServiceException("腾讯API调用失败：" + this.getErrorInfo());
        }
    }

}
