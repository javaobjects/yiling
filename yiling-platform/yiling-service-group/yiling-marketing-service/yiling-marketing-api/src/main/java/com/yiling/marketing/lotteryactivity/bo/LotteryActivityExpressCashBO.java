package com.yiling.marketing.lotteryactivity.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class LotteryActivityExpressCashBO implements Serializable {

    /**
     * 快递公司（见字典）
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String expressOrderNo;

    /**
     * 卡密信息
     */
    private List<LotteryActivityExpressCashBO.CardInfo> cardInfoList;

    @Data
    public static class CardInfo implements Serializable{
        /**
         * 卡号
         */
        private String cardNo;

        /**
         * 密码
         */
        private String password;

    }

}
