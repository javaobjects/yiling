package com.yiling.sjms.flee.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移除销售申诉选择流向数据
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveSelectAppealFlowFormRequest extends BaseRequest {

    /**
     * 销量申诉选择流向表 id集合
     */
    private List<Long> ids;
    /**
     * 销量申诉选择流向表 id
     */
    private Long id;
}
