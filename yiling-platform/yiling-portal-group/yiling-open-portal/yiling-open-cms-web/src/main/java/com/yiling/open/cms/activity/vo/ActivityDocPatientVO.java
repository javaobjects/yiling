package com.yiling.open.cms.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动 VO
 * @author fan.shen
 * @date 2022/9/2
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityDocPatientVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动状态 1-启用，2-停用
     */
    @ApiModelProperty("活动状态 1-启用，2-停用")
    private Integer activityStatus;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;
}
