package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/11/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIHPatientContentPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * 标题
     */
    private String title;

    /**
     * 医生Id
     */
    private Long doctorId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目id
     */
    private Long categoryId;

    /**
     * 状态 1未发布 2已发布
     */
    private Integer status;


    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;
}
