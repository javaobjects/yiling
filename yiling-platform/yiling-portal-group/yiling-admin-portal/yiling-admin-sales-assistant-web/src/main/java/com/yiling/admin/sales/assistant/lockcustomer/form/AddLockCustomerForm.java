package com.yiling.admin.sales.assistant.lockcustomer.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加销售助手-锁定用户 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddLockCustomerForm extends BaseForm {

    /**
     * 企业名称
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "企业名称", required = true)
    private String name;

    /**
     * 执业许可证号/社会信用统一代码
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "执业许可证号/社会信用统一代码", required = true)
    private String licenseNumber;

    /**
     * 所属省份编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属省份编码", required = true)
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属城市编码", required = true)
    private String cityCode;

    /**
     * 所属区域编码
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属区域编码", required = true)
    private String regionCode;

    /**
     * 详细地址
     */
    @NotEmpty
    @Length(max = 100)
    @ApiModelProperty(value = "详细地址", required = true)
    private String address;

    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @NotNull
    @ApiModelProperty(value = "类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所（见企业类型的字典）", required = true)
    private Integer type;

    /**
     * 所属板块
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "所属板块", required = true)
    private String plate;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;

}
