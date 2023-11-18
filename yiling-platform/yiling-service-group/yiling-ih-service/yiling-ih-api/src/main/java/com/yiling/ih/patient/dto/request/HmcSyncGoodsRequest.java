package com.yiling.ih.patient.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * HMC往IH同步商品 request
 *
 * @author fan.shen
 * @date 2023-05-18
 */
@Data
public class HmcSyncGoodsRequest implements java.io.Serializable {

    private static final long serialVersionUID = 2574332310936289764L;

    //
    // @NotNull
    // @ApiModelProperty(value = "中台药品id")
    private Integer hmcGoodsId;

    // @NotNull
    // @ApiModelProperty(value = "中台规格ID")
    private Integer hmcSellSpecificationsId;

    // @NotBlank
    // @ApiModelProperty(value = "YPID")
    private String ypidCode;

    // @NotBlank
    // @ApiModelProperty(value = "是否为处方药 0:不是 1:是")
    private String isPrescriptionDrug;

    // @NotBlank
    // @ApiModelProperty(value = "商品通用名")
    private String generalName;

    // @ApiModelProperty(value = "药品名称")
    private String name;

    // @NotNull
    // @ApiModelProperty(value = "药品类型(1,2为西药 3,4为中药) 1：西药 2：中成药 3：中药饮片 4：中草药颗粒")
    private Integer drugType;

    // @NotBlank
    // @ApiModelProperty(value = "规格")
    private String spec;

    // @NotBlank
    // @ApiModelProperty(value = "剂型单位（片/粒/瓶/袋/颗/支/贴/丸/吸...）")
    private String preparationForm;

    // @ApiModelProperty(value = "生产厂家")
    private String productCompany;

    // @NotBlank
    // @ApiModelProperty(value = "包装单位（盒/瓶/袋/支...）")
    private String specificationsUnit;

    // @NotBlank
    // @ApiModelProperty(value = "用法用量")
    private String usageAndDosage;

    // @ApiModelProperty(value = "批准文号")
    private String approvalNumber;

    // @ApiModelProperty(value = "国家监管对应的药品剂型")
    private String preparationFormCode;

    // @ApiModelProperty(value = "成分")
    private String composition;

    // @ApiModelProperty(value = "性状")
    private String property;

    // @ApiModelProperty(value = "储藏")
    private String storage;

    // @ApiModelProperty(value = "保质期-有效期")
    private String validity;

    // @ApiModelProperty(value = "适应症|功效")
    private String effect;

    // @ApiModelProperty(value = "不良反应")
    private String adverseReaction;

    // @ApiModelProperty(value = "注意事项")
    private String attention;

    // @ApiModelProperty(value = "禁忌")
    private String taboo;

    // @ApiModelProperty(value = "药物相互作用")
    private String drugInteraction;

    //---------------------中药新增的------------------------------------
    // @ApiModelProperty(value = "生产许可证")
    private String licenseNo;
}
