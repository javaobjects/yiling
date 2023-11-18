package com.yiling.dataflow.crm.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseRelationPinchRunnerPageListRequest extends QueryPageListRequest {

    /**
     * 数据权限
     */
    private SjmsUserDatascopeBO userDatascopeBO;

    /**
     * 销量计入主管工号
     */
    private String businessSuperiorCode;

    /**
     * 岗位代码
     */
    private String postCode;

    /**
     * 机构id
     */
    private Long crmEnterpriseId;

    /**
     * 机构id合集
     */
    private List<Long> crmEnterpriseIdList;

    /**
     * erp供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    private Integer crmSupplyChainRole;


    /**
     * 品种分类ID
     */
    private Long categoryId;

    /**
     * 辖区ID
     */
    private Long manorId;

    /**
     * 最后操作时间开始
     */
    private Date opTimeStart;

    /**
     * 最后操作时间结束
     */
    private Date opTimeEnd;

}
