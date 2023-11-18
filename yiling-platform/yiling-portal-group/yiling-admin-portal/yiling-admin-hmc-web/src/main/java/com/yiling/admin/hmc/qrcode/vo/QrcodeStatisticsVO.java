package com.yiling.admin.hmc.qrcode.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("二维码统计")
public class QrcodeStatisticsVO extends BaseVO {


    /**
     * 页面打开次数
     */
    @ApiModelProperty(value = "页面打开次数")
    private Integer pageView = 0;

    /**
     * 关注数
     */
    @ApiModelProperty(value = "关注数")
    private Integer follow = 0;

    /**
     * 注册人数
     */
    @ApiModelProperty(value = "注册人数")
    private Integer register = 0;

    /**
     * 广告点击人数
     */
    @ApiModelProperty(value = "广告点击人数")
    private Integer adClick = 0;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
