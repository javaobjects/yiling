package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseForm extends BaseForm {

    /**
     * 企业ID
     */
    @NotNull
    private Long id;

    /**
     * 企业名称
     */
    @NotEmpty
    private String name;

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
     * 所属城市编码
     */
    @NotEmpty
    private String cityCode;

    /**
     * 所属区域编码
     */
    @NotEmpty
    private String regionCode;

    /**
     * 详细地址
     */
    @NotEmpty
    private String address;

    /**
     * 联系人
     */
    @NotEmpty
    private String contactor;

    /**
     * 联系人电话
     */
    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "联系人电话格式不正确")
    private String contactorPhone;
}
