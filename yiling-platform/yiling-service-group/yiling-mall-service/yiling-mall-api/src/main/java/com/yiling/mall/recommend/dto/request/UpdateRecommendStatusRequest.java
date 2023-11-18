package com.yiling.mall.recommend.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 编辑banner Form
 * </p>
 *
 * @author wei.wang
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRecommendStatusRequest extends BaseRequest {

    /**
     * 主键ID
     */

    private Long id;


    /**
     * 状态：1-启用 2-停用
     */

    private Integer status;

}
