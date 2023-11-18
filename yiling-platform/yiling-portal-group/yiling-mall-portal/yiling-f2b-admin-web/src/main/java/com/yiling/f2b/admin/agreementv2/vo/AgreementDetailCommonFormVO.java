package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议分类表单 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-17
 */
@Data
@ApiModel
@Accessors(chain = true)
public class AgreementDetailCommonFormVO implements Serializable {

    /**
     * 返利规则，多个分号分割
     */
    @ApiModelProperty("返利规则，多个分号分割")
    private String rebateRule;

    /**
     * 任务量标准：1-销售 2-购进 3-付款金额
     */
    @ApiModelProperty("任务量标准：1-销售 2-购进 3-付款金额")
    private Integer taskStandard;

    /**
     * 任务量，多个分号分割
     */
    @ApiModelProperty("任务量，多个分号分割")
    private String taskNum;

    /**
     * 超任务量汇总返，多个分号分割
     */
    @ApiModelProperty("超任务量汇总返，多个分号分割")
    private String overTaskNum;

    /**
     * 返利阶梯条件计算方法：1-覆盖计算 2-叠加计算
     */
    @ApiModelProperty("返利阶梯条件计算方法：1-覆盖计算 2-叠加计算")
    private Integer rebateStageMethod;

    /**
     * 返利计算规则：1-按单计算 2-汇总计算
     */
    @ApiModelProperty("返利计算规则：1-按单计算 2-汇总计算")
    private Integer rebateCalculateRule;

    /**
     * 返利计算规则类型：1-起返 2-倍返
     */
    @ApiModelProperty("返利计算规则类型：1-起返 2-倍返")
    private Integer rebateRuleType;

    /**
     * 控销类型：1-无 2-黑名单 3-白名单
     */
    @ApiModelProperty("控销类型：1-无 2-黑名单 3-白名单")
    private Integer controlSaleType;

    /**
     * 控销区域描述
     */
    @ApiModelProperty("控销区域描述")
    private String description;

    /**
     * 控销区域Json串
     */
    @ApiModelProperty("控销区域Json串")
    private String jsonContent;

    /**
     * 客户类型，多个分号分割
     */
    @ApiModelProperty("客户类型，多个分号分割")
    private String customerType;


}
