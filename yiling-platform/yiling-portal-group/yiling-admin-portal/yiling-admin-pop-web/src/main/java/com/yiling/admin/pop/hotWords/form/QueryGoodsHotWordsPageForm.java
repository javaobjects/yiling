package com.yiling.admin.pop.hotWords.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsHotWordsPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "热词名称")
    private String name;

    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer state;

    @ApiModelProperty(value = "投放开始时间")
    private Date startTime;

    @ApiModelProperty(value = "投放结束时间")
    private Date endTime;

    @ApiModelProperty(value = "开始创建时间")
    private Date starCreateTime;

    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;
}
