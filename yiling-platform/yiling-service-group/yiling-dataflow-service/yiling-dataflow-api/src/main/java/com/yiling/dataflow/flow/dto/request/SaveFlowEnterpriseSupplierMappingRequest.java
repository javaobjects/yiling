package com.yiling.dataflow.flow.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowEnterpriseSupplierMappingRequest
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowEnterpriseSupplierMappingRequest extends BaseRequest {
    private Long id;

    /**
     * 流向客户名称
     */
    private String flowSupplierName;

    /**
     * 标准机构编码
     */
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    private String orgName;

    /**
     * 标准机构社会信用代码
     */
    private String orgLicenseNumber;
    /**
     * 经销商名称
     */
    private String enterpriseName;

    /**
     * 经销商编码
     */
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 省份
     */
    private String province;

    /**
     * 最后上传时间
     */
    private Date lastUploadTime;

}
