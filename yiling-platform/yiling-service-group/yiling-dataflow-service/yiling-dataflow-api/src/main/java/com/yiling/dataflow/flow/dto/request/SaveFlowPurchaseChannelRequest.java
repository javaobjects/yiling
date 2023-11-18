package com.yiling.dataflow.flow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowPurchaseChannelRequest
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowPurchaseChannelRequest extends BaseRequest {
    private Long id;

    /**
     * 机构编码
     */
    private Long crmOrgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 市
     */
    private String city;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 区
     */
    private String region;

    /**
     * 区代码
     */
    private String regionCode;

    /**
     * 采购渠道机构编码
     */
    private Long crmPurchaseOrgId;

    /**
     * 采购渠道机构名称
     */
    private String purchaseOrgName;
}
