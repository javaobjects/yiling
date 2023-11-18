package com.yiling.sales.assistant.app.mr.contact.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/06/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ContactDoctorListVO extends BaseVO {

    @ApiModelProperty("医生姓名")
    private String name;

    @ApiModelProperty("医生手机号")
    private String mobile;

    /**
     * 医疗机构名称
     */
    @ApiModelProperty("医疗机构名称")
    private String orgName;
}
