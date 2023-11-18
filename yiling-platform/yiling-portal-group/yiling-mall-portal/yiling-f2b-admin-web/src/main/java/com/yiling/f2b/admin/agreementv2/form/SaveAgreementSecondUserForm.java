package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议乙方签订人表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-08
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgreementSecondUserForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("ID（新增时无需传入，修改时传入）")
    private Long id;

    /**
     * 联系人
     */
    @Length(max = 20)
    @NotEmpty
    @ApiModelProperty(value = "联系人",required = true)
    private String name;

    /**
     * 电话
     */
    @Length(max = 20)
    @NotEmpty
    @ApiModelProperty(value = "电话",required = true)
    private String mobile;

    /**
     * 邮箱
     */
    @Length(max = 50)
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long departmentId;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departmentName;

    /**
     * 厂家类型
     */
    @Length(max = 20)
    @NotEmpty
    @ApiModelProperty(value = "厂家类型（当前默认传入商业公司）",required = true)
    private String manufacturerType;

    /**
     * 乙方ID
     */
    @NotNull
    @ApiModelProperty(value = "乙方ID",required = true)
    private Long secondEid;

    /**
     * 乙方名称
     */
    @Length(max = 50)
    @NotEmpty
    @ApiModelProperty(value = "乙方名称",required = true)
    private String secondName;

}
