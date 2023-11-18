package com.yiling.admin.b2b.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖规则分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLotteryActivityRulePageForm extends QueryPageListForm {

    /**
     * 使用平台：1-B端 2-C端
     */
    @ApiModelProperty(value = "使用平台：1-B端 2-C端")
    private Integer usePlatform;

}
