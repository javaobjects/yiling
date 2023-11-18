package com.yiling.b2b.app.lotteryactivity.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询我的奖品分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryJoinDetailPageForm extends QueryPageListForm {

    /**
     * 抽奖活动ID
     */
    @ApiModelProperty(value = "抽奖活动ID")
    private Long lotteryActivityId;

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    @ApiModelProperty("兑付状态：1-已兑付 2-未兑付")
    private Integer status;

}