package com.yiling.admin.data.center.enterprise.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 简单企业员工信息 VO
 *
 * @author: lun.yu
 * @date: 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseEmployeeSimpleVO extends BaseVO {

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 身份：企业管理员/员工
     */
    @ApiModelProperty("身份：企业管理员/员工")
    private String identity;
}
