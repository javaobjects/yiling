package com.yiling.admin.data.center.enterprise.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 企业详情页 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
public class EnterpriseDetailPageVO {

    @ApiModelProperty("企业信息")
    private EnterpriseVO enterpriseInfo;

    @ApiModelProperty("企业销售区域描述")
    private String salesAreaDescription;

    @ApiModelProperty("归属企业列表")
    private List<SubEnterpriseVO> subEnterpriseList;

    @ApiModelProperty("平台产品线开通信息")
    private PlatformInfo platformInfo;

    @Deprecated
    @ApiModelProperty("管理员账号（已废弃）")
    private String adminAccount;

    @ApiModelProperty("管理员信息列表")
    private List<ManagerInfo> managerList;

    @ApiModelProperty("企业资质列表")
    private List<EnterpriseCertificateVO> enterpriseCertificateList;

    @Data
    public static class SubEnterpriseVO {

        @ApiModelProperty("企业ID")
        private Long id;

        @ApiModelProperty("企业名称")
        private String name;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("编码")
        private String erpCode;
    }

    @Data
    public static class PlatformInfo {

        @ApiModelProperty("是否开通B2B：0-否 1-是")
        private Integer mallFlag;
        @ApiModelProperty("是否显示开通B2B：0-否 1-是")
        private Integer mallVisiableFlag;
        @ApiModelProperty("是否能操作开通B2B：0-否 1-是")
        private Integer mallEnableFlag;

        @ApiModelProperty("是否开通POP：0-否 1-是")
        private Integer popFlag;
        @ApiModelProperty("是否显示开通POP：0-否 1-是")
        private Integer popVisiableFlag;
        @ApiModelProperty("是否能操作开通POP：0-否 1-是")
        private Integer popEnableFlag;

        @ApiModelProperty("是否开通销售助手：0-否 1-是")
        private Integer salesAssistFlag;
        @ApiModelProperty("是否显示开通销售助手：0-否 1-是")
        private Integer salesAssistVisiableFlag;
        @ApiModelProperty("是否能操作开通销售助手：0-否 1-是")
        private Integer salesAssistEnableFlag;

        @ApiModelProperty("是否开通C端药+险：0-否 1-是")
        private Integer hmcFlag;
        @ApiModelProperty("是否显示开通C端药+险：0-否 1-是")
        private Integer hmcVisiableFlag;
        @ApiModelProperty("是否能操作开通C端药+险：0-否 1-是")
        private Integer hmcEnableFlag;
    }

    @Data
    public static class ManagerInfo {

        @ApiModelProperty("管理员用户ID")
        private Long userId;

        @ApiModelProperty("管理员姓名")
        private String name;

        @ApiModelProperty("管理员手机号")
        private String mobile;

        @ApiModelProperty("状态：1-启用 2-停用 3-已冻结 9-已注销")
        private Integer status;
    }

}
