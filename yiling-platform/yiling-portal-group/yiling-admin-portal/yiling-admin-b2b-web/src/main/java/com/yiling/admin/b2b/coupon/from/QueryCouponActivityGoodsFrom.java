package com.yiling.admin.b2b.coupon.from;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryCouponActivityGoodsFrom extends QueryPageListForm {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 优惠券活动ID
     */
    @NotNull
    @ApiModelProperty(value = "优惠券活动ID",required = true)
    private Long couponActivityId;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 可用供应商（1-全部供应商可用；2-部分供应商可用）
     */
    @NotNull
    @ApiModelProperty(value = "供应商限制（1-全部供应商；2-部分供应商）")
    private Integer enterpriseLimit;

    /**
     * 以岭商品标记 0-全部 1-以岭品 2-非以岭品
     */
    @ApiModelProperty(value = "以岭商品标记 0-全部 1-以岭品 2-非以岭品")
    private Integer yilingGoodsFlag;

    /**
     * 商品状态：1上架 2下架 3待设置
     */
    private Integer goodsStatus;
}
