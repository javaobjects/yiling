package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 内容分页列表查询参数
 *
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ContentReferStatusRequest extends BaseRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * id
     */
    private Long id;

    /**
     * 引用状态 1-引用，2-取消引用
     */
    private Integer referStatus;

}