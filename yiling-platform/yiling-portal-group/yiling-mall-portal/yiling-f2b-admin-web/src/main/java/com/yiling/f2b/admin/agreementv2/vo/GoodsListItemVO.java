package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@Accessors(chain = true)
public class GoodsListItemVO {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 规格商品ID
     */
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 生产厂家名称
     */
    private String manufacturerName;

}
