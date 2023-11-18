package com.yiling.marketing.integralmessage.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分兑换消息配置 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralExchangeMessageConfigRequest extends BaseRequest {

    /**
     * ID（编辑时传入）
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 投放开始时间
     */
    private Date startTime;

    /**
     * 投放结束时间
     */
    private Date endTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：1-启用 2-禁用
     */
    private Integer status;

    /**
     * 页面配置：1-活动链接
     */
    private Integer pageConfig;

    /**
     * 超链接
     */
    private String link;

}
