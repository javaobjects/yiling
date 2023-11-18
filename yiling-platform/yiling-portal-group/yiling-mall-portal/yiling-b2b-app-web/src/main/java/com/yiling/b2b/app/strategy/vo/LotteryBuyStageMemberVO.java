package com.yiling.b2b.app.strategy.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/11/4
 */
@Data
public class LotteryBuyStageMemberVO implements Serializable {

    @ApiModelProperty("会员id")
    private Long memberId;

    @ApiModelProperty("会员购买条件id")
    private List<Long> buyStageMemberIdList;
}
