package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Data
public class CheckAgencyDataForm extends BaseForm {

    /**
     * 机构主表id-修改时需要
     */
    @ApiModelProperty("机构主表id-修改时校验需要")
    private Long id;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    @NotNull(message = "机构类型不存在")
    @ApiModelProperty("erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role")
    private Integer supplyChainRole;

    /**
     * 以岭编码
     */
    @ApiModelProperty("以岭编码")
    private String ylCode;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @ApiModelProperty("执业许可证号/社会信用统一代码")
    private String licenseNumber;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String name;

    /**
     * 药品经营许可证编号
     */
    @ApiModelProperty("药品经营许可证编号")
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    @ApiModelProperty("医机构执业许可证")
    private String institutionPracticeLicense;/**
     * 医机构执业许可证
     */
    @ApiModelProperty("CRM编码")
    private String code;
}
