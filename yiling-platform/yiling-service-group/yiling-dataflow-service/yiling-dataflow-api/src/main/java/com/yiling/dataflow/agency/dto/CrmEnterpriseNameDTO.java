package com.yiling.dataflow.agency.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseNameDTO extends BaseDTO {
    /**
     * crm系统对应客户编码
     */
    private String code;

    /**
     * crm系统对应客户名称
     */
    private String name;

}
