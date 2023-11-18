package com.yiling.dataflow.crm.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveCrmEnterpriseRelationPinchRunnerRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 三者关系id列表
     */
    private List<Long> enterpriseCersIdList;

}
