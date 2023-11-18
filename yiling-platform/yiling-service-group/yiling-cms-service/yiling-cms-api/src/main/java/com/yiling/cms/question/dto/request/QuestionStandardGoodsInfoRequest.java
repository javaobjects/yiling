package com.yiling.cms.question.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @describe 疑问库关联商品信息
 * @author:wei.wang
 * @date:2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionStandardGoodsInfoRequest extends BaseRequest {

    /**
     * 标准库id
     */
    private Long standardId;

    /**
     * 标准库商品规格ID
     */
    private Long sellSpecificationsId;
}
