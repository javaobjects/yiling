package com.yiling.admin.system.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * IP相关信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/10/19
 */
@Data
public class IPInfoVO {

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("IP归属地信息")
    private Location location;

    @Data
    public static class Location {

        /**
         * 国家名称
         */
        @ApiModelProperty("国家名称")
        private String countryName;

        /**
         * 省名称
         */
        @ApiModelProperty("省名称")
        private String provinceName;

        /**
         * 市名称
         */
        @ApiModelProperty("市名称")
        private String cityName;

        /**
         * 区名称
         */
        @ApiModelProperty("区名称")
        private String regionName;

        /**
         * 运营商名称
         */
        @ApiModelProperty("运营商名称")
        private String operatorName;

        /**
         * 归属地
         */
        @ApiModelProperty("归属地")
        private String location;

        /**
         * 是否为简单格式：<br/>
         * 是 - 取location、operatorName字段值
         * 否 - 取各字段值
         */
        @ApiModelProperty("是否为简单格式")
        private Boolean simpleFormatFlag;

        /**
         * 数据来源
         */
        @ApiModelProperty("数据来源")
        private String dataSource;
    }
}
