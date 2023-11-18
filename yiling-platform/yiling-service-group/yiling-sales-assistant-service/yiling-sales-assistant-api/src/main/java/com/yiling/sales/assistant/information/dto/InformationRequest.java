package com.yiling.sales.assistant.information.dto;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手-信息反馈Request
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InformationRequest extends BaseRequest {

    /**
     * 信息描述
     */
    private String content;


}
