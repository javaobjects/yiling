package com.yiling.admin.b2b.lotteryactivity.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 兑付奖品 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCashRewardForm extends QueryPageListForm {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 兑付类型：1-单个兑付 2-兑付当前页 3-兑付全部
     */
    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "兑付类型：1-单个兑付 2-兑付当前页 3-兑付全部", required = true)
    private Integer cashType;

    /**
     * ID（单个兑付时必传）
     */
    @ApiModelProperty("参与抽奖明细ID（单个兑付时必传）")
    private Long id;

    /**
     * 收货信息（单个兑付时必须存在，不能为空）
     */
    @ApiModelProperty("收货信息（单个兑付时必传，不能为空）")
    private UpdateLotteryActivityReceiptInfoForm activityReceiptInfo;

}
