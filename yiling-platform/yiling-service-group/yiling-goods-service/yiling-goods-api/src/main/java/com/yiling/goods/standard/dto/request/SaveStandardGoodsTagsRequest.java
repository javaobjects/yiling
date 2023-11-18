package com.yiling.goods.standard.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveStandardGoodsTagsRequest
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveStandardGoodsTagsRequest extends BaseRequest {

    /**
     * 标准库ID
     */
    @NotNull
    @Min(1)
    private Long standardId;

    /**
     * 企业标签ID列表
     */
    private List<Long> tagIds;

    /**
     * 关联方式：1-手动 2-自动
     */
    private Integer type;
}
