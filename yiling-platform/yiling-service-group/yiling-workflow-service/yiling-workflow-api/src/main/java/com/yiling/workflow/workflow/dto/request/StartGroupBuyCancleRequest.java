package com.yiling.workflow.workflow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 启动流程入参
 * @author: gxl
 * @date: 2022/11/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StartGroupBuyCancleRequest extends BaseRequest {


    private static final long serialVersionUID = -4069398606905285675L;
    /**
     * 编号
     */
    private String gbNo;

    /**
     * 来源团购编号
     */
    private String srcGbNo;


    private Long gbId;
}