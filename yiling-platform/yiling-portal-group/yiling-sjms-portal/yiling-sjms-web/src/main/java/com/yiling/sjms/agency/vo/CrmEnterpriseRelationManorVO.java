package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseRelationManorVO extends BaseVO {
    /**
     * 辖区表Id
     */
    @ApiModelProperty("辖区表Id")
    private Long crmManorId;

    /**
     * 机构编码
     */
    @ApiModelProperty("机构编码")
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String crmEnterpriseName;
    /**、
     * 机构Crm编码
     */
    private String crmEnterpriseCode;


    /**
     * 品类ID
     */
    @ApiModelProperty("品类ID")
    private Long categoryId;

    /**
     * 品类名称
     */
    @ApiModelProperty("品类名称")
    private String categoryName;
}
