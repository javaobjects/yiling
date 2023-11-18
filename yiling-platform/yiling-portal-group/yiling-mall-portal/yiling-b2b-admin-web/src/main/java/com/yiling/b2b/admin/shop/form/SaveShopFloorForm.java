package com.yiling.b2b.admin.shop.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-保存店铺楼层 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-20
 */
@Data
public class SaveShopFloorForm extends BaseForm {

    /**
     * ID（编辑时存在）
     */
    @ApiModelProperty("ID（编辑时存在）")
    private Long id;

    /**
     * 楼层名称
     */
    @NotEmpty
    @Length(max = 10)
    @ApiModelProperty(value = "楼层名称", required = true)
    private String name;

    /**
     * 楼层状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "楼层状态：1-启用 2-停用", required = true)
    private Integer status;

    /**
     * 权重值
     */
    @NotNull
    @Range(min = 1, max = 200)
    @ApiModelProperty(value = "权重值", required = true)
    private Integer sort;

    /**
     * 店铺商品信息
     */
    @NotEmpty
    @ApiModelProperty(value = "店铺商品信息", required = true)
    private List<@Valid SaveShopGoodsForm> shopGoodsList;

    @Data
    public static class SaveShopGoodsForm {

        /**
         * 商品ID
         */
        @NotNull
        @ApiModelProperty(value = "商品ID", required = true)
        private Long goodsId;

        /**
         * 排序值
         */
        @NotNull
        @ApiModelProperty(value = "排序值", required = true)
        private Integer sort;

    }

}
