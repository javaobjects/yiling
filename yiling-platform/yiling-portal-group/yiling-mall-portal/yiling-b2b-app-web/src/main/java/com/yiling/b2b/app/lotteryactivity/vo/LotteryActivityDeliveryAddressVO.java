package com.yiling.b2b.app.lotteryactivity.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动-奖品收货地址 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityDeliveryAddressVO extends BaseVO {

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

}
