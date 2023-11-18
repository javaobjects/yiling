package com.yiling.f2b.web.notice.vo;

import com.yiling.f2b.web.mall.vo.NoticeInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:lun.yu
 * @date:2021年8月4日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NoticeInfoDetailsVO extends NoticeInfoVO {
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "上一篇的ID")
    private Long beforeId;

    @ApiModelProperty(value = "上一篇的title")
    private String beforeTitle;

    @ApiModelProperty(value = "下一篇的ID")
    private Long afterId;

    @ApiModelProperty(value = "下一篇的title")
    private String afterTitle;
}
