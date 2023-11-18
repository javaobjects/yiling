package com.yiling.admin.erp.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/4/27
 */
@Data
public class ErpFlowGoodsConfigEnterpriseVO {

    /**
     * 商业id
     */
    @ApiModelProperty(value = "商业id")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

}
