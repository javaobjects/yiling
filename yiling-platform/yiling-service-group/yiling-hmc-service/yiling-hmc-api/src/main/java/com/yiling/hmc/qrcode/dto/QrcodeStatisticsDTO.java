package com.yiling.hmc.qrcode.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 二维码统计
 * </p>
 *
 * @author gxl
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QrcodeStatisticsDTO extends BaseDTO {


    /**
     * 页面打开次数
     */
    private Integer pageView = 0;

    /**
     * 关注数
     */
    private Integer follow = 0;

    /**
     * 注册人数
     */
    private Integer register = 0;

    /**
     * 广告点击人数
     */
    private Integer adClick = 0;


    private Date createTime;
}
