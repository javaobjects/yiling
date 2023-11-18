package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利阶梯 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateStageBO extends BaseVO implements Serializable {

    /**
     * 返利任务量阶梯ID
     */
    @ApiModelProperty("返利任务量阶梯ID")
    private Long taskStageId;

    /**
     * 满
     */
    @ApiModelProperty("满")
    private BigDecimal full;

    /**
     * 满单位：1-元 2-盒
     */
    @ApiModelProperty("满单位：1-元 2-盒")
    private Integer fullUnit;

    /**
     * 返
     */
    @ApiModelProperty("返")
    private BigDecimal back;

    /**
     * 返单位：1-元 2-%
     */
    @ApiModelProperty("返单位：1-元 2-%")
    private Integer backUnit;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

}
