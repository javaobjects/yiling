package com.yiling.user.integral.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralExchangeOrderDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 兑换订单号
     */
    private String orderNo;

    /**
     * 兑换提交时间
     */
    private Date submitTime;

    /**
     * 积分兑换商品表的ID
     */
    private Long exchangeGoodsId;

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
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 订单兑付时间
     */
    private Date exchangeTime;

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
