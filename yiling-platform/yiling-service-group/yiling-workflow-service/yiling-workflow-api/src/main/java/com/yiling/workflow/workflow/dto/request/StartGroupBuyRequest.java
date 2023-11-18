package com.yiling.workflow.workflow.dto.request;

import java.util.List;

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
public class StartGroupBuyRequest extends BaseRequest {

    private static final long serialVersionUID = -7639265294176298022L;
    /**
     * 流程定义id
     */
    private String procDefId;

    /**
     * 业务系统唯一标识（比如团购编号） 必填
     */
    private String businessKey;


    private String provinceName;

    /**
     * 提交人用户 emp_id 工号
     */
    private String startUserId;
    /**
     * 提交人部门id
     */
    private Long orgId;
    /**
     * 出货终端编码
     */
    private List<String> terminalCompanyCode;

    /**
     * 是否政府团购
     */
    private Boolean governmentBuy;

    /**
     * 团购id
     */
    private Long gbId;
}