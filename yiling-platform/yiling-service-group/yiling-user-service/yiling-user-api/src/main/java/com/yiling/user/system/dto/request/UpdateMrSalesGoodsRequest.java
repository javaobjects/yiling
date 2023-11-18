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
 * 更新医药代表可售商品 Request
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMrSalesGoodsRequest extends BaseRequest {

    /**
     * 员工ID
     */
    @NotNull
    @Min(1L)
    private Long employeeId;

    /**
     * 商品ID集合
     */
    private List<Long> goodsIds;

}
