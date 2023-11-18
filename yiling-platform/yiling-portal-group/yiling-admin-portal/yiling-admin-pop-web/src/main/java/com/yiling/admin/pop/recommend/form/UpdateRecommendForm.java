package com.yiling.admin.pop.recommend.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.admin.common.form.GoodsForm;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 编辑banner Form
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRecommendForm extends BaseForm {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "recommendId")
    @NotNull
    private Long id;

    /**
     * recommend标题
     */
    @ApiModelProperty(value = "recommend标题")
    @NotBlank
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
