package com.yiling.hmc.medinstruct.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardGoodsInfoForm extends QueryPageListForm {

    private static final long serialVersionUID = -333710304281212221L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;
//
//    /**
//     * 有无图片 1有 2无
//     */
//    @ApiModelProperty(value = "有无图片：1-有 2-无")
//    private Integer pictureFlag;

}
