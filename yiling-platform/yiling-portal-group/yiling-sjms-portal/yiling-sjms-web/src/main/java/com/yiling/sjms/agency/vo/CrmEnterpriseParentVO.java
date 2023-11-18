package com.yiling.sjms.agency.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseParentVO extends BaseVO {

    /**
     * crm系统对应客户名称
     */
    private String name;


    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty("连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁")
    private Integer chainAttribute;
}
