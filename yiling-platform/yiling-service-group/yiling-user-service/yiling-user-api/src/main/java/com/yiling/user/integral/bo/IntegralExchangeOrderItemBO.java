package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单分页列表项 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@Accessors(chain = true)
public class IntegralExchangeOrderItemBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 兑换提交时间
     */
    private Date submitTime;

    /**
     * 兑换订单号
     */
    private String orderNo;

    /**
     * 兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    private Integer goodsType;

    /**
     * 物品ID
     */
    private Long goodsId;

    /**
     * 物品名称
     */
    private String goodsName;

    /**
     * 兑换数量
     */
    private Integer exchangeNum;

    /**
     * 订单积分值
     */
    private Integer orderIntegral;

    /**
     * 兑换状态：1-未兑换 2-已兑换
     */
    private Integer status;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 提交人手机号
     */
    private String mobile;

    /**
     * 收货人
     */
    private String contactor;

    /**
     * 联系电话
     */
    private String contactorPhone;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 快递公司（见字典）
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String expressOrderNo;

    /**
     * 订单兑付时间
     */
    private Date exchangeTime;

}
