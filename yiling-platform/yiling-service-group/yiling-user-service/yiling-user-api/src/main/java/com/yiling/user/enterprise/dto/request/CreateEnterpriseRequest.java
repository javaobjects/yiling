package com.yiling.user.enterprise.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加企业信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateEnterpriseRequest extends BaseRequest{

    /**
     * 企业名称
     */
    @NotEmpty
    private String name;

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
    @NotEmpty
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @NotEmpty
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @NotEmpty
    private String provinceName;

    /**
     * 所属城市编码
     */
    @NotEmpty
    private String cityCode;

    /**
     * 所属城市名称
     */
    @NotEmpty
    private String cityName;

    /**
     * 所属区域编码
     */
    @NotEmpty
    private String regionCode;

    /**
     * 所属区域名称
     */
    @NotEmpty
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
     * 类型（参见EnterpriseTypeEnum）
     */
    @NotNull
    private Integer type;

    /**
     * 来源(参见EnterpriseSourceEnum)
     */
    @NotNull
    private Integer source;

    /**
     * 状态（参见EnterpriseAuthStatusEnum）
     */
    @NotNull
    private Integer authStatus;

    /**
     * 审核人ID
     */
    @NotNull
    private Long authUser;

    /**
     * 审核时间
     */
    @NotNull
    private Date authTime;

    /**
     * 状态（参见EnableStatusEnum）
     */
    @NotNull
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 企业资质列表
     */
    private List<CreateEnterpriseCertificateRequest> certificateList;

    /**
     * 开通产品线
     */
    private List<PlatformEnum> platformEnumList;

    /**
     * 渠道类型ID
     */
    private Long channelId;

    /**
     * 药+险业务类型
     */
    private EnterpriseHmcTypeEnum hmcTypeEnum;

    /**
     * 企业管理员账号的密码（为空表示使用默认密码）
     */
    private String password;

}
