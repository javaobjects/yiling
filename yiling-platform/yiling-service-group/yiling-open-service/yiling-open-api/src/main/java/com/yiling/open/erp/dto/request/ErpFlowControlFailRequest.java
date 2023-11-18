package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpFlowControlFailRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 同步状态：0-未同步 1-正在同步 2-同步成功 3-同步失败
     */
    private Integer syncStatus;

    /**
     * 同步信息
     */
    private String syncMsg;

    /**
     * 成功数量
     */
    private Integer successNumber;

    /**
     * 失败数量
     */
    private Integer failedNumber;

}
