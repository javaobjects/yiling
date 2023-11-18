package com.yiling.admin.b2b.promotion.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动-秒杀&特价
 *
 * @author: fan.shen
 * @date: 2022/1/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSecKillSpecialVO extends BaseVO {

    @ApiModelProperty(value = "促销活动ID")
    private Long          promotionActivityId;

    @ApiModelProperty(value = "终端身份 1-会员，2-非会员")
    private Integer       terminalType;

    @ApiModelProperty(value = "允许购买区域 1-全部，2-部分")
    private Integer       permittedAreaType;

    @ApiModelProperty(value = "允许购买区域明细json")
    private String        permittedAreaDetail;

    @ApiModelProperty(value = "允许购买区域明细描述")
    private String        permittedAreaDetailDescription;

    @ApiModelProperty(value = "企业类型 1-全部，2-部分")
    private Integer       permittedEnterpriseType;

    @ApiModelProperty(value = "企业类型json")
    private List<Integer> permittedEnterpriseDetail;
}
