package com.yiling.hmc.qrcode.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 二维码页面统计
 * @author: gxl
 * @date: 2022/4/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveQrcodeStatisticsRequest extends BaseRequest {
    private static final long serialVersionUID = -6932733567245896218L;
    /**
     * 页面打开次数
     */
    private Integer pageView;

    /**
     * 关注数
     */
    private Integer follow;

    /**
     * 注册人数
     */
    private Integer register;

    /**
     * 注册人数
     */
    private Integer adClick;
}