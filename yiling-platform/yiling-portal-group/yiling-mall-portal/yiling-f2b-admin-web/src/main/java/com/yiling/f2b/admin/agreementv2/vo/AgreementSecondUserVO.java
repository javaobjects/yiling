package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议乙方签订人 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSecondUserVO extends BaseVO {

    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long departmentId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departmentName;

    /**
     * 乙方ID
     */
    @ApiModelProperty("乙方ID")
    private Long secondEid;

    /**
     * 乙方名称
     */
    @ApiModelProperty("乙方名称")
    private String secondName;

}
