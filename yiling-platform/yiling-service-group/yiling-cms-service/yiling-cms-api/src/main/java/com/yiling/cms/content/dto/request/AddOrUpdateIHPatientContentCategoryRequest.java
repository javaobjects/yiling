package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddOrUpdateIHPatientContentCategoryRequest extends BaseRequest {

    private static final long serialVersionUID = 8943103044035364017L;

    /**
     * 业务线id
     */
    private Long lineId;

    /**
     * 模块id 1-医院健康资讯 2-医院首页推荐
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 栏目排序
     */
    private Integer categoryRank;
}
