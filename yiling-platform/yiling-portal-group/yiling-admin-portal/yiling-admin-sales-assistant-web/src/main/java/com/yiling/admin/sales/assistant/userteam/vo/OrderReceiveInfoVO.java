package com.yiling.admin.sales.assistant.userteam.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-订单对应收货信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/30
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReceiveInfoVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 收货人
     */
    @ApiModelProperty("收货人")
    private String name;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String mobile;

    /**
     * 发货量
     */
    @ApiModelProperty("发货量")
    private Integer deliveryQuantity;

    /**
     * 收货量
     */
    @ApiModelProperty("收货量")
    private Integer receiveQuantity;

    /**
     * 省份名称
     */
    @ApiModelProperty("省份名称")
    private String provinceName;

    /**
     * 城市名称
     */
    @ApiModelProperty("城市名称")
    private String cityName;

    /**
     * 区域名称
     */
    @ApiModelProperty("区域名称")
    private String regionName;


    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;




}
