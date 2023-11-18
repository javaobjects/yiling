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
public class QueryCrmEnterpriseRelationShipPageListRequest extends QueryPageListRequest {

    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 业务代表姓名
     */
    private String representativeName;

    /**
     * 业务代表工号
     */
    private String representativeCode;

    /**
     * 机构id合集
     */
    private List<Long> crmEnterpriseIds;

    /**
     * 机构名称
     */
    private String customerName;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    private Integer supplyChainRole;

    /**
     * 最后操作时间-开始
     */
    private Date beginTime;

    /**
     * 最后操作时间-结束
     */
    private Date endTime;
    /**
     * 岗位编码
     */
    private Long postCode;
    /**
     * 权限控制crmEnterIds
     */
    @Deprecated
    private List<Long> longs;
    /**
     * OrgPartDatascopeBO
     */
    SjmsUserDatascopeBO sjmsUserDatascopeBO;

    /**
     * 辖区ID
     */
    private Long manorId;
    /**
     * 品类ID
     */
    private Long categoryId;
    /**
     * 机构ID
     */
    private Long crmEnterpriseId;
}
