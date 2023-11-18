package com.yiling.sjms.crm.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 QueryTagGoodsInfoPageForm
 * @描述
 * @创建时间 2023/4/11
 * @修改人 shichen
 * @修改时间 2023/4/11
 **/
@Data
public class QueryTagGoodsInfoPageForm extends QueryGoodsInfoPageForm {

    @ApiModelProperty(value = "标签id")
    private Long tagId;
}
