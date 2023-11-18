package com.yiling.dataflow.flow.dto.request;

import java.util.List;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryFlowPurchaseChannelRequest
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseChannelRequest extends QueryPageListRequest {
    /**
     * 机构编码
     */
    private String orgName;

    /**
     * 机构编码
     */
    private Long crmOrgId;

    /**
     * 省代码
     */
    private String provinceCode;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 区代码
     */
    private String regionCode;

    /**
     * 采购渠道机构名称
     */
    private String purchaseOrgName;

    /**
     * 采购渠道机构编码
     */
    private Long crmPurchaseOrgId;

    /**
     * 权限对象
     */
    private SjmsUserDatascopeBO userDatascopeBO;
}
