package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家管理列表查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementManufacturerGoodsRequest extends QueryPageListRequest {

    /**
     * 厂家ID
     */
    private Long manufacturerId;


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

}
