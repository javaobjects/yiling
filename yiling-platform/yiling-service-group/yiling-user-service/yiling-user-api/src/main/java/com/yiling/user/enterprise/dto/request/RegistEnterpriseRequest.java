package com.yiling.user.enterprise.dto.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.enterprise.enums.EnterpriseSourceEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 注册企业 Form
 *
 * @author: xuan.zhou
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RegistEnterpriseRequest extends BaseRequest {

    @NotNull
    private Integer type;

    @NotEmpty
    @Length(max = 20)
    private String name;

    @NotEmpty
    @Length(max = 18)
    private String licenseNumber;

    @NotEmpty
    @Length(max = 20)
    private String provinceCode;

    @NotEmpty
    @Length(max = 50)
    private String provinceName;

    @NotEmpty
    @Length(max = 20)
    private String cityCode;

    @NotEmpty
    @Length(max = 50)
    private String cityName;

    @NotEmpty
    @Length(max = 20)
    private String regionCode;

    @NotEmpty
    @Length(max = 50)
    private String regionName;

    @NotEmpty
    @Length(max = 100)
    private String address;

    @NotEmpty
    @Length(max = 20)
    private String contactor;

    @NotEmpty
    @Length(max = 20)
    @Pattern(regexp = Constants.REGEXP_MOBILE)
    private String contactorPhone;

    @NotEmpty
    @Length(max = 20)
    @Pattern(regexp = Constants.REGEXP_PASSWORD)
    private String password;

    @NotEmpty
    @ApiModelProperty(value = "企业资质列表", required = true)
    private List<CreateEnterpriseCertificateRequest> certificateList;

    /**
     * 企业来源
     */
    private EnterpriseSourceEnum enterpriseSourceEnum;
}
