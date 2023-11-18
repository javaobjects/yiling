package com.yiling.basic.location.bo;

import com.yiling.basic.location.enums.IPLocationDataSourceEnum;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * IP归属地 BO 对象 <br/>
 * 首先判断simpleFormatFlag，根据结果取不同字段值
 *
 * @author: xuan.zhou
 * @date: 2022/10/18
 */
@Data
public class IPLocationBO implements java.io.Serializable {

    private static final long serialVersionUID = -1608346939144114872L;

    private IPLocationBO() {
    }

    public IPLocationBO(String ip, String location, String operatorName, IPLocationDataSourceEnum dataSourceEnum) {
        this.ip = ip;
        this.location = location;
        this.operatorName = operatorName;
        this.simpleFormatFlag = true;
        this.dataSource = dataSourceEnum.getCode();
    }

    public IPLocationBO(String ip, String countryName, String provinceName, String cityName, String regionName, String operatorName, IPLocationDataSourceEnum dataSourceEnum) {
        this.ip = ip;
        this.countryName = countryName;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.regionName = regionName;
        this.operatorName = operatorName;
        this.simpleFormatFlag = false;
        this.dataSource = dataSourceEnum.getCode();
    }

    /**
     * IP
     */
    private String ip;

    /**
     * 国家名称
     */
    private String countryName;

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
     * 运营商名称
     */
    private String operatorName;

    /**
     * 归属地
     */
    private String location;

    /**
     * 是否为简单格式：<br/>
     * 是 - 取location、operatorName字段值
     * 否 - 取各字段值
     */
    private Boolean simpleFormatFlag = true;

    /**
     * 数据来源，参考IPLocationDataSourceEnum
     */
    private String dataSource;

    public String getLocation() {
        if (StrUtil.isEmpty(this.location)) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.provinceName).append(this.cityName).append(this.regionName);
            return sb.toString();
        }
        return this.location;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.simpleFormatFlag) {
            sb.append(StrUtil.emptyToDefault(this.location, "-")).append("|");
            sb.append(StrUtil.emptyToDefault(this.operatorName, "-"));
        } else {
            sb.append(StrUtil.emptyToDefault(this.countryName, "-")).append("|");
            sb.append(StrUtil.emptyToDefault(this.provinceName, "-")).append(" ");
            sb.append(StrUtil.emptyToDefault(this.cityName, "-")).append(" ");
            sb.append(StrUtil.emptyToDefault(this.regionName, "-")).append("|");
            sb.append(StrUtil.emptyToDefault(this.operatorName, "-"));
        }
        return sb.toString();
    }
}
