package com.yiling.marketing.integral.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 立即兑换 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-29
 */
@Data
@Accessors(chain = true)
public class RecentExchangeGoodsRequest extends BaseRequest {

    /**
     * ID
     */
    @NotNull
    private Long id;

    /**
     * UID（企业ID）
     */
    @NotNull
    private Long uid;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    @NotNull
    private Integer platform;

    /**
     * 兑换数量
     */
    @NotNull
    private Integer exchangeNum;

    /**
     * 兑换地址ID（真实物品时需要）
     */
    private Integer addressId;


}
