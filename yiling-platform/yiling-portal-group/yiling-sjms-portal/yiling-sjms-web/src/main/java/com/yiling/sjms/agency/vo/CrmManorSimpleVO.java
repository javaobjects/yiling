package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmManorSimpleVO extends BaseVO {
    /**
     * 辖区编码
     */
    @ApiModelProperty("辖区编码")
    private String manorNo;

    /**
     * 辖区名称
     */
    @ApiModelProperty("辖区名称")
    private String name;

    /**
     * 机构数量
     */
    @ApiModelProperty("机构数量")
    private Integer agencyNum;

    /**
     * 品类数量
     */
    @ApiModelProperty("品类数量")
    private Integer categoryNum;
}
