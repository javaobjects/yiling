package com.yiling.f2b.web.mall.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupVO
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendGoodsGroupVO extends BaseVO {

    /**
     * 组名称
     */
    @ApiModelProperty(value = "组名称")
    private String name;

    /**
     * 所属企业
     */
    @ApiModelProperty(value = "所属企业")
    private Long eid;

    /**
     * 是否启用快速采购推荐位
     */
    @ApiModelProperty(value = "是否启用快速采购推荐位")
    private Integer quickPurchaseFlag;
}
