package com.yiling.admin.data.center.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业分页列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
public class EnterprisePageListItemVO {

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterpriseInfo;

    @ApiModelProperty("平台产品线开通信息")
    private PlatformInfo platformInfo;

    @ApiModelProperty("账号数量")
    private Long accountNum;

    @ApiModelProperty("企业标签名称列表")
    private List<String> tagNames;

    @Data
    public static class PlatformInfo {

        @ApiModelProperty("是否开通商城：0-否 1-是")
        private Integer mallFlag;

        @ApiModelProperty("是否开通POP：0-否 1-是")
        private Integer popFlag;

        @ApiModelProperty("是否开通销售助手：0-否 1-是")
        private Integer salesAssistFlag;

        @ApiModelProperty("是否开通互联网医院：0-否 1-是")
        private Integer internetHospitalFlag;

        @ApiModelProperty("是否ERP对接：0-否 1-是")
        private Integer erpFlag;
    }
}
