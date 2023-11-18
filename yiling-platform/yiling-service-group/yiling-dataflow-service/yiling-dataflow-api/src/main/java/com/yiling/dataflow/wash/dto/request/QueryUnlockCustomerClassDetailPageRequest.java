package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryUnlockCustomerClassDetailPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -8841138801148704112L;

    private Long crmEnterpriseId;

    private String customerName;

    private Integer classFlag;

    private Integer customerClassification;

    private Date startOpTime;

    private Date endOpTime;
}
