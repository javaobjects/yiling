package com.yiling.dataflow.wash.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryuUnlockSaleRulePageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -3436488791106291210L;

    private Long id;

    private String code;

    private String name;

    private Integer status;

    private Date startUpdate;

    private Date endUpdate;
}
