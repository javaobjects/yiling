package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.hmc.order.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 保存订单相关人 request
 *
 * @author: fan.shen
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderRelateUserRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单相关人类型
     */
    private HmcOrderRelateUserTypeEnum userTypeEnum;

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
