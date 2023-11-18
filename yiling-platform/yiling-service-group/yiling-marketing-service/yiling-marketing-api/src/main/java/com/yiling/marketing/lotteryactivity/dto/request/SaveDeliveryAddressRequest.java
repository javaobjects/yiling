package com.yiling.marketing.lotteryactivity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动-奖品收货地址 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveDeliveryAddressRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID（企业ID或用户ID）
     */
    private Long uid;

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
     * 城市编码
     */
    private String cityCode;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

}
