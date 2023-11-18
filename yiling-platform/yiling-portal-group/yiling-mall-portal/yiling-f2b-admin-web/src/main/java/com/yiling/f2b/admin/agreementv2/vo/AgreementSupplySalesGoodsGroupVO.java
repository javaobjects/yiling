package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Range;

import com.yiling.f2b.admin.agreementv2.form.AddAgreementControlAreaForm;
import com.yiling.f2b.admin.agreementv2.form.AddAgreementSupplySalesGoodsForm;
import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销商品组 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesGoodsGroupVO extends BaseVO {

    /**
     * 控销类型：1-无 2-黑名单 3-白名单
     */
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
    @ApiModelProperty(value = "供销商品集合")
    private List<AgreementSupplySalesGoodsProVO> supplySalesGoodsList;

    /**
     * 控销条件：1-区域 2-客户类型
     */
    @ApiModelProperty("控销条件集合：1-区域 2-客户类型")
    private List<Integer> agreementControlList;

    /**
     * 控销区域
     */
    @ApiModelProperty("控销区域对象")
    private AgreementControlAreaVO controlArea;

    /**
     * 控销客户类型
     */
    @ApiModelProperty("控销客户类型集合：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private List<Integer> controlCustomerTypeList;

}
