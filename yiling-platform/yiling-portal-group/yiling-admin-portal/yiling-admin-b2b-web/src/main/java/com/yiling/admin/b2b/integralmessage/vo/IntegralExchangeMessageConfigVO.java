package com.yiling.admin.b2b.integralmessage.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换消息配置详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralExchangeMessageConfigVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 图标key
     */
    @ApiModelProperty("图标key")
    private String icon;

    /**
     * 图标url
     */
    @ApiModelProperty("图标url")
    private String iconUrl;

    /**
     * 投放开始时间
     */
    @ApiModelProperty(value = "投放开始时间")
    private Date startTime;

    /**
     * 投放结束时间
     */
    @ApiModelProperty(value = "投放结束时间")
    private Date endTime;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 状态：1-启用 2-禁用
     */
    @ApiModelProperty("状态：1-启用 2-禁用")
    private Integer status;

    /**
     * 页面配置：1-活动链接
     */
    @ApiModelProperty("页面配置：1-活动链接")
    private Integer pageConfig;

    /**
     * 超链接
     */
    @ApiModelProperty("超链接")
    private String link;

}
