package com.yiling.f2b.admin.agreementv2.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询甲方 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/24
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFirstPartyPageForm extends QueryPageListForm {

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    @ApiModelProperty(value = "甲方类型：1-工业生产厂家 2-工业品牌厂家 3-商业供应商 4-代理商（选择甲方时必填，见字典：agreement_first_type）")
    private Integer firstType;

    /**
     * 甲方/乙方名称
     */
    @Length(max = 50)
    @ApiModelProperty("甲方/乙方名称")
    private String ename;

}
