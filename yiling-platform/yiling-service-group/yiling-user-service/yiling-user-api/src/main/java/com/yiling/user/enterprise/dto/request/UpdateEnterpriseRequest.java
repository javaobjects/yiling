package com.yiling.user.enterprise.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseRequest extends BaseRequest{

    /**
     * 企业ID
     */
    @NotNull
    private Long id;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 企业Logo
     */
    private String logo;

    /**
     * 法人姓名
     */
    private String legal;

    /**
     * 法人身份证号
     */
    private String legalIdNumber;

    /**
     * 法人授权委托书
     */
    private String legalAttorney;

    /**
     * 联系人
     */
    private String contactor;

    /**
     * 联系人电话
     */
    private String contactorPhone;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所属企业ID
     */
    private Long parentId;

    /**
     * 渠道ID
     */
    private Long channelId;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    private Integer type;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过 4-认证中
     */
    private Integer authStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 企业资质列表
     */
    private List<UpdateEnterpriseCertificateRequest> certificateList;

}
