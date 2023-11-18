package com.yiling.user.system.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加或删除医药代表可售商品 Request
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddOrRemoveMrSalesGoodsRequest extends BaseRequest {

    /**
     * 员工ID
     */
    @NotNull
    @Min(1L)
    private Long employeeId;

    /**
     * 商品ID
     */
    @NotEmpty
    private List<Long> goodsIds;

    /**
     * 操作类型：1-添加 2-删除
     */
    @NotNull
    @Range(min = 1, max = 2)
    private Integer opType;
}
