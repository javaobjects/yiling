package com.yiling.b2b.app.integral.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 我的积分页 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@Accessors(chain = true)
public class MyIntegralVO {

    @ApiModelProperty("积分值")
    private Integer integralValue;

    @ApiModelProperty("快来赚积分")
    private List<GetIntegralRule> getIntegralRuleList;

    @Data
    public static class GetIntegralRule {

        @ApiModelProperty("规则ID")
        private Long ruleId;

        @ApiModelProperty("规则名称")
        private String ruleName;

        @ApiModelProperty("规则说明")
        private String description;

        @ApiModelProperty("行为ID")
        private Long behaviorId;

        @ApiModelProperty("行为名称")
        private String behaviorName;

        @ApiModelProperty("行为图标")
        private String icon;

    }

    @ApiModelProperty("积分兑换消息")
    private List<IntegralExchangeMessageVO> integralExchangeMessageList;

    @Data
    public static class IntegralExchangeMessageVO {

        @ApiModelProperty("积分兑换消息ID")
        private Long exchangeMessageId;

        @ApiModelProperty("积分兑换消息名称")
        private String exchangeMessageName;

        @ApiModelProperty("积分兑换消息图标")
        private String exchangeMessageIcon;

        @ApiModelProperty("超链接")
        private String link;

    }

}
