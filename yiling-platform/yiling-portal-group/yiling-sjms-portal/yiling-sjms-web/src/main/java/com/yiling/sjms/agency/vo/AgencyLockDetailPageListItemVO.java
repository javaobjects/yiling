package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgencyLockDetailPageListItemVO extends BaseVO {

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty("供应链角色：1-商业公司 2-医院 3-零售药店。数据字典：erp_crm_supply_chain_role")
    private Integer supplyChainRole;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("机构名称")
    private String name;

    /**
     * 新增三者关系数量
     */
    @ApiModelProperty("新增三者关系数量")
    private Integer relationShipCount;

    /**
     * 数据归档：1-开启 2-关闭
     */
    @ApiModelProperty("数据归档：1-开启 2-关闭")
    private Integer archiveStatus;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
