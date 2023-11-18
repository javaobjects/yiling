package com.yiling.open.cms.collect.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 我的收藏
 * </p>
 *
 * @author gxl
 * @date 2022-07-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MyCollectVO extends BaseVO {


    /**
     * 收藏的内容/文献/会议id
     */
    private Long collectId;

    /**
     * 收藏的类型：1-文章 2-视频 3-文献 4-会议
     */
    private Integer collectType;

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
