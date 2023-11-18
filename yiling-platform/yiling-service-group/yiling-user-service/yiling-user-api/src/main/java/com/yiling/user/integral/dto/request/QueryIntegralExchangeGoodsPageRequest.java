package com.yiling.user.integral.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;
import com.yiling.user.common.util.bean.Order;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换商品分页 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralExchangeGoodsPageRequest extends QueryPageListRequest {

    /**
     * 商品名称
     */
    @Like
    private String goodsName;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    @Eq
    private Integer shelfStatus;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @Eq
    private Integer goodsType;

    /**
     * 创建人
     */
    @Eq
    private Long createUser;

    /**
     * 排序条件：1-运营后台排序 2-APP端排序
     */
    private Integer orderCond;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String mobile;

    /**
     * 是否只查询未过期的兑换商品：0-否 1-是
     */
    private Integer validGoods;

}
