package com.yiling.hmc.lotteryactivity.vo;

import java.io.Serializable;
import java.util.List;

import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动快递信息或卡密 BO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-27
 */
@Data
@Accessors(chain = true)
public class LotteryActivityExpressCashVO implements Serializable {

    /**
     * 快递公司（见字典）
     */
    @ApiModelProperty("快递公司")
    private String expressCompany;

    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String expressOrderNo;

    /**
     * 卡密信息
     */
    @ApiModelProperty("卡密信息")
    private List<LotteryActivityExpressCashBO.CardInfo> cardInfoList;

    @Data
    public static class CardInfo {
        /**
         * 卡号
         */
        @ApiModelProperty("卡号")
        private String cardNo;

        /**
         * 密码
         */
        @ApiModelProperty("密码")
        private String password;

    }


}
