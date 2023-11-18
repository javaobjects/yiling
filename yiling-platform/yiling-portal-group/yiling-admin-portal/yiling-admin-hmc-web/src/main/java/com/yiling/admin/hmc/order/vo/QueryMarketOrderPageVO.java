package com.yiling.admin.hmc.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/23
 */
@Data
public class QueryMarketOrderPageVO extends BaseVO {

    /**
     * 平台订单编号
     */
    @ApiModelProperty("平台订单编号")
    private String orderNo;

    /**
     * 订单状态:1-预订单待支付,2-已取消,3-待自提,4-待发货,5-待收货,6-已收货,7-已完成,8-取消已退
     */
    @ApiModelProperty("订单状态:1-预订单待支付,2-已取消,3-待自提,4-待发货,5-待收货,6-已收货,7-已完成,8-取消已退")
    private Integer orderStatus;

    /**
     * 订单类型 1-八子，2-处方订单
     */
    @ApiModelProperty("订单类型 1-八子，2-处方订单")
    private Integer marketOrderType;

    /**
     * 药品服务终端名称
     */
    @ApiModelProperty("药品服务终端名称")
    private String ename;

    @ApiModelProperty("支付状态:1-未支付，2-已支付,3-已全部退款")
    private Integer paymentStatus;

    /**
     * 订单总额 = 运费 + 商品总额
     */
    @ApiModelProperty("需付金额")
    private BigDecimal orderTotalAmount;

    @ApiModelProperty("实收金额")
    private String realTotalAmount;

    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String name;

    /**
     * 收货人手机
     */
    @ApiModelProperty("收货人手机")
    private String mobile;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    private Date createTime;

    @ApiModelProperty("药品详情")
    private List<MarketOrderDetailVO> detailVOS;

    @ApiModelProperty("处方药品详情")
    private HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO;

    @ApiModelProperty(value = "处方商品描述",example = "核桃仁 8g、川贝 9g、土鳖虫 12g")
    private String hmcPrescriptionGoodsGoodsDesc;

    @ApiModelProperty("注册用户ID")
    private Long createUser;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("医生id")
    private Integer doctorId;

    @ApiModelProperty("用户手机号")
    private String userMobile;

    @ApiModelProperty("医生名称")
    private String doctorName;

    @ApiModelProperty("医院名称")
    private String hospitalName;

    @ApiModelProperty("IH配送商来源 1：以岭互联网医院IH 2：健康中心HMC")
    private Integer ihPharmacySource;

    @ApiModelProperty("处方类型 1：西药 0：中药  仅仅用来展示，不允许参与逻辑")
    private Integer prescriptionType;

    @ApiModelProperty("IH 处方编号")
    private String ihPrescriptionNo;

    /**
     * 处方id
     */
    @ApiModelProperty("处方id")
    private Long prescriptionId;
}
