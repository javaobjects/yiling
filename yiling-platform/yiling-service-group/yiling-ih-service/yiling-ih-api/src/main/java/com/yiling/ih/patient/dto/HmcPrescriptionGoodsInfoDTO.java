package com.yiling.ih.patient.dto;

import lombok.Data;

import java.util.List;

/**
 * HMC处方支付获取获取药品信息VO
 *
 * @author: fan.shen
 * @data: 2023/05/10
 */
@Data
public class HmcPrescriptionGoodsInfoDTO implements java.io.Serializable {

    /**
     * 处方id
     */
    private Integer prescriptionId;

    /**
     * 处方类型 处方类型 1：西药 0：中药
     */
    private Integer prescriptionType;

    /**
     * 健康中心商家id
     */
    private Integer hmcEid;

    /**
     * 健康中心商家名称
     */
    private String hmcEname;

    /**
     * 互联网医院商家id
     */
    private Integer ihEid;

    /**
     * 互联网医院商家名称
     */
    private String ihEname;

    /**
     * IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC
     */
    private Integer ihPharmacySource;

    /**
     * 处方价格
     */
    private String prescriptionPrice;

    /**
     * 药品信息
     */
    private List<HmcPrescriptionCommodityDTO> goodsList;

    /**
     * 药品单剂价格-中药
     */
    private String dosePrice;

    /**
     * 药品剂数-中药
     */
    private Integer doseCount;

    /**
     * 服用频次-中药
     */
    private String dailyTimes;

    /**
     * 服用天数-中药
     */
    private Integer dayCount;
}
