package com.yiling.export.export.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ExportCrmManorRelationBO {
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
    /**、
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

    private Integer delFlag;

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
}
