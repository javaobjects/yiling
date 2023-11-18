package com.yiling.admin.b2b.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询抽奖活动参与次数明细分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryJoinDetailPageForm extends QueryPageListForm {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 页面类型：1-抽奖次数弹窗 2-中奖次数弹窗
     */
    @NotNull
    @ApiModelProperty(value = "页面类型：1-抽奖次数弹窗 2-中奖次数弹窗", required = true)
    private Integer pageType;

}
