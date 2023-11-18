package com.yiling.admin.hmc.enterprise.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSaveListForm extends BaseForm {

    @ApiModelProperty("商家id")
    @NotNull(message = "没有选择商家")
    private Long eid;

    @ApiModelProperty("商家名称")
    @NotNull(message = "没有选择商家")
    private String ename;

    @ApiModelProperty("C端保险药品商家提成信息")
    @NotEmpty(message = "保险药品商家提成信息不能为空")
    private List<GoodsSaveForm> goodsRequest;
}
