package com.yiling.marketing.lotteryactivity.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改奖品设置信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRewardSettingRequest extends BaseRequest {

    /**
     * 奖品列表
     */
    @NotEmpty
    private List<SaveRewardSettingRequest> activityRewardSettingList;

    @Data
    public static class SaveRewardSettingRequest implements Serializable {

        /**
         * ID
         */
        @NotNull
        private Long id;

        /**
         * 每天最大抽中数量（0为不限制）
         */
        @NotNull
        private Integer everyMaxNumber;

        /**
         * 中奖概率
         */
        @NotNull
        private BigDecimal hitProbability;
    }

}
