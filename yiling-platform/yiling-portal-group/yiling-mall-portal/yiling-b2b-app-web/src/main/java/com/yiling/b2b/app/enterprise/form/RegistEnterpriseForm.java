package com.yiling.b2b.app.enterprise.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 注册企业 Form
 *
 * @author: xuan.zhou
 * @date: 2021/10/25
 */
@Data
public class RegistEnterpriseForm {

    @NotNull
    @ApiModelProperty(value = "企业类型（见字典：b2b_regist_enterprise_type）", required = true)
    private Integer type;

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "企业名称", required = true)
    private String name;

    @NotEmpty
    @Length(max = 30)
    @ApiModelProperty(value = "社会信用统一代码", required = true)
    private String licenseNumber;

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属省份编码", required = true)
    private String provinceCode;

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属城市编码", required = true)
    private String cityCode;

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属区域编码", required = true)
    private String regionCode;

    @NotEmpty
    @Length(max = 100)
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "联系人姓名", required = true)
    private String contactor;

    @NotEmpty
    @Length(max = 20)
    @Pattern(regexp = Constants.REGEXP_MOBILE)
    @ApiModelProperty(value = "联系人电话", required = true)
    private String contactorPhone;

    @Length(max = 20)
    @Pattern(regexp = Constants.REGEXP_PASSWORD)
    @ApiModelProperty(value = "登录密码")
    private String password;

    @NotEmpty
    @ApiModelProperty(value = "企业资质列表", required = true)
    private List<EnterpriseCertificateForm> certificateList;

    @Data
    public static class EnterpriseCertificateForm {

        /**
         * 资质类型（参见EnterpriseCertificateTypeEnum）
         */
        @ApiModelProperty(value = "资质类型")
        private Integer type;

        /**
         * 资质文件KEY
         */
        @ApiModelProperty("资质文件KEY")
        private String fileKey;

        /**
         * 资质有效期-起
         */
        @ApiModelProperty("资质有效期-起")
        private Date periodBegin;

        /**
         * 资质有效期-止
         */
        @ApiModelProperty("资质有效期-止")
        private Date periodEnd;

        /**
         * 是否长期有效：0-否 1-是
         */
        @ApiModelProperty("是否长期有效：0-否 1-是")
        private Integer longEffective;
    }
}
