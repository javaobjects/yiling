package com.yiling.user.esb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存或更新ESB人员信息 Request
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateEsbJobRequest extends BaseRequest {

    private static final long serialVersionUID = -8824653833216483392L;

    /**
     * 部门岗ID
     */
    private Long jobDeptId;

    /**
     * 部门刚名称
     */
    private String jobDeptName;

    /**
     * 标准岗ID
     */
    private Long jobId;

    /**
     * 标准岗名称
     */
    private String jobName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门形成
     */
    private String deptName;

    /**
     * HRID
     */
    private String sourceDetailId;

    /**
     * 编制数
     */
    private String postPrepare;

    /**
     * 是否失效：0-正常，其他失效
     */
    private String state;
}
