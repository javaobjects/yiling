package com.yiling.f2b.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveInventorySubscriptionForm
 * @描述
 * @创建时间 2022/11/28
 * @修改人 shichen
 * @修改时间 2022/11/28
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveInventorySubscriptionForm extends BaseForm {

    /**
     * 库存Id
     */
    @ApiModelProperty(value = "库存Id", example = "1")
    private Long inventoryId;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId", example = "1")
    private Long skuId;

    /**
     * 订阅方企业id
     */
    @ApiModelProperty(value = "订阅方企业id", example = "1")
    private Long eid;
    /**
     * 订阅列表
     */
    @ApiModelProperty(value = "订阅列表")
    private List<SaveSubscriptionForm> subscriptionList;

}
