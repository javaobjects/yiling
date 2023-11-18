package com.yiling.hmc.gzh.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公众号欢迎语DTO
 *
 * @author: fan.shen
 * @date: 2023-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GzhGreetingDTO extends BaseDTO {

    /**
     * 场景id
     */
    private Integer sceneId;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 累计触发次数
     */
    private Long triggerCount;

    /**
     * 发布状态 1-已发布，2-未发布
     */
    private Integer publishStatus;

    /**
     * 发布版本
     */
    private String publishVersion;

    /**
     * 草稿版本
     */
    private String draftVersion;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 发布时间
     */
    private Date publishDate;
}
