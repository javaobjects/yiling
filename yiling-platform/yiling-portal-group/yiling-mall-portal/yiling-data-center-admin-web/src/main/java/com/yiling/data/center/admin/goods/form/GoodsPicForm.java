package com.yiling.data.center.admin.goods.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPicForm extends BaseForm {

    /**
     * 商品图片值
     */
    @ApiModelProperty(value = "商品图片值")
    private String pic;

    /**
     * 图片排序
     */
    @ApiModelProperty(value = "图片排序")
    private Integer picOrder;

    /**
     * 是否商品默认图片（0否1是）
     */
    @ApiModelProperty(value = "是否商品默认图片：0-否 1-是")
    private Integer picDefault;

}
