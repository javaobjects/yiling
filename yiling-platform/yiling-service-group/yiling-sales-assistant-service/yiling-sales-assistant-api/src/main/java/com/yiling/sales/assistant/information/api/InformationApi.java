package com.yiling.sales.assistant.information.api;

import com.yiling.sales.assistant.information.dto.InformationRequest;

/**
 * 信息反馈API
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
public interface InformationApi {

    /**
     * 新增信息
     * @param request
     * @return
     */
    boolean saveInformation(InformationRequest request);

}
