package com.yiling.dataflow.crm.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/9/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsGroupDTO extends BaseDTO {

    /**
     * 产品组代码
     */
    private String code;

    /**
     * 产品组名称
     */
    private String name;

    /**
     * 状态 0 有效 1无效
     */
    private Integer status;

    /**
     * 修改时间
     */
    private Date updateTime;
}
