package com.yiling.f2b.admin.agreementv2.vo;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 非商品返利 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementOtherRebateVO extends BaseVO {

    /**
     * 非商品返利方式：1-会务费 2-流向返利 3-破损返利 4-维价返利 5-控销返利 6-如期回款返利 7-其他返利
     */
    @ApiModelProperty("非商品返利方式：1-会务费 2-流向返利 3-破损返利 4-维价返利 5-控销返利 6-如期回款返利 7-其他返利（见字典：agreement_not_product_rebate_type）")
    private Integer rebateType;

    /**
     * 金额类型：1-销售金额 2-购进金额 3-付款金额 4-固定金额
     */
    @ApiModelProperty("金额类型：1-销售金额 2-购进金额 3-付款金额 4-固定金额（见字典：agreement_not_product_rebate_amount_type）")
    private Integer amountType;

    /**
     * 百分比或固定金额值
     */
    @ApiModelProperty("百分比或固定金额值")
    private BigDecimal amount;

    /**
     * 单位：1-固定金额 2-百分比
     */
    @ApiModelProperty("单位：1-固定金额 2-百分比")
    private Integer unit;

    /**
     * 是否含税
     */
    @ApiModelProperty("是否含税")
    private Boolean taxFlag;

}
