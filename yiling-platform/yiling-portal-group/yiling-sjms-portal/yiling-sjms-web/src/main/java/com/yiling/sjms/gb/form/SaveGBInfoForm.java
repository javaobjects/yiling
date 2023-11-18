package com.yiling.sjms.gb.form;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import com.sun.istack.NotNull;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 保存团购信息
 */
@Data
public class SaveGBInfoForm extends BaseForm {

    /**
     * 团购ID
     */
    @ApiModelProperty(value = "团购ID")
    private Long gbId;

    /**
     * 业务类型：1-提报 2-取消
     */
    @NotNull
    @ApiModelProperty(value = "业务类型：1-提报 2-取消",required = true)
    private Integer bizType;

    /**
     * 省区名称
     */
    @ApiModelProperty(value = "省区名称")
    private String provinceName;

    /**
     * 事业部Id
     */
    @ApiModelProperty(value = "事业部Id")
    private Long orgId;

    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String orgName;

    /**
     * 销量计入人工号
     */
    @ApiModelProperty(value = "销量计入人工号")
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    @ApiModelProperty(value = "销量计入人姓名")
    private String sellerEmpName;

    /**
     * 销量计入人区办ID
     */
    @ApiModelProperty(value = "销量计入人区办ID")
    private Long sellerDeptId;

    /**
     * 销量计入人区办名称
     */
    @ApiModelProperty(value = "销量计入人区办名称")
    private String sellerDeptName;

    /**
     * 销量计入人部门名称
     */
    @ApiModelProperty(value = "销量计入人部门名称")
    private String sellerYxDeptName;

    /**
     * 团购负责人工号
     */
    @ApiModelProperty(value = "团购负责人工号")
    private String managerEmpId;

    /**
     * 团购负责人姓名
     */
    @ApiModelProperty(value = "团购负责人姓名")
    private String managerEmpName;

    /**
     * 团购负责人部门名称
     */
    @ApiModelProperty(value = "团购负责人部门名称")
    private String managerYxDeptName;

    /**
     * 团购单位ID
     */
    @ApiModelProperty(value = "团购单位ID")
    private Long customerId;

    /**
     * 团购单位名称
     */
    @ApiModelProperty(value = "团购单位名称")
    private String customerName;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String monthTime;


    /**
     * 申请团购政策-返利金额（元）
     */
    @ApiModelProperty(value = "申请团购政策-返利金额(元)")
    private BigDecimal rebateAmount;

    /**
     * 团购区域：1-国内 2-海外
     */
    @ApiModelProperty(value = "团购区域：1-国内 2-海外")
    private Integer regionType;

    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    @ApiModelProperty(value = "团购性质：1-普通团购 2-政府采购")
    private Integer gbType;

    /**
     * 团购证据-证据类型（多个以,分隔）
     */
    @ApiModelProperty(value = "团购证据-证据类型（多个以,分隔）")
    private String evidences;

    /**
     * 团购证据-其他
     */
    @ApiModelProperty(value = "团购证据-其他")
    private String otherEvidence;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     *库终端和商业信息
     */
    @ApiModelProperty(value = "库终端和商业信息")
    private List<SaveGBCompanyRelationForm> companyList;

    /**
     * 文件key
     */
    @ApiModelProperty(value = "文件key")
    private List<FileInfoForm> fileKeyList;

    /**
     *类型 1-保存草稿 2-提交
     */
    @Valid
    @NotNull
    @ApiModelProperty(value = "类型 1-保存草稿 2-提交 3-驳回提交 " ,required = true)
    private Integer type;
}
