package com.yiling.admin.sales.assistant.userteam.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-拉人信息VO
 * @author lun.yu
 * @version V1.0
 * @date: 2022/1/25
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserTeamSimpleVO {

    /**
     * 成员名称
     */
    @ApiModelProperty("成员名称")
    private String name;

    /**
     * 注册状态：0-未注册 1-已注册
     */
    @ApiModelProperty("注册状态：0-未注册 1-已注册")
    private Integer registerStatus;

    /**
     * 联系人电话
     */
    @ApiModelProperty("联系人电话")
    private String mobilePhone;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private Date registerTime;

}
