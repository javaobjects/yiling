package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryUnlockCustomerClassRulePageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -5045787575106108137L;

    private Long ruleId;

    private String remark;

    private Integer customerClassification;

    private Integer status;

    private Date startOpTime;

    private Date endOpTime;
}
