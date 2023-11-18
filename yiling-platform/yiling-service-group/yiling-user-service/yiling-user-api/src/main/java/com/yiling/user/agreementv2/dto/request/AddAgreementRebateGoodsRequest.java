package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利商品 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateGoodsRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 商品组ID
     */
    private Long groupId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 规格商品ID
     */
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 生产厂家名称
     */
    private String producerManufacturerName;

}
