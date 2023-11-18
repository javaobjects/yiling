package com.yiling.user.system.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 查询医药代表可售药品分页列表 Request
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
public class QueryMrSalesGoodsPageListRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    @NotNull
    @Min(1L)
    private Long eid;

    /**
     * 药品名称（模糊查询）
     */
    private String name;
}
