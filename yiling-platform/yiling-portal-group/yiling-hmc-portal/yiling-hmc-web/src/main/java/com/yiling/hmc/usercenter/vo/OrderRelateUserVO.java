package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单关联用户信息
 *
 * @author fan.shen
 * @date 2022/4/27
 */
@Data
public class OrderRelateUserVO extends BaseVO {

    /**
     * 用户类型 1-收货人，2-发货人，3-通知人
     */
    @ApiModelProperty("用户类型 1-收货人，2-发货人，3-通知人")
    private Integer type;

    /**
     * 姓名
     */
    @ApiModelProperty("用户类型 1-收货人，2-发货人，3-通知人")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty("姓名")
    private String userTel;

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String provinceName;

    /**
     * 市
     */
    @ApiModelProperty("市")
    private String cityName;

    /**
     * 区
     */
    @ApiModelProperty("区")
    private String districtName;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String detailAddress;


}
