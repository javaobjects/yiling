package com.yiling.b2b.admin.enterprisecustomer.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业客户分组列表项 VO
 *
 * @author: lun.yu
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CustomerGroupListItemVO extends CustomerGroupVO {

    /**
     * 客户数量
     */
    @ApiModelProperty("客户数量")
    private Long customerNum;

    /**
     * 是否已添加：true-是，false-否
     */
    @ApiModelProperty("是否已添加：true-是，false-否")
    private Boolean selectedFlag;

    public CustomerGroupListItemVO(){
        this.selectedFlag = false;
    }
}
