package com.yiling.mall.banner.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 校验banner信息 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckBannerRequest extends BaseRequest {

    /**
     * banner标题
     */
    private String title;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 链接url
     */
    private String linkUrl;

}
