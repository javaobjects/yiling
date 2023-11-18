package com.yiling.dataflow.crm.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsTagRelationDTO
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmGoodsTagRelationDTO extends BaseDTO {

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 商品id
     */
    private Long crmGoodsId;
}
