package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换商品列表项 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@Accessors(chain = true)
public class IntegralExchangeGoodsItemBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 物品ID
     */
    private Long goodsId;

    /**
     * 物品名称
     */
    private String goodsName;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    private Integer goodsType;

    /**
     * 兑换所需积分
     */
    private Integer exchangeUseIntegral;

    /**
     * 剩余可兑换数量
     */
    private Integer canExchangeNum;

    /**
     * 近30天兑换数量
     */
    private Integer recentExchangeNum;

    /**
     * 是否区分用户身份：1-全部 2-指定会员类型
     */
    private Integer userFlag;

    /**
     * 有效期生效时间
     */
    private Date validStartTime;

    /**
     * 有效期失效时间
     */
    private Date validEndTime;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    private Integer shelfStatus;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 创建人手机号
     */
    private String mobile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
