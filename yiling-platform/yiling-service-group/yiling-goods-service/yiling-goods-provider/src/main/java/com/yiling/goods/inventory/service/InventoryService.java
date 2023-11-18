package com.yiling.goods.inventory.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.inventory.bo.GoodsInventoryQtyBO;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.inventory.dto.request.AddOrSubtractQtyRequest;
import com.yiling.goods.inventory.dto.request.SaveInventoryRequest;
import com.yiling.goods.inventory.entity.InventoryDO;

/**
 * <p>
 * 商品库存表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-18
 */
public interface InventoryService extends BaseService<InventoryDO> {

    /**
     * 根据id判断是添加或者修改库存信息
     * @param request
     * @return
     */
    Long save(SaveInventoryRequest request);


    /**
     * 根据gid获取用户信息
     *
     * @param skuId
     * @return
     */
    InventoryDTO getBySkuId(Long skuId);

    /**
     * 内码和gid查询库存
     * @param gid
     * @param inSn
     * @return
     */
    InventoryDTO getByGidAndInSn(Long gid,String inSn);

    /**
     * 根据gid获取B2B库存信息
     *
     * @param goodsIds
     * @return
     */
    List<InventoryDO> getB2BListByGid(List<Long> goodsIds);

    /**
     * 获取商品下面的所有库存信息列表
     * @param goodsIds
     * @return
     */
    Map<Long,List<InventoryDTO>> getInventoryMapByGids(List<Long> goodsIds);

    /**
     * 根据gid集合获取可用库存(B2B可用库存)
     *
     * @param goodsIds
     * @return key=商品ID value=可用库存
     */
    Map<Long, Long> getAvailableQtyByGoodsIds(List<Long> goodsIds);

    /**
     * 通过skuIds获取库存信息
     * @param skuIds
     * @return
     */
    Map<Long,InventoryDTO> getMapBySkuIds(List<Long> skuIds);

    /**
     * 通过库存id集合查询库存信息
     * @param ids
     * @return
     */
    List<InventoryDTO> getInventoryByIds(List<Long> ids);

    /**
     * 下单, 冻结库存加
     *
     * @param request
     * @return
     */
    int addFrozenQty(AddOrSubtractQtyRequest request);

    /**
     * 下单, 冻结库存加(批量)
     *
     * @param requestList
     * @return
     */
    int batchAddFrozenQty(List<AddOrSubtractQtyRequest> requestList);

    /**
     * 确认库存, 冻结库存减,实际库存减
     *
     * @param request
     * @return
     */
    int subtractFrozenQtyAndQty(AddOrSubtractQtyRequest request);

    /**
     * 退库存, 冻结库存减
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
     * 库存刷新接口
     * @param gids
     */
     void sendInventoryMessage(List<Long> gids);

    /**
     * 库存延时刷新接口
     * @param goodsInventoryQtyBO
     */
     void sendDelayInventoryMessage(GoodsInventoryQtyBO goodsInventoryQtyBO);

    /**
     * c端下单, 冻结库存加
     *
     * @param request
     * @return
     */
    int addHmcFrozenQty(AddOrSubtractQtyRequest request);
    /**
     * c端下单, 冻结库存加(批量)
     *
     * @param requestList
     * @return
     */
    int batchAddHmcFrozenQty(List<AddOrSubtractQtyRequest> requestList);


}
