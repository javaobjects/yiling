package com.yiling.goods.inventory.api;

import java.util.List;
import java.util.Map;

import com.yiling.goods.inventory.bo.GoodsInventoryQtyBO;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;

/**
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
public interface InventoryApi {

    /**
     * 获取库存集合
     * @param ids
     * @return
     */
    List<InventoryDTO> getInventoryByIds(List<Long> ids);

    /**
     * 根据商品id查询库存
     *
     * @param skuId
     * @return
     */
    InventoryDTO getBySkuId(Long skuId);

    /**
     * 根据gid获取用户信息 （返回B2B关联库存对象）
     *
     * @param goodsIds
     * @return
     */
    Map<Long,List<InventoryDTO>> getInventoryMapByGids(List<Long> goodsIds);

    /**
     * 根据gid集合获取可用库存（返回B2B可用库存）
     *
     * @param goodsIds
     * @return key=商品ID value=可用库存
     */
    Map<Long,Long> getAvailableQtyByGoodsIds(List<Long> goodsIds);

    /**
     * 根据skuIds获取库存信息
     *
     * @param skuIds
     * @return
     */
    Map<Long,InventoryDTO> getMapBySkuIds(List<Long> skuIds);

    /**
     * 根据gid判断是添加或者修改库存信息
     * @param request
     * @return
     */
    Long save(SaveInventoryRequest request);

    /**
     * (创建订单)下单, 冻结库存加
     *
     * @param request
     * @return
     */
    int addFrozenQty(AddOrSubtractQtyRequest request);

    /**
     * (创建订单)下单, 冻结库存加(批量)
     *
     * @param requestList
     * @return
     */
    int batchAddFrozenQty(List<AddOrSubtractQtyRequest> requestList);

    /**
     * （发货）确认库存, 冻结库存减,实际库存减
     *
     * @param request
     * @return
     */
    int subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request);

    /**
     * （取消） 冻结库存减
     *
     * @param request
     * @return
     */
    int subtractFrozenQty(AddOrSubtractQtyRequest request);

    /**
     * 批量退还冻结库存 ，用于库存扣减补偿机制
     * @param requestList
     * @return
     */
    int batchSubtractFrozenQty(List<AddOrSubtractQtyRequest> requestList);

    /**
     * （反审核） 冻结库增加，实际库存也增加
     *
     * @param request
     * @return
     */
    int backFrozenQtyAndQty(AddOrSubtractQtyRequest request);

    /**
     * 库存索引刷新接口
     * @param gids
     */
    void sendInventoryMessage(List<Long> gids);

    /**
     * 库存延时刷新接口
     * @param goodsInventoryQtyBO
     */
    void sendDelayInventoryMessage(GoodsInventoryQtyBO goodsInventoryQtyBO);
}
