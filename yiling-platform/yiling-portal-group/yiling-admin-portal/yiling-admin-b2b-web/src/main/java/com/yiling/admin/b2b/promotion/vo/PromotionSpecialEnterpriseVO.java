package com.yiling.admin.b2b.promotion.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2021/05/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSpecialEnterpriseVO extends BaseVO {

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    @ApiModelProperty(value = "营销活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "营销活动名称")
    private String promotionActivityName;

    @ApiModelProperty(value = "营销活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "营销活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "专场活动图片")
    private String pic;

    @ApiModelProperty(value = "是否已经添加过")
    private Boolean isUsed;
}
