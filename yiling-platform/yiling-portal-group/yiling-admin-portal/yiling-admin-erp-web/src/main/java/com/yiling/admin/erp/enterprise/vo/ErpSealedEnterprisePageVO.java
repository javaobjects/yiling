package com.yiling.admin.erp.enterprise.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流向封存查询商业公司VO
 *
 * @author: houjie.sun
 * @date: 2022/4/14
 */
@Data
public class ErpSealedEnterprisePageVO {

    /**
     * 商业ID
     */
    @ApiModelProperty(value = "商业ID")
    private Long eid;

    /**
     * 商业名称
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

}
