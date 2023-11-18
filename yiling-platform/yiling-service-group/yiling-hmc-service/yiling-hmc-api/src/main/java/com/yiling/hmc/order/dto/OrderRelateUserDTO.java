package com.yiling.hmc.order.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 订单关联用户DTO
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class OrderRelateUserDTO extends BaseDTO {


    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 类型 1-收货人，2-发货人，3-通知人
     */
    private Integer type;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String userTel;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String detailAddress;


}
