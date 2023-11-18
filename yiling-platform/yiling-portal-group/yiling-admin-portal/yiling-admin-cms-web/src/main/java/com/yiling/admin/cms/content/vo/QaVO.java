package com.yiling.admin.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: fan.shen
 * @data: 2023-03-14
 */
@Data
public class QaVO extends BaseVO {

    @ApiModelProperty("问答类型 1-提问，2-解答")
    private Integer qaType;

    @ApiModelProperty("cms_qa问答表主键")
    private Long qaId;

    @ApiModelProperty("问答内容")
    private String content;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("针对内容")
    private String contentTitle;

    private Long contentId;

    @ApiModelProperty("引用业务线id")
    private Integer lineId;

    @ApiModelProperty("展示状态 1-展示，2-关闭")
    private Integer showStatus;

    @ApiModelProperty("手机号")
    private String mobile;

    private Long createUser;

    @ApiModelProperty("医生信息")
    private HmcDoctorInfoVO doctorInfoVo = new HmcDoctorInfoVO();

    @ApiModelProperty("创建时间")
    private Date createTime;

}
