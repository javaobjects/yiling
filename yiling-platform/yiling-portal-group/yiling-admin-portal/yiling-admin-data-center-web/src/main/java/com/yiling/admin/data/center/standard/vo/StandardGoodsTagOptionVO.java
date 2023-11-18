package com.yiling.admin.data.center.standard.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 StandardGoodsTagOptionVO
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class StandardGoodsTagOptionVO {
    @ApiModelProperty("标签ID")
    private Long id;

    @ApiModelProperty("标签名称")
    private String name;

    @ApiModelProperty("是否选中")
    private boolean checked;
}
