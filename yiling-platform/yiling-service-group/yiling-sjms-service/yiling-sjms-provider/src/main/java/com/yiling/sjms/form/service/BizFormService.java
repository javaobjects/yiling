package com.yiling.sjms.form.service;

import com.yiling.sjms.form.dto.request.ApproveFormRequest;
import com.yiling.sjms.form.dto.request.CreateFormRequest;
import com.yiling.sjms.form.dto.request.RejectFormRequest;
import com.yiling.sjms.form.dto.request.SubmitFormRequest;

/**
 * 业务表单 Service
 *
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
public interface BizFormService {

    /**
     * 创建基础表单信息
     *
     * @param request
     * @return java.lang.Long 基础表单信息ID
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    <T extends CreateFormRequest> Long create(T request);

    /**
     * 提交表单
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    <T extends SubmitFormRequest> Boolean submit(T request);

    /**
     * 驳回表单
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    <T extends RejectFormRequest> Boolean reject(T request);

    /**
     * 审批通过表单
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2023/2/24
     **/
    <T extends ApproveFormRequest> Boolean approve(T request);

    /**
     * 删除表单
     *
     * @param id 基础表单ID
     * @param opUserId 操作人ID
     * @return
     */
    Boolean delete(Long id, Long opUserId);
}
