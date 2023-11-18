package com.yiling.admin.hmc.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: wei.wang
 * @date: 2021/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardSpecificationPageForm extends QueryPageListForm {


    /**
     * 标准库商品id
     */
    @ApiModelProperty(value = "标准库商品id")
   private Long standardId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
   private String name;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
   private String licenseNo;

}
