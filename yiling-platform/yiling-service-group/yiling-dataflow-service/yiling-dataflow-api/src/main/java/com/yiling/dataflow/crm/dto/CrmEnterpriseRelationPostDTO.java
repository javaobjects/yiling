package com.yiling.dataflow.crm.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseRelationPostDTO extends BaseDTO {

    private Long crmEnterpriseId;

     /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 产品组id
     */
    private Long productGroupId;

    /**
     * 辖区id
     */
    private Long manorId;

    /**
     * 分类id
     */
    private Long categoryId;
}
