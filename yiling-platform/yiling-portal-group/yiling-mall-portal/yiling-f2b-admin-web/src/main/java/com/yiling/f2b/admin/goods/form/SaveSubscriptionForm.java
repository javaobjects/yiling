package com.yiling.f2b.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveSubscriptionForm
 * @描述
 * @创建时间 2022/11/28
 * @修改人 shichen
 * @修改时间 2022/11/28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSubscriptionForm extends BaseForm {

    @ApiModelProperty(value = "订阅关系id", example = "1")
    private Long id;

    /**
     * 被订阅企业id
     */
    @ApiModelProperty(value = "被订阅企业id", example = "1")
    private Long subscriptionEid;

    /**
     * 订阅企业名称
     */
    @ApiModelProperty(value = "订阅企业名称", example = "以岭")
    private String subscriptionEname;

    /**
     * 内码
     */
    @ApiModelProperty(value = "订阅内码")
    private String inSn;

    /**
     * 订阅类型 1 本店 2：erp订阅 3：pop订阅
     */
    @ApiModelProperty(value = "订阅类型 1 本店 2：erp订阅 3：pop订阅", example = "2")
    private Integer subscriptionType;

}
