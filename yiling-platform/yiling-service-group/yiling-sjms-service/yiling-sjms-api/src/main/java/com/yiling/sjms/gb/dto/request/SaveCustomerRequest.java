package com.yiling.sjms.gb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author wei.wang
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCustomerRequest  extends BaseRequest {
    /**
     * 团购单位名称
     */
    private String name;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 省份编号
     */
    private String provinceCode;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区县名称
     */
    private String regionName;

    /**
     * 区县编码
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 社会统一信用代码
     */
    private String creditCode;




}
