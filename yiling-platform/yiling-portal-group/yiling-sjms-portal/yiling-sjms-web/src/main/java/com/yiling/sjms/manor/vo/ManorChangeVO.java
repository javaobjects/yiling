package com.yiling.sjms.manor.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 辖区变更详情反显接口
 * @author: gxl
 * @date: 2023/5/15
 */
@Data
@Accessors(chain = true)
public class ManorChangeVO extends BaseVO {

    /**
     * 医院名称
     */
    @ApiModelProperty(value = "医院名称")
    private String enterpriseName;

    @ApiModelProperty(value = "社会统一信用代码")
    private String licenseNumber;

    /**
     * 供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    @ApiModelProperty(value = "供应链角色")
    private Integer supplyChainRole;

    @ApiModelProperty(value = "省")
    private String provinceName;

    @ApiModelProperty(value = "市")
    private String cityName;
    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    @ApiModelProperty(value = "区")
    private String regionName;

    @ApiModelProperty(value = "辖区关系")
    List<ManorRelationVO> relation;

    @ApiModelProperty(value = "备注")
    private String remark;
}