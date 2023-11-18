package com.yiling.b2b.app.shop.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 店铺-优惠券列表VO
 *
 * @author: lun.yu
 * @date: 2021/11/12
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponActivityVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;
    /**
     * 优惠券集合
     */
    @ApiModelProperty("企业名称")
    private List<CouponActivityHasGetVO> couponActivityHasGet;
}
