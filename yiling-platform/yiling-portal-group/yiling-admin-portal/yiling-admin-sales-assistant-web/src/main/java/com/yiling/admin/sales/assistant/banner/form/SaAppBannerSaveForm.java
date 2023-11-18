package com.yiling.admin.sales.assistant.banner.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import cn.hutool.core.date.DateUtil;
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
public class SaAppBannerSaveForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long    id;

    @ApiModelProperty(value = "banner标题")
    @NotBlank(message = "请输入标题名称")
    private String  title;

    @ApiModelProperty(value = "banner图片地址")
    @NotBlank(message = "请提交图片")
    private String  pic;

    @ApiModelProperty(value = "使用场景：1-以岭内部机构 2-非以岭机构")
    private Integer bannerCondition;

    @ApiModelProperty(value = "使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner")
    private Integer usageScenario;

    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    @NotNull(message = "请输入权重")
    @Range(message = "权重范围为 {min} 到 {max} 之间", min = 1, max = 200)
    private Integer sort;

    @ApiModelProperty(value = "显示状态：1-启用 2-停用")
    private Integer bannerStatus;

    @ApiModelProperty(value = "有效起始时间")
    private Date    startTime;

    @ApiModelProperty(value = "有效结束时间")
    private Date    stopTime;

    @ApiModelProperty(value = "活动详情超链接")
    private String  activityLinks;

    @ApiModelProperty(value = "备注")
    private String  remark;

    public Date getStartTime() {
        return DateUtil.beginOfDay(startTime);
    }

    public Date getStopTime() {
        String stop = DateUtil.format(stopTime, "yyyy-MM-dd 23:59:59");
        return DateUtil.parse(stop);
    }
}
