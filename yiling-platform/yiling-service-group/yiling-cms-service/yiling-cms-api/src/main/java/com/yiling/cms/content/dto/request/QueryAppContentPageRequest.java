package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 内容分页列表查询参数
 *
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAppContentPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 板块id
     */
    private Long moduleId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 类型 1-文章，2-视频
     */
    private Integer contentType;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer isOpen;

    private Integer isMeetingVideo;

    private Long standardGoodsId;

    /**
     * 视频标题或者医生名称
     */
    private String title;

    /**
     * 医生id
     */
    private Long doctorId;

    /**
     * 医生id列表
     */
    private List<Integer> docIdList;
}