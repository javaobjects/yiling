package com.yiling.dataflow.crm.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/9 0009
 */
@Data
public class CrmEnterpriseIdAndNameBO implements Serializable {


    /**
     * ID
     */
    private Long id;

    /**
     * crm系统对应客户名称
     */
    private String name;
}
