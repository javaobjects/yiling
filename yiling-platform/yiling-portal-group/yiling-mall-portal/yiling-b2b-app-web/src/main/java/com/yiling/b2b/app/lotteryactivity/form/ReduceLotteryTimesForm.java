package com.yiling.b2b.app.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 减少抽奖次数 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReduceLotteryTimesForm extends BaseForm {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 店铺企业ID
     */
    @ApiModelProperty("店铺企业ID（从店铺banner进入抽奖的会带入该店铺的企业ID）")
    private Long shopEid;

    @ApiModelProperty("抽奖类型:1-积分抽奖 0-默认积分抽奖")
    private Integer type;
}
