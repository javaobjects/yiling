package com.yiling.hmc.meeting.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.hmc.cms.vo.AnswerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 会议签到VO
 * @author: fan.shen
 * @data: 2023-03-14
 */
@Data
@ApiModel("MeetingSignVO")
public class MeetingSignVO extends BaseVO {

    @ApiModelProperty("省区")
    private String provinceName;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("电话")
    private String mobile;

    @ApiModelProperty("工作单位")
    private String hospitalName;

    @ApiModelProperty("科室")
    private String departmentName;

    @ApiModelProperty("职务")
    private String jobTitle;

    @ApiModelProperty("机构编码")
    private String sysCode;

    @ApiModelProperty("核销码")
    private String checkCode;

}
