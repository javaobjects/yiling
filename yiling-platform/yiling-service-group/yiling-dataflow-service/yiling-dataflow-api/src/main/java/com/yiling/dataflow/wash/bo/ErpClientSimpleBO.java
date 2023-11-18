package com.yiling.dataflow.wash.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/12/2
 */
@Data
public class ErpClientSimpleBO implements Serializable {
    private static final long serialVersionUID = 6321850184353341077L;

    /**
     * 对接企业eid
     */
    private Long eid;

    private Long crmEnterpriseId;
    /**
     * 统一信用代码
     */
    private String licenseNumber;



    private Integer flowMode;

}
