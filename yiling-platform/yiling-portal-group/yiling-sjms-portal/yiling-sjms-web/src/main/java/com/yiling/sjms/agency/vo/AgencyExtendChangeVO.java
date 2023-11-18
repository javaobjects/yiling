package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/2/23
 */
@Data
public class AgencyExtendChangeVO extends BaseVO {
    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty(value = "供应链角色 字典crm_supply_chain_role")
    private Integer supplyChainRole;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty(value = "机构名称")
    private String name;

    /**
     * 变更项目
     */
    @ApiModelProperty(value = "变更项目 字典crm_agency_change_item 多个用,号分隔 ")
    private String changeItem;

    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 数据归档：1-开启 2-关闭
     */
    @ApiModelProperty(value = "数据归档：1-开启 2-关闭")
    private Integer archiveStatus;
}