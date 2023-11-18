package com.yiling.goods.medicine.service;

import java.util.List;

import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.HmcSaveOrUpdateGoodsRequest;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;

/**
 * @author shichen
 * @类名 HmcGoodsService
 * @描述
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
public interface HmcGoodsService {
    /**
     * c端生成商品
     * @param request
     * @return
     */
    Long generateGoods(HmcSaveOrUpdateGoodsRequest request);

    /**
     * 根据goodsId查询商品基础信息，库存，标准库分类信息，标准库信息
     *
     * @param goodsIds
     * @return
     */
    List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasic(List<Long> goodsIds);

    /**
     * 根据specificationsIds查询商品基础信息，库存，标准库分类信息，标准库信息
     *
     * @param specificationsIds
     * @return
     */
    List<StandardGoodsBasicDTO> batchQueryStandardGoodsBasicBySpecificationsIds(List<Long> specificationsIds);

    /**
     * 通过商品skuId修改实际库存数量
     * @param skuId
     * @param inventoryQty
     * @param opUserId
     * @return
     */
    Long updateGoodsInventoryBySku(Long skuId,Long inventoryQty,Long opUserId);

    /**
     * 确认库存, 冻结库存减,实际库存减
     *
     * @param request
     * @return
     */
    boolean subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request);


    /**
     *  确认库存, 冻结库存减,实际库存减(批量)
     * @param requestList
     * @return
     */
    int batchSubtractFrozenQtyAndQty(List<AddOrSubtractQtyRequest> requestList);

    /**
     * 冻结库存加
     *
     * @param request
     * @return
     */
    boolean addHmcFrozenQty(AddOrSubtractQtyRequest request);
    /**
     * 下单, 冻结库存加(批量)
     *
     * @param requestList
     * @return
     */
    int batchAddHmcFrozenQty(List<AddOrSubtractQtyRequest> requestList);

    /**
     * 退库存, 冻结库存减
     *
     * @param request
     * @return
     */
    int subtractHmcFrozenQty(AddOrSubtractQtyRequest request);

    /**
     * 标准库id查询标准库商品
     * @param standardIds
     * @return
     */
    List<StandardGoodsInfoDTO> queryStandardGoodsByStandardIds(List<Long> standardIds);
}
