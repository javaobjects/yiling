package com.yiling.search.flow.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import com.yiling.framework.common.base.EsBaseEntity;

import lombok.Data;

/**
 * @author shichen
 * @类名 EsFlowEnterpriseCustomerMappingEntity
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
@Document(indexName = "#{@flowEnterpriseCustomerMappingIndex}",createIndex = false)
public class EsFlowEnterpriseCustomerMappingEntity extends EsBaseEntity {
    /**
     * 流向客户名称
     */
    @Field(name = "flow_customer_name")
    private String flowCustomerName;

    /**
     * 标准机构编码
     */
    @Field(name = "crm_org_id")
    private Long crmOrgId;

    /**
     * 标准机构名称
     */
    @Field(name = "org_name")
    private String orgName;

    /**
     * 标准机构社会信用代码
     */
    @Field(name = "org_license_number")
    private String orgLicenseNumber;
    /**
     * 经销商名称
     */
    @Field(name = "enterprise_name")
    private String enterpriseName;

    /**
     * 经销商编码
     */
    @Field(name = "crm_enterprise_id")
    private Long crmEnterpriseId;

    /**
     * 省份代码
     */
    @Field(name = "province_code")
    private String provinceCode;

    /**
     * 省份
     */
    @Field(name = "province")
    private String province;

    /**
     * 最后上传时间
     */
    @Field(name = "last_upload_time")
    private String lastUploadTime;

    /**
     * 推荐客户crmId
     */
    @Field(name = "recommend_org_crm_id")
    private Long recommendOrgCrmId;

    /**
     * 推荐客户名称
     */
    @Field(name = "recommend_org_name")
    private String recommendOrgName;

    /**
     * 推荐分数
     */
    @Field(name = "recommend_score")
    private BigDecimal recommendScore;

    /**
     * 是否删除：0-否 1-是
     */
    @Field(name = "del_flag")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @Field(name = "create_time")
    private String createTime;

    /**
     * 创建人
     */
    @Field(name = "create_user")
    private Long createUser;

    /**
     * 修改时间
     */
    @Field(name = "update_time")
    private String updateTime;

    /**
     * 修改人
     */
    @Field(name = "update_user")
    private Long updateUser;
}
