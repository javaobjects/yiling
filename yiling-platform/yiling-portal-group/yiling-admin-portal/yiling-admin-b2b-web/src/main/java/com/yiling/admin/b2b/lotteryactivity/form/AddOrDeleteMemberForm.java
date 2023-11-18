package com.yiling.admin.b2b.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加或删除会员 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddOrDeleteMemberForm extends BaseForm {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 会员ID集合
     */
    @NotNull
    @ApiModelProperty(value = "会员ID", required = true)
    private Long memberId;

}
