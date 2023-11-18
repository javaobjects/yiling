package com.yiling.admin.sales.assistant.banner.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaAppBannerPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "使用位置：0-所有 1-B2B移动端主Banner 2-B2B移动端副Banner")
    private Integer usageScenario;

    @ApiModelProperty(value = "使用场景：0-所有 1-以岭内部机构 2-非以岭机构")
    private Integer bannerCondition;

    @ApiModelProperty(value = "banner标题")
    private String title;

    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer bannerStatus;

    @ApiModelProperty(value = "创建起始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "创建截止时间")
    private Date createEndTime;

    @ApiModelProperty(value = "投放起始时间")
    private Date useStartTime;

    @ApiModelProperty(value = "投放截止时间")
    private Date useEndTime;
}
