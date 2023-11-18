package com.yiling.basic.location.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/12/23
 */
@Data
@Accessors(chain = true)
public class RegionFullViewDTO implements Serializable {

    private static final long serialVersionUID = -5476738260837305970L;

    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 区名称
     */
    private String regionName;
    /**
     * 省编码
     */
    private String provinceCode;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 区编码
     */
    private String regionCode;

}
