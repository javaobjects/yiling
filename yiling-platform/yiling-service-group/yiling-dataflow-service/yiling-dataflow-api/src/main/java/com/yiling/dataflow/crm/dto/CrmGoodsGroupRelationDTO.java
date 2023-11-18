package com.yiling.dataflow.crm.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsGroupRelationDTO
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsGroupRelationDTO extends BaseDTO {

    /**
     * 商品组ID
     */
    private Long groupId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品编码
     */
    private Long goodsCode;

    /**
     * 0:启用，1停用
     */
    private Integer status;
}
