package com.yiling.sales.assistant.app.userteam.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手-成员详情VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberDetailVO extends BaseVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 成员名称
     */
    @ApiModelProperty("成员名称")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private Long mobilePhone;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private Date registerTime;

    /**
     * 销售区域描述
     */
    @ApiModelProperty("销售区域描述")
    private String salesArea;


}
