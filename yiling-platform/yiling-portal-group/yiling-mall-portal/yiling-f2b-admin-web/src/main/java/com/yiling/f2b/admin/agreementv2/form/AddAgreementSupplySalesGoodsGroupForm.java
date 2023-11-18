package com.yiling.f2b.admin.agreementv2.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议供销商品组 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementSupplySalesGoodsGroupForm extends BaseForm {

    /**
     * 控销类型：1-无 2-黑名单 3-白名单
     */
    @Range(min = 1, max = 3)
    @ApiModelProperty("控销类型：1-无 2-黑名单 3-白名单")
    private Integer controlSaleType;


    /**
     * 出库价含税是否维价：0-否 1-是
     */
    @ApiModelProperty("出库价含税是否维价")
    private Boolean exitWarehouseTaxPriceFlag;

    /**
     * 零售价含税是否维价：0-否 1-是
     */
    @ApiModelProperty("零售价含税是否维价")
    private Boolean retailTaxPriceFlag;

    /**
     * 供销商品集合
     */
    @ApiModelProperty(value = "供销商品集合",required = true)
    private List<AddAgreementSupplySalesGoodsForm> supplySalesGoodsList;

    /**
     * 控销条件：1-区域 2-客户类型
     */
    @ApiModelProperty("控销条件集合：1-区域 2-客户类型（只有控销类型不选择无，才可能会有控销条件，允许为空）")
    private List<Integer> agreementControlList;

    /**
     * 控销区域
     */
    @ApiModelProperty("控销区域对象（只有勾选区域后，才必须有值）")
    private AddAgreementControlAreaForm controlArea;

    /**
     * 控销客户类型
     */
    @ApiModelProperty("控销客户类型集合：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所（只有勾选客户类型后，才必须有值）")
    private List<Integer> controlCustomerTypeList;

}
