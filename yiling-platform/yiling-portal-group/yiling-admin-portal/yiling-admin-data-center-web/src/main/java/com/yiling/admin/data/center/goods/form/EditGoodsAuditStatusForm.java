package com.yiling.admin.data.center.goods.form;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Data
public class EditGoodsAuditStatusForm {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 驳回信息
     */
    @ApiModelProperty(value = "驳回信息")
    @Size(min=0, max=200)
    private String rejectMessage;
}
