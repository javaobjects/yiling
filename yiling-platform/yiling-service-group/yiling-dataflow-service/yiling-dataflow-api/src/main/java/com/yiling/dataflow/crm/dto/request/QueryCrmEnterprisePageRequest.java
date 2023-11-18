package com.yiling.dataflow.crm.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterprisePageRequest extends QueryPageListRequest {

    /**
     * 企业名称
     */
    private String likeName;

    /**
     * 1-经销商 2-终端医院 3-终端药店
     */
    private List<Integer> roleIds;

    private boolean flowGoodsPlansFlag;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;
}
