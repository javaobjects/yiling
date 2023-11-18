package com.yiling.admin.erp.enterprisecustomer.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 BindCustomerForm
 * @描述
 * @创建时间 2022/1/13
 * @修改人 shichen
 * @修改时间 2022/1/13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "绑定客户参数")
public class BindCustomerForm extends BaseForm {

    /**
     * 客户eid
     */
    @ApiModelProperty(value = "客户eid")
    private Long customerEid;

    /**
     * open库erp客户
     */
    @ApiModelProperty(value = "open库erp客户")
    private ErpCustomerForm erpCustomer;

    /**
     * 天眼查企业信息
     */
    @ApiModelProperty(value = "天眼查企业信息")
    private TycEnterpriseInfoForm tycEnterprise;
}
