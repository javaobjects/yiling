package com.yiling.dataflow.flow.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectStatisticBO
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Data
public class FlowEnterpriseConnectStatisticBO implements Serializable {

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 经销商数量
     */
    private Integer enterpriseCount;

    /**
     * 无效连接数量
     */
    private Integer invalidCount;

    /**
     * 有效连接数量
     */
    private Integer validCount;

}
