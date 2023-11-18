package com.yiling.sjms.crm.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryCrmGoodsGroupPageForm
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmGoodsGroupPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "产品组编码")
    private String groupCode;

    @ApiModelProperty(value = "产品组名称")
    private String groupName;

    @ApiModelProperty(value = "业务部门")
    private String department;


}
