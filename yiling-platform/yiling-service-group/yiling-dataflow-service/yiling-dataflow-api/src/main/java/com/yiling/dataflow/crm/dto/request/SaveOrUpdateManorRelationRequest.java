package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateManorRelationRequest extends BaseRequest {
    private Long id;
    /**
     * 辖区表Id
     */
    private Long crmManorId;

    /**
     * 机构编码
     */
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    private String crmEnterpriseName;
    /**
     * 、
     * 机构Crm编码
     */
    private String crmEnterpriseCode;


    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    private String categoryName;


    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新日期
     */
    private Date updateTime;

    private String remark;

    /**
     * 新辖区id(辖区变更流程审批通过后传过来)
     */
    private Long newManorId;

}
