package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 创建订单扩展属性
 *
 * @author zhigang.guo
 * @date: 2022/11/3
 */
@Data
@Accessors(chain = true)
public class CreateOrderExtendRequest extends BaseRequest {

    private static final long serialVersionUID = 1l;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 买家所属省份名称
     */
    private String buyerProvinceName;

    /**
     * 买家所属城市名称
     */
    private String buyerCityName;

    /**
     * 买家所属区域名称
     */
    private String buyerRegionName;

    /**
     * 买家所属省份编码
     */
    private String buyerProvinceCode;

    /**
     * 买家所属城市编码
     */
    private String buyerCityCode;

    /**
     * 买家所属区域编码
     */
    private String buyerRegionCode;

    /**
     * 卖家所属省份名称
     */
    private String sellerProvinceName;

    /**
     * 卖家所属城市名称
     */
    private String sellerCityName;

    /**
     * 卖家所属区域名称
     */
    private String sellerRegionName;

    /**
     * 卖家所属省份编码
     */
    private String sellerProvinceCode;

    /**
     * 卖家所属城市编码
     */
    private String sellerCityCode;

    /**
     * 卖家所属区域编码
     */
    private String sellerRegionCode;

    /**
     * 下单时是否会员 1-非会员 2-会员
     */
    private Integer vipFlag;


    /**
     * vipFlag 标志
     */
    @Getter
    @AllArgsConstructor
    public enum VipFlagEnum {


        VIP(2, "会员订单"), NORMAL(1, "非会员"),
        ;

        private Integer code;

        private String name;

        public static VipFlagEnum getByCode(Integer code) {

            for (VipFlagEnum e : VipFlagEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }

}
