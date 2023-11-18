package com.yiling.admin.b2b.integral.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单分页列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralExchangeOrderItemVO extends BaseVO {

    /**
     * 兑换提交时间
     */
    @ApiModelProperty("兑换提交时间")
    private Date submitTime;

    /**
     * 兑换订单号
     */
    @ApiModelProperty("兑换订单号")
    private String orderNo;

    /**
     * 兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    @ApiModelProperty("兑换商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券")
    private Integer goodsType;

    /**
     * 物品名称
     */
    @ApiModelProperty("物品名称")
    private String goodsName;

    /**
     * 兑换数量
     */
    @ApiModelProperty("兑换数量")
    private Integer exchangeNum;

    /**
     * 订单积分值
     */
    @ApiModelProperty("订单积分值")
    private Integer orderIntegral;

    /**
     * 兑换状态：1-未兑换 2-已兑换
     */
    @ApiModelProperty("兑换状态：1-未兑换 2-已兑换")
    private Integer status;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long uid;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String uname;

    /**
     * 提交人手机号
     */
    @ApiModelProperty("提交人手机号")
    private String mobile;

    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String contactor;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String contactorPhone;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String regionName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 快递公司（见字典）
     */
    @ApiModelProperty("快递公司（见字典）")
    private String expressCompany;

    /**
     * 快递单号
     */
    @ApiModelProperty("快递单号")
    private String expressOrderNo;

    /**
     * 订单兑付时间
     */
    @ApiModelProperty("订单兑付时间")
    private Date exchangeTime;

}
