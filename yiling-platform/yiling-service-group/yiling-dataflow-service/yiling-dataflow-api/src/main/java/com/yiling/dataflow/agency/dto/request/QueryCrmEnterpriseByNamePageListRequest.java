package com.yiling.dataflow.agency.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseByNamePageListRequest extends QueryPageListRequest {

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
     * 是否过滤掉supply_chain_role=0的数据
     */
    private Boolean filterBySupplyChainRole;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;

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

    SjmsUserDatascopeBO sjmsUserDatascopeBO;
    /**
     * 备份表
     */
    private String tableSuffix;
}
