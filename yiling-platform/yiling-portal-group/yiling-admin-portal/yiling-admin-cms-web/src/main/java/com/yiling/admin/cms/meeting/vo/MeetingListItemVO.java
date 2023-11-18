package com.yiling.admin.cms.meeting.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议管理列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingListItemVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 活动状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期
     */
    @ApiModelProperty("活动状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期（字典：meeting_show_status）")
    private Integer status;

    /**
     * 阅读量（PV）
     */
    @ApiModelProperty("阅读量（PV）")
    private Integer readNum;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

}
