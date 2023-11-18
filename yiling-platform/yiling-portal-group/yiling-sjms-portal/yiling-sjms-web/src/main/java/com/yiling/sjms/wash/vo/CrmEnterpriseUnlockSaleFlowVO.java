package com.yiling.sjms.wash.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.sjms.agency.vo.EnterpriseDisableVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseUnlockSaleFlowVO extends BaseVO {

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    @ApiModelProperty("供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("crm系统对应客户名称")
    private String name;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;
    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;
    /**
     * 所属区区域名称
     */
    @ApiModelProperty("所属区区域名称")
    private String regionName;

    @ApiModelProperty("所属区区域名称")
    private EnterpriseDisableVO enterpriseDisableVO;

}
