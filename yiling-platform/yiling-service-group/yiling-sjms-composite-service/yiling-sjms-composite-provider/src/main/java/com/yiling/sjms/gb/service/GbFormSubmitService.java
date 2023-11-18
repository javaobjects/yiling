package com.yiling.sjms.gb.service;

import com.yiling.sjms.gb.dto.request.SaveGBCancelInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;

/**
 * 团购表单提交
 */
public interface GbFormSubmitService {
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
     * 费用申请保存
     * @param request
     * @return
     */
    Boolean feeApplicationFormProcess(SaveGBCancelInfoRequest request);
}
