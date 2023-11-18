package com.yiling.sjms.crm.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateCrmGoodsTagForm
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmGoodsTagForm extends BaseForm {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String name;

    /**
     * 标签描述
     */
    @ApiModelProperty(value = "标签描述")
    private String description;

    /**
     * 类型 1：非锁标签  2：团购标签
     */
    @ApiModelProperty(value = "类型 1：非锁标签  2：团购标签")
    private Integer type;
}
