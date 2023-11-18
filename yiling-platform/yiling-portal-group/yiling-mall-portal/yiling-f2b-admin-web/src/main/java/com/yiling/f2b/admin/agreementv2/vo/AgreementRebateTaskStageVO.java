package com.yiling.f2b.admin.agreementv2.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.f2b.admin.agreementv2.form.AddAgreementRebateStageForm;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利任务量阶梯 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateTaskStageVO extends BaseVO {

    /**
     * 任务量
     */
    @ApiModelProperty("任务量")
    private BigDecimal taskNum;

    /**
     * 任务量单位：1-元 2-盒
     */
    @ApiModelProperty("任务量单位：1-元 2-盒")
    private Integer taskUnit;

    /**
     * 超任务量汇总返
     */
    @ApiModelProperty("超任务量汇总返")
    private BigDecimal overSumBack;

    /**
     * 超任务量汇总返单位：1-元 2-%
     */
    @ApiModelProperty("超任务量汇总返单位：1-元 2-%")
    private Integer overSumBackUnit;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 协议返利阶梯集合
     */
    @ApiModelProperty("协议返利阶梯集合")
    private List<AgreementRebateStageVO> agreementRebateStageList;

}
