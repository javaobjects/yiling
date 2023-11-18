package com.yiling.dataflow.crm.dto;


import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 辖区机构品类关系
 * </p>
 *
 * @author xueli.ji
 * @date 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseRelationManorDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 辖区表Id
     */
    private Long crmManorId;

    /**
     * 辖区编码
     */
    private String manorNo;
    /**
     * 辖区名称
     */
    private String manorName;
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
