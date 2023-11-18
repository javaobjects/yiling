package com.yiling.sjms.wash.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUnlockCollectionDetailPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    @ApiModelProperty(value = "最后操作开始时间")
    private Date startOpTime;

    @ApiModelProperty(value = "最后操作结束时间")
    private Date endOpTime;
}
