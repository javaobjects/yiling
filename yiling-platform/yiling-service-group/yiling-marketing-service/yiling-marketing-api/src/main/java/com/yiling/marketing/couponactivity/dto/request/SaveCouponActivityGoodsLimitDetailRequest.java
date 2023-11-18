package com.yiling.marketing.couponactivity.dto.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveCouponActivityGoodsLimitDetailRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 优惠券活动ID
     */
    @NotNull
    private Long couponActivityId;

    /**
     * 企业id
     */
    @NotNull
    private Long eid;

    /**
     * 企业名称
     */
    @NotEmpty
    private String ename;

    /**
     * 商品id
     */
    @NotNull
    private Long goodsId;

    /**
     * 商品名称
     */
    @NotEmpty
    private String goodsName;

    /**
     * 以岭品
     */
    @NotEmpty
    private Integer yilingGoodsFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
