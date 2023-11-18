package com.yiling.admin.b2b.promotion.form;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动保存form
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionSpecialActivitySaveForm")
public class PromotionSpecialActivitySaveForm extends BaseForm {

    @ApiModelProperty(value = "专场活动id")
    private String id;

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "活动类型 1-满赠，2-特价，3-秒杀, 4-组合包")
    private Integer type;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;
}
