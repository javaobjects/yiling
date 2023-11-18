package com.yiling.f2b.admin.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/18
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("企业信息VO")
public class FlowEnterpriseInfoVO {

    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "商业代码（商家eid）")
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称（商家名称）")
    private String ename;

}
