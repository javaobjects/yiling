package com.yiling.dataflow.order.bo;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
public class FlowPermissionsBO {

    /**
     * 商业名称（商家名称）
     */
    private String ename;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

}
