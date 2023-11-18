package com.yiling.f2b.admin.enterprise.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 采购关系 VO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PurchaseRelationVO extends BaseVO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String name;

    /**
     * 企业简称
     */
    @ApiModelProperty("企业简称")
    private String shortName;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

}
