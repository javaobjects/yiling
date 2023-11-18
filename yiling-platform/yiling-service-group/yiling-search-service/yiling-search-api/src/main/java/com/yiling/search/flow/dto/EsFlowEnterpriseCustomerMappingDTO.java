package com.yiling.search.flow.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseCustomerMappingDTO
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
public class EsFlowEnterpriseCustomerMappingDTO extends BaseDTO {
    /**
     * 流向客户名称
     */
    private String flowCustomerName;

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

    /**
     * 推荐客户crmId
     */
    private Long recommendOrgCrmId;

    /**
     * 推荐客户名称
     */
    private String recommendOrgName;

    /**
     * 推荐分数
     */
    private BigDecimal recommendScore;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;
}
