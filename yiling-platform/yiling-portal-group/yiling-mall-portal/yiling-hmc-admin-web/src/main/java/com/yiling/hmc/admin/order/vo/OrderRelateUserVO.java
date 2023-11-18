package com.yiling.hmc.admin.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 订单相关人表
 * </p>
 *
 * @author fan.shen
 * @date 2022-04-27
 */
@Data
public class OrderRelateUserVO extends BaseVO {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("类型 1-收货人，2-发货人，3-通知人")
    private Integer type;

    @ApiModelProperty("姓名")
    private String userName;

    @ApiModelProperty("手机号")
    private String userTel;

    @ApiModelProperty("省")
    private String provinceName;

    @ApiModelProperty("市")
    private String cityName;

    @ApiModelProperty("区")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelProperty("创建人")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private Long updateUser;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;


}
