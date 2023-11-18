package com.yiling.admin.b2b.integral.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分行为分页 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralBehaviorForm extends QueryPageListForm {

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @NotNull
    @ApiModelProperty(value = "平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手（见字典：integral_rule_platform）", required = true)
    private Integer platform;

    /**
     * 行为类型：1-发放 2-消耗
     */
    @NotNull
    @ApiModelProperty(value = "行为类型：1-发放 2-消耗", required = true)
    private Integer type;
}
