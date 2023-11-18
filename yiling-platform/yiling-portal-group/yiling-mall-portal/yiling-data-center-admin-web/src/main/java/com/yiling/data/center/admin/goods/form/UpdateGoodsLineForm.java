package com.yiling.data.center.admin.goods.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGoodsLineForm extends BaseForm {

    /**
     * 开通产品线
     */
    @ApiModelProperty(value = "开通产品线")
    private List<GoodsLineForm> goodsLineList;


}
