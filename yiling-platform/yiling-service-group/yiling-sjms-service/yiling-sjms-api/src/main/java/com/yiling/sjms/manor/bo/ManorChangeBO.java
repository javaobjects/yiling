package com.yiling.sjms.manor.bo;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.sjms.manor.dto.ManorRelationDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 辖区变更详情反显接口
 * @author: gxl
 * @date: 2023/5/15
 */
@Data
@Accessors(chain = true)
public class ManorChangeBO extends BaseDTO {

    private static final long serialVersionUID = 2308466127129951170L;
    /**
     * 医院名称
     */
    private String enterpriseName;

    private String licenseNumber;

    /**
     * 供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    private Integer supplyChainRole;

    private String provinceName;

    private String cityName;

    private String regionName;
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

    List<ManorRelationDTO> relation;

    private String remark;
}