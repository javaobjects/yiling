package com.yiling.b2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCustomerRecommendationForm extends BaseForm {

    /**
     * 是否推荐
     */
    @ApiModelProperty("是否推荐：0否 1是")
    private Integer recommendationFlag;

    /**
     * 客户Id
     */
    @ApiModelProperty("客户Id")
    private List<Long> customerEids;
}
