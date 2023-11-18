package com.yiling.dataflow.wash.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerClassificationRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -8305087869217447337L;

    private List<Long> idList;

    private Integer customerClassification;

    private String remark;
}
