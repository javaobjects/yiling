package com.yiling.sales.assistant.app.usercustomer.form;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户客户 Form
 * 
 * @author lun.yu
 * @date 2021/9/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserCustomerForm extends QueryPageListForm {

    /**
     * 企业名称或联系电话
     */
    @Length(max = 30)
    @ApiModelProperty(value = "企业名称或联系电话")
    private String nameOrPhone;

    /**
     * 状态
     */
    @Range(min = 1,max = 3)
    @ApiModelProperty(value = "状态：1-待审核 2-审核通过 3-审核驳回")
    private Integer status;

    /**
     * 所属城市编码集合
     */
    @ApiModelProperty(value = "所属城市编码集合")
    private List<Long> cityCodeList;

}
