package com.yiling.sjms.crm.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 CrmTagGoodsInfoVO
 * @描述
 * @创建时间 2023/4/11
 * @修改人 shichen
 * @修改时间 2023/4/11
 **/
@Data
public class CrmTagGoodsInfoVO extends BaseCrmGoodsInfoVO {

    @ApiModelProperty(value = "是否绑定标签")
    private Boolean tagFlag;
}
