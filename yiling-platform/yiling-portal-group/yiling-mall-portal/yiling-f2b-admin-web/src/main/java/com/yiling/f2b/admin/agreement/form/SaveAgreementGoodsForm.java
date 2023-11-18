package com.yiling.f2b.admin.agreement.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveAgreementGoodsForm extends BaseForm {
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private Long   goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 售卖规格
     */
    @ApiModelProperty(value = "售卖规格")
    private String sellSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 标准库Id
     */
    @ApiModelProperty(value = "标准库Id")
    private Long standardId;

    /**
     * 销售规格Id
     */
    @ApiModelProperty(value = "销售规格Id")
    private Long sellSpecificationsId;

    /**
     * 专利类型 1-非专利 2-专利
     */
    @ApiModelProperty(value = "专利类型 1-非专利 2-专利")
    private Integer isPatent;

}
