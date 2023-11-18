package com.yiling.cms.content.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryContentPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    private String title;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 栏目id
     */
    private Long categoryId;

    private Date startTime;

    private Date endTime;

    /**
     * 置顶:1-是 0-否
     */
    private Integer isTop;

    /**
     * 类型:1-文章 2-视频
     */
    private Integer contentType;

    /**
     * 医生id
     */
    private Long docId;

    /**
     * 状态：1-未发布 2-已发布
     */
    private Integer status;

    /**
     * 是否院方文章 0-否，1-是
     */
    private Integer ihFlag;

    /**
     * 是否手动排序 0-否，1-是
     */
    private Integer isHandRank;

    /**
     * 创建人id
     */
    private List<Long> createUserIdList;

    /**
     * 创建来源 1-运营后台，2-IH后台
     */
    private Integer createSource;

    /**
     * 内容来源:0-所有 1-站内创建 2-外链
     */
    private Integer sourceContentType;
}