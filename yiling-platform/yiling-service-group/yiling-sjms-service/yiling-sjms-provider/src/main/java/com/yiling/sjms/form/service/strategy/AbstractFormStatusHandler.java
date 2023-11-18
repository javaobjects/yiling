package com.yiling.sjms.form.service.strategy;

import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;

/**
 * @author: gxl
 * @date: 2023/2/27
 */
public abstract class AbstractFormStatusHandler{

    /**
     * 表单类型
     * @return
     */
    public abstract Integer getCurrentType();

    /**
     * 接mq通知审批通过
     * @param request
     * @return
     */
    public  abstract Boolean approve(UpdateGBFormInfoRequest request);

    /**
     * 接mq通知提交成功
     * @param request
     * @return
     */
    public  abstract Boolean submit(UpdateGBFormInfoRequest request);

    /**
     * 接mq通知重新发起
     * @param request
     * @return
     */
    public  abstract Boolean reSubmit(UpdateGBFormInfoRequest request);

    /**
     * 接mq通知驳回
     * @param request
     * @return
     */
    public  abstract Boolean reject(UpdateGBFormInfoRequest request);
}