package com.yiling.cms.read.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 我的阅读
 * </p>
 *
 * @author gxl
 * @date 2022-07-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddMyReadRequest extends BaseRequest {


    private static final long serialVersionUID = -3848861103038538980L;
    /**
     * 阅读的内容/文献/会议id
     */
    private Long readId;

    /**
     * 阅读类型：1-文章 2-视频 3-文献 4-会议
     */
    private Integer readType;

    /**
     * 标题
     */
    private String title;

    /**
     * 阅读来源
     */
    private Integer source;

    /**
     * 内容创建时间
     */
    private Date contentTime;

    /**
     * cms 各个业务线表主键
     */
    private Long cmsId;


}
