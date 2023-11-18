package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCrmGoodsTagRelationPageRequest
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmGoodsTagRelationPageRequest extends QueryPageListRequest {

    private Long tagId;

    private Long crmGoodsCode;

    private String crmGoodsName;
}
