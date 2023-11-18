package com.yiling.open.erp.bo;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * erp实施负责人手机号信息
 *
 * @author: houjie.sun
 * @date: 2022/3/15
 */
@Data
@Accessors(chain = true)
public class ErpInstallEmployeeInfoDetailBO implements java.io.Serializable {

    private static final long serialVersionUID = -9045342699871499480L;

    /**
     * 实施负责人姓名
     */
    private String name;

    /**
     * 实施负责人手机号
     */
    private List<String> mobileList;

}
