package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号审核列表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserDeregisterAuthListItemVO extends BaseVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 申请注销时间
     */
    @ApiModelProperty("申请注销时间")
    private Date applyTime;

    /**
     * 来源：1-销售助手APP 2-大运河APP 3-医生助手APP
     */
    @ApiModelProperty("来源：1-销售助手APP 2-大运河APP 3-医生助手APP")
    private Integer source;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人", hidden = true)
    private Long authUser;

    /**
     * 审核人名称
     */
    @ApiModelProperty("审核人名称")
    private String authUserName;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date authTime;

    /**
     * 所属企业
     */
    @ApiModelProperty("所属企业")
    private Integer enterpriseNum;

}
