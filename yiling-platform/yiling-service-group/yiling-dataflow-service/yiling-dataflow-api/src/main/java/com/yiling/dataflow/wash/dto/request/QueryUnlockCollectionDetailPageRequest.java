package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockCollectionDetailPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -7619564734032352028L;

    private Long crmGoodsCode;

    private Date startOpTime;

    private Date endOpTime;
}
