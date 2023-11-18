package com.yiling.user.shop.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;

/**
 * <p>
 * B2B-店铺楼层商品分页列表查询 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-21
 */
@Data
public class QueryFloorGoodsPageRequest extends QueryPageListRequest {

    /**
     * 楼层ID
     */
    @NotNull
    private Long floorId;

}
