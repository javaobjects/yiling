package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * HMC处方支付获取获取药品信息VO
 *
 * @author: fan.shen
 * @data: 2023/05/10
 */
@Data
public class HmcPrescriptionGoodsInfoVO {

    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty("处方类型 1：西药 0：中药")
    private Integer prescriptionType;

    @ApiModelProperty("健康中心商家id")
    private Integer hmcEid;

    @ApiModelProperty("健康中心商家名称")
    private String hmcEname;

    @ApiModelProperty("互联网医院商家id")
    private Integer ihEid;

    @ApiModelProperty("互联网医院商家名称")
    private String ihEname;

    @ApiModelProperty("IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC")
    private Integer ihPharmacySource;

    @ApiModelProperty("处方价格")
    private String prescriptionPrice;

    @ApiModelProperty("药品信息")
    private List<HmcPrescriptionCommodityVO> goodsList;

    @ApiModelProperty("药品单剂价格-中药")
    private String dosePrice;

    @ApiModelProperty("药品剂数-中药")
    private Integer doseCount;

    /**
     * 服用频次-中药
     */
    @ApiModelProperty("服用频次-中药")
    private String dailyTimes;

    /**
     * 服用天数-中药
     */
    @ApiModelProperty("服用天数-中药")
    private Integer dayCount;
}
