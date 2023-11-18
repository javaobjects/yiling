package com.yiling.cms.content.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 内容分页列表查询参数
 *
 * @author: fan.shen
 * @date: 2022-11-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHMCContentPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8387299024010863751L;

    /**
     *引用业务线id
     */
    private Long lineId;

    /**
     *标题
     */
    private String title;

    /**
     *引用板块id
     */
    private Long moduleId;

    /**
     *栏目id
     */
    private Long categoryId;

    /**
     *类型:1-文章 2-视频
     */
    private Integer contentType;

    /**
     *是否手动排序 0-否，1-是
     */
    private Integer handRankFlag;

    /**
     *医生id
     */
    private Long docId;

    /**
     *状态：1-未发布 2-已发布
     */
    private Integer status;

    /**
     *开始时间
     */
    private Date startTime;

    /**
     *结束时间
     */
    private Date endTime;

    /**
     *创建来源 1-CMS运营后台, 2-IH运营后台
     */
    private Integer createSource;

    /**
     * 置顶:1-是 0-否
     */
    private Integer topFlag;

}