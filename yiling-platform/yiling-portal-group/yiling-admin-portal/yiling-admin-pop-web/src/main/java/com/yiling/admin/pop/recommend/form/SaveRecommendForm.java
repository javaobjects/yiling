package com.yiling.admin.pop.recommend.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.admin.common.form.GoodsForm;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 创建banner form
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRecommendForm extends BaseForm {

    /**
     * recommend标题
     */
    @ApiModelProperty(value = "recommend标题")
    @NotEmpty
    private String title;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 商品集合
     */
    @ApiModelProperty(value = "商品集合")
    private List<GoodsForm> goodsList;
}
