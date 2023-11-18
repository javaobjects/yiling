package com.yiling.admin.pop.notice.vo;

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
}
