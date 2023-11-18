package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数-商品类型
 * </p>
 *
 * @author dexi.yao
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateReportActivityGoodsForm extends BaseForm {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty("id---修改时必填")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;


    /**
     * 奖励类型：1-金额 2-百分比
     */
    @Range(min = 1,max = 2)
    @NotNull
    @ApiModelProperty("奖励类型：1-金额 2-百分比")
    private Integer rewardType;

    /**
     * 奖励金额
     */
    @NotNull
    @ApiModelProperty("奖励金额")
    private BigDecimal rewardAmount;

    /**
     * 奖励百分比
     */
    @NotNull
    @ApiModelProperty("奖励百分比--小三员奖励不可穿此参数--百分比时传小数如86.3%传86.3")
    private BigDecimal rewardPercentage;

    /**
     * 活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单
     */
    @ApiModelProperty("活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单")
    private Integer orderSource;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty("结束时间")
    private Date endTime;

}
