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
public class QueryUnlockCustomerClassDetailCountRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -8495598396397219701L;

    private Long crmEnterpriseId;

    private String customerName;

    private Integer classFlag;

    private Integer customerClassification;

    private Date startOpTime;

    private Date endOpTime;
}
