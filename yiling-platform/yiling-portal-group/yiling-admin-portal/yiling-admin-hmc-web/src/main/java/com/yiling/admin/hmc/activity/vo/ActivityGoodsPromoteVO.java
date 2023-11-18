package com.yiling.admin.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 商品推广活动 VO
 *
 * @author fan.shen
 * @date 2022/9/2
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityGoodsPromoteVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

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
     * 活动下的医生数量
     */
    @ApiModelProperty("活动下的医生数量")
    private Long activityDoctorCount;


}
