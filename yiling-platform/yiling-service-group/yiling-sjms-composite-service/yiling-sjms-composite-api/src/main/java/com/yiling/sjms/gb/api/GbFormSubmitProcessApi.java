package com.yiling.sjms.gb.api;

import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;

/**
 * 团购表单提交流程
 *
 * @author: wei.wang
 * @date: 2022/11/28
 */
public interface GbFormSubmitProcessApi {

    /**
     *提交表单流程
     * @param request
     * @return
     */
    Boolean submitFormProcess(SaveGBInfoRequest request);

    /**
     * 团购取消保存
     * @param request
     * @return
     */
    Boolean cancelFormProcess(SaveGBCancelInfoRequest request);

    /**
     * 团购费用申请保存
     * @param request
     * @return
     */
    Boolean feeApplicationFormProcess(SaveGBCancelInfoRequest request);
}
