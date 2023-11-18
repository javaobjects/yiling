package com.yiling.dataflow.wash.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseUnlockSalePageListRequest extends QueryPageListRequest {

    /**
     * 机构基础信息id
     */
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    private Integer supplyChainRole;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**------   业务字段信息 -------*/
    private Long businessRuleId;

    private Long customerRuleId;

}
