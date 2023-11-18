package com.yiling.f2b.admin.agreementv2.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 分页查询协议乙方签订人 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementSecondUserPageForm extends QueryPageListForm {

    /**
     * 联系人
     */
    @Length(max = 20)
    @ApiModelProperty("联系人")
    private String name;

    /**
     * 电话
     */
    @Length(max = 20)
    @ApiModelProperty("电话")
    private String mobile;

    /**
     * 乙方名称
     */
    @Length(max = 50)
    @ApiModelProperty("乙方名称")
    private String secondName;

}
