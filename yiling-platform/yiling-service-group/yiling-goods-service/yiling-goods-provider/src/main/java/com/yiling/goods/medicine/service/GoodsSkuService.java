package com.yiling.goods.medicine.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.inventory.dto.InventorySubscriptionDTO;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsSkuRequest;
import com.yiling.goods.medicine.entity.GoodsSkuDO;
import com.yiling.goods.medicine.enums.GoodsLineEnum;

/**
 * <p>
 * 商品sku 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-19
 */
public interface GoodsSkuService extends BaseService<GoodsSkuDO> {

    /**
     * 添加销售包装数据
     *
     * @param request
     * @return
     */
    Long saveOrUpdateByNumber(SaveOrUpdateGoodsSkuRequest request);

    /**
     * 添加销售包装数据
     *
     * @param skuIds
     * @param operUser
     * @return
     */
    Boolean deleteGoodsSku(List<Long> skuIds,Long operUser);

    /**
     * 获取销售包装库存IdList集合
     * @param goodsId
     * @return
     */
    List<Long> getB2bInventoryByGoodsId(Long goodsId);

    /**
     * 根据goodsId获取正常状态sku
     * @param goodsId
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsId(Long goodsId);

    /**
     * 根据goodsId和状态获取sku
     * @param goodsId
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsIdAndStatus(Long goodsId,List<Integer> statusList);
    /**
     * 根据goodsIds获取销售包装信息
     * @param goodsIds
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsIds(List<Long> goodsIds);

    /**
     * 根据goodsIds和状态获取销售包装信息
     * @param goodsIds
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGoodsIdsAndStatus(List<Long> goodsIds,List<Integer> statusList);

    /**
     * 获取销售包装信息
     * @param eid
     * @param inSn
     * @param goodsLine
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuListByEidAndInSnAndGoodsLine(Long eid,String inSn,Integer goodsLine);

    /**
     * 企业id和内码更新状态
     * @param eid
     * @param inSn
     * @param status
     * @return
     */
    Boolean updateStatusByEidAndInSn(Long eid, String inSn, Integer status,Long updater);

    /**
     * 根据商品id修改sku信息
     * @param request
     * @return
     */
    Boolean updateByGoodsId(SaveOrUpdateGoodsSkuRequest request);

    /**
     * skuIds获取sku列表
     * @param skuIds
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByIds(List<Long> skuIds);

    /**
     * skuId获取sku对象
     * @return
     */
    GoodsSkuDTO getGoodsSkuById(Long skuId);

    /**
     * 商品id和产品线查找sku列表
     * @param goodsId
     * @param lineEnum
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGidAndGoodsLine(Long goodsId, GoodsLineEnum lineEnum);

    /**
     *
     * @param goodsId
     * @param inventoryId
     * @return
     */
    List<GoodsSkuDTO> getGoodsSkuByGidAndInventoryId(Long goodsId,Long inventoryId);
}
