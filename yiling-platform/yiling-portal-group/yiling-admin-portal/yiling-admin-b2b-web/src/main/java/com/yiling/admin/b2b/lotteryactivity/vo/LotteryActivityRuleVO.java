package com.yiling.admin.b2b.lotteryactivity.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动规则 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityRuleVO extends BaseVO {

    /**
     * 使用平台：1-B端 2-C端
     */
    @ApiModelProperty("使用平台：1-B端 2-C端")
    private Integer usePlatform;

    /**
     * 参与规则
     */
    @ApiModelProperty("参与规则")
    private String joinRule;

    /**
     * 说明
     */
    @ApiModelProperty("说明")
    private String instruction;


}
