package com.yiling.user.agreementv2.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 查询协议商品分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementGoodsPageRequest extends QueryPageListRequest {

    /**
     * 协议ID
     */
    @NotNull
    private Long id;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;





}
