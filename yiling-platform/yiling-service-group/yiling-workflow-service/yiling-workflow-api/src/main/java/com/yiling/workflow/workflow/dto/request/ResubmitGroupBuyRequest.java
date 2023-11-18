package com.yiling.workflow.workflow.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 驳回后重新提交
 * @author: gxl
 * @date: 2023/1/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ResubmitGroupBuyRequest extends BaseRequest {
    private static final long serialVersionUID = -2659068453508060637L;

    private String provinceName;

    /**
     * 提交人部门id
     */
    private Long orgId;
    /**
     * 出货终端编码
     */
    private List<String> terminalCompanyCode;

    private String gbNo;

}