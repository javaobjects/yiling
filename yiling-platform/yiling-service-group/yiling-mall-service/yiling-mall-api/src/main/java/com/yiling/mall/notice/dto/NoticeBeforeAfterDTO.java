package com.yiling.mall.notice.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: lun.yu
 * @date: 2021/8/12
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NoticeBeforeAfterDTO extends BaseDTO {

    /**
     * 上一篇的ID
     */
    private Long beforeId;

    /**
     * 上一篇的title
     */
    private String beforeTitle;

    /**
     * 下一篇的ID
     */
    private Long afterId;

    /**
     * 下一篇的title
     */
    private String afterTitle;

}
