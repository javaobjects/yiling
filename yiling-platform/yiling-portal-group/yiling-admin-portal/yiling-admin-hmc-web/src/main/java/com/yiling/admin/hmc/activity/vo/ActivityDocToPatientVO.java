package com.yiling.admin.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动 VO
 *
 * @author fan.shen
 * @date 2023-01-13
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityDocToPatientVO extends BaseVO {

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
     * 活动进度 1-未开始，2-进行中，3-已结束
     */
    @ApiModelProperty("活动进度 1-未开始，2-进行中，3-已结束")
    private Integer activityProgress;

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

    /**
     * 活动描述
     */
    @ApiModelProperty("活动描述")
    private String activityDesc;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 活动页医生访问数量
     */
    @ApiModelProperty("活动页医生访问数量")
    private Long activityPV;

    /**
     * 活动下的医生数量
     */
    @ApiModelProperty("活动下的医生数量")
    private Long activityDoctorCount;

    /**
     * 活动下的患者数量
     */
    @ApiModelProperty("活动下的患者数量")
    private Long patientCount;

    /**
     * 活动浏览人数
     */
    @ApiModelProperty("活动浏览人数")
    private Long uvCount;

    @ApiModelProperty("活动链接")
    private String activityLink;

}
