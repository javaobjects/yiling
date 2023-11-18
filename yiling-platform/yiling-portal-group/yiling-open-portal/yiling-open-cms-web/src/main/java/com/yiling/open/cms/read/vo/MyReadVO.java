package com.yiling.open.cms.read.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

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
public class MyReadVO extends BaseVO {


    /**
     * 阅读的内容/文献/会议id
     */
    private Long readId;

    /**
     * 阅读类型：1-文章  2-视频 3-文献 4-会议
     */
    private Integer readType;

    /**
     * 标题
     */
    private String title;


    /**
     * 内容创建时间
     */
    private Date contentTime;

    private String cover;

    /**
     * cms 各个业务线表主键
     */
    private Long cmsId;
}
