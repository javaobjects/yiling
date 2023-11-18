package com.yiling.admin.cms.content.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议分页结果 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-13
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingVO extends BaseVO {

    /**
     * 会议名称
     */
    @ApiModelProperty("会议名称")
    private String title;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;


}
