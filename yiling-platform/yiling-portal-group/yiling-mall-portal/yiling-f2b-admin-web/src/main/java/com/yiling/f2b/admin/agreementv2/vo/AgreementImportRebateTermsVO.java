package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利条款导入 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-18
 */
@Data
@ApiModel
@Accessors(chain = true)
public class AgreementImportRebateTermsVO implements Serializable {

    /**
     * 是否底价供货：0-否 1-是
     */
    @ApiModelProperty(value = "是否底价供货：0-否 1-是")
    private Integer reserveSupplyFlag;

    /**
     * 商品返利规则设置方式：1-全品设置 2-分类设置
     */
    @ApiModelProperty(value = "商品返利规则设置方式：1-全品设置 2-分类设置")
    private Integer goodsRebateRuleType;

}
