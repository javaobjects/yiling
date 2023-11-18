package com.yiling.hmc.insurance.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrugDto {

    /**
     * 泰康药品编码
     */
    private String drugCode;

    /**
     * 供应商药品编码
     */
    private String channelDrugCode;

    /**
     * 药品大类
     */
    private String drugCategory = "1";

    /**
     * 药品商品名称
     */
    private String drugGoodName;

    /**
     * 药品通用名称
     */
    private String drugGenericName;

    /**
     * 药品规格
     */
    private String specification;

    /**
     * 药品单位
     */
    private String unit = "盒";

    /**
     * 药品剂量 1-处方药必填
     */
    private String prepenType;

    /**
     * 药品频次 1-处方药必填
     */
    private String frequency;

    /**
     * 药品用法 1-处方药必填
     */
    private String usage;

    /**
     * 药品数量
     */
    private Integer num;

    /**
     * 药品市场单价
     */
    private String marketPrice;

    /**
     * 药品折后单价
     */
    private String discountPrice;

}