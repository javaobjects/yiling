package com.yiling.f2b.admin.enterprise.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 统计可采购销售渠道商的个数 VO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8 0008
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CountSellerChannelVO extends BaseVO {

    @ApiModelProperty("渠道商采购销购销售渠道商总个数")
    private Integer total;

    @ApiModelProperty("工业个数")
    private Integer industryCount;

    @ApiModelProperty("以岭直采个数")
    private Integer industryDirectCount;

    @ApiModelProperty("一级商个数")
    private Integer level1Count;

    @ApiModelProperty("二级商个数")
    private Integer level2Count;

    @ApiModelProperty("专二普一个数")
    private Integer z2p1Count;

}
