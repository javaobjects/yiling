package com.yiling.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患教活动 VO
 * @author fan.shen
 * @date 2022/9/2
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityPatientEducateVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;


}
