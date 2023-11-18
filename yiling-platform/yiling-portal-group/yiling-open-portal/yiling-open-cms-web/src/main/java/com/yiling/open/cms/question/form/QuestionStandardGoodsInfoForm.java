package com.yiling.open.cms.question.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @describe 疑问库关联商品信息
 * @author:wei.wang
 * @date:2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuestionStandardGoodsInfoForm extends BaseRequest {

    /**
     * 标准库id
     */
    @ApiModelProperty("标准库id")
    @NotNull
    private Long standardId;

    /**
     * 标准库商品规格ID
     */
    @ApiModelProperty("标准库商品规格ID")
    @NotNull
    private Long sellSpecificationsId;
}
