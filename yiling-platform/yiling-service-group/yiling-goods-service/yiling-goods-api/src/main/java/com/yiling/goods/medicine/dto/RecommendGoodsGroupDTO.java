package com.yiling.goods.medicine.dto;

import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupDTO
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecommendGoodsGroupDTO extends BaseDTO {

    /**
     * 组名称
     */
    private String name;

    /**
     * 所属企业
     */
    private Long eid;

    /**
     * 是否启用快速采购推荐位 0：启用 1不启用
     */
    private Integer quickPurchaseFlag;

    /**
     * 关联商品列表
     */
    private List<RecommendGoodsGroupRelationDTO> relationList;
}
