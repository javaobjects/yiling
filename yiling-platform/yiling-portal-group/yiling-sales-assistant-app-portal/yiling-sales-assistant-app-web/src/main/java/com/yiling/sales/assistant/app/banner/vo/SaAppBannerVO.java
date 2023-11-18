package com.yiling.sales.assistant.app.banner.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaAppBannerVO extends BaseVO {

    @ApiModelProperty(value = "banner标题")
    private String  title;

    @ApiModelProperty(value = "banner图片地址")
    private String  pic;

    @ApiModelProperty(value = "使用场景：1-以岭内部机构 2-非以岭机构")
    private Integer bannerCondition;

    @ApiModelProperty(value = "使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner")
    private Integer usageScenario;

    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    private Integer sort;

    @ApiModelProperty(value = "显示状态：1-启用 2-停用")
    private Integer bannerStatus;

    @ApiModelProperty(value = "有效起始时间")
    private Date    startTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date    stopTime;

    @ApiModelProperty(value = "活动详情超链接")
    private String  activityLinks;

    @ApiModelProperty(value = "创建人")
    private Long    createUser;

    @ApiModelProperty(value = "创建时间")
    private Date    createTime;

    @ApiModelProperty(value = "修改人")
    private Long    updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date    updateTime;

    @ApiModelProperty(value = "备注")
    private String  remark;
}
