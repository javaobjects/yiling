package com.yiling.sjms.gb.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GbFormFlowDTO extends GbFormInfoDTO {
    /**
     * 出库终端和商业数据
     */
    private List<GbFlowCompanyRelationDTO> companyRelationList;


    /**
     * 团购单位ID
     */
    private Long customerId;

    /**
     * 团购单位名称
     */
    private String customerName;

    /**
     * 团购月份
     */
    private Date month;


    /**
     * 团购区域：1-国内 2-海外
     */
    private Integer regionType;


    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    private Integer gbType;

    /**
     * 销量计入人工号
     */
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    private String sellerEmpName;

    /**
     * 销量计入人区办ID
     */
    private Long sellerDeptId;

    /**
     * 销量计入人区办名称
     */
    private String sellerDeptName;



}
