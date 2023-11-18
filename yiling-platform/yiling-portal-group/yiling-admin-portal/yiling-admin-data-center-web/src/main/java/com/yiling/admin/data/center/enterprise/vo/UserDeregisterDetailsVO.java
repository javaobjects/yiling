package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号详情信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserDeregisterDetailsVO extends BaseVO {

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
     * 注销原因
     */
    @ApiModelProperty("注销原因")
    private String rejectReason;

    /**
     * 所属企业列表
     */
    @ApiModelProperty("所属企业列表")
    private List<EnterpriseEmployeeSimpleVO> enterpriseList;

}
