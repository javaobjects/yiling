package com.yiling.sjms.crm.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCrmGoodsTagRelationPageForm
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmGoodsTagRelationPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "标签id")
    private Long tagId;

    @ApiModelProperty(value = "产品编码或产品名称")
    private String keyword;

}
