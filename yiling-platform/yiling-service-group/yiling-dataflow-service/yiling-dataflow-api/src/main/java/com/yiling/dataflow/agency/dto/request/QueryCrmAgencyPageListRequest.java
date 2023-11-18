package com.yiling.dataflow.agency.dto.request;

import java.util.Date;
import java.util.List;

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
public class QueryCrmAgencyPageListRequest extends QueryPageListRequest {
    /**
     * 机构编码
     */
    private String id;
    private String code;

    /**
     * 以岭编码
     */
    private String ylCode;

    /**
     * 机构名称
     */
    private String name;

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

    /**
     * 状态 0全部 1有效 2失效
     */
    private String businessCode;

    /**
     * 创建时间-开始
     */
    private Date beginTime;

    /**
     * 创建时间-结束
     */
    private Date endTime;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    private Integer supplyChainRole;

    /**
     * 简称
     */
    private String shortName;
    /**
     * 权限控制的机构ID集合 .size==0为所有数据权限
     */
    @Deprecated
    private List<Long> longs;

    /**
     * OrgPartDatascopeBO
     */
    SjmsUserDatascopeBO sjmsUserDatascopeBO;
}
