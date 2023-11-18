package com.yiling.f2b.web.notice.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class NoticeInfoFrontVO extends BaseVO {
    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;


}
