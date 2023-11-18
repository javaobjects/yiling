package com.yiling.dataflow.crm.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2023/02/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ChangeRelationShipRequest extends BaseRequest {

    private static final long serialVersionUID = -776991669622366317L;
    private List<ChangeRelationShipDetailRequest> requestList;
}
