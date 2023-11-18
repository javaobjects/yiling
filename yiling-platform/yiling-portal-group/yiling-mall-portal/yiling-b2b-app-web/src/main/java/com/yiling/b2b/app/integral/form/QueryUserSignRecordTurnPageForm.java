package com.yiling.b2b.app.integral.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询用户签到翻页 Form
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserSignRecordTurnPageForm extends BaseForm {

    /**
     * 发放规则ID
     */
    @NotNull
    @ApiModelProperty(value = "发放规则ID", required = true)
    private Long giveRuleId;

    /**
     * 当前页的年份
     */
    @NotNull
    @ApiModelProperty(value = "当前页的年份", required = true)
    private Integer year;

    /**
     * 当前页的月份
     */
    @NotNull
    @Range(min = 1, max = 12)
    @ApiModelProperty(value = "当前页的月份", required = true)
    private Integer month;

    /**
     * 翻页类型：1-上一页 2-下一页
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "翻页类型：1-上一页 2-下一页", required = true)
    private Integer turnType;
}
