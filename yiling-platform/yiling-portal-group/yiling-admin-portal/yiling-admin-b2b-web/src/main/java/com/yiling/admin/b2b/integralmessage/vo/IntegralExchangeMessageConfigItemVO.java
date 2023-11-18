package com.yiling.admin.b2b.integralmessage.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换消息配置列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralExchangeMessageConfigItemVO extends BaseVO {

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
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

}
