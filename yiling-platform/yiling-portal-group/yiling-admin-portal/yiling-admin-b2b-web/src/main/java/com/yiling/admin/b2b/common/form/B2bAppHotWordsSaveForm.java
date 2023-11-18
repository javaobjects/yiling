package com.yiling.admin.b2b.common.form;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.yiling.framework.common.base.form.BaseForm;

import cn.hutool.core.date.DateUtil;
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
public class B2bAppHotWordsSaveForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "热词名称")
    @NotBlank(message = "热词名称不能为空")
    private String content;

    @ApiModelProperty(value = "启用状态：0-启用 1-停用")
    private Integer useStatus;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date stopTime;

    @ApiModelProperty(value = "排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    private Integer sort;

    public Date getStartTime() {
        return DateUtil.beginOfDay(startTime);
    }

    public Date getStopTime() {
        String stop = DateUtil.format(stopTime, "yyyy-MM-dd 23:59:59");
        return DateUtil.parse(stop);
    }
}
