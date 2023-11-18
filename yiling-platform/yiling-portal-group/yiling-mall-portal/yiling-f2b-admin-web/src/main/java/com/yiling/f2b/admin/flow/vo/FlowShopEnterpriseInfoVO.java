package com.yiling.f2b.admin.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/11
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("总店企业信息VO")
public class FlowShopEnterpriseInfoVO {

    /**
     * 总店商业代码
     */
    @ApiModelProperty(value = "总店商业代码（商家eid）")
    private Long eid;

    /**
     * 总店商业名称
     */
    @ApiModelProperty(value = "总店商业名称（商家名称）")
    private String ename;
}
