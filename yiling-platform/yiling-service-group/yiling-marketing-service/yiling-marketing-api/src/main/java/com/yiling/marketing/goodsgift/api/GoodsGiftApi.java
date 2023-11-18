package com.yiling.marketing.goodsgift.api;

import java.util.List;
import java.util.Map;

import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.goodsgift.dto.request.QueryGoodsGiftRequest;
import com.yiling.marketing.goodsgift.dto.request.SaveGoodsGiftRequest;

/**
 * 赠品 API
 *
 * @author: houjie.sun
 * @date: 2021/10/23
 */
public interface GoodsGiftApi {
    /**
     * 保存赠品库信息
     * @param request
     * @return
     */
    Boolean save(SaveGoodsGiftRequest request);

    /**
     * 赠品列表查询
     * @param request
     * @return
     */
    List<GoodsGiftDTO> queryGoodsGiftList(QueryGoodsGiftRequest request);

    /**
     * 根据id获取赠品信息
     * @param id
     * @return
     */
    GoodsGiftDTO getOneById(Long id);

    /**
     * 修改赠品库信息
     * @param request
     * @return
     */
    Boolean update(SaveGoodsGiftRequest request);

    /**
     *
     * @param id
     * @param opUserId 操作人
     * @return
     */
    Boolean delete(Long id,Long opUserId);

    /**
     * 扣减赠品库存数量
     * @param quantity 变换的数量
     * @param id 赠品id
     * @return
     */
    boolean deduct(Integer quantity,Long id);

    /**
     * 返还赠品库存数量
     * @param quantity 变换的数量
     * @param id 赠品id
     * @return
     */
    boolean increase(Integer quantity,Long id);

    /**
     * 活动选择赠品
     * @return
     */
    List<GoodsGiftDTO> activityChoiceGoodsGiftList();

    /**
     * 根据id批量查询
     * @param ids
     * @return
     */
    List<GoodsGiftDTO> listByListId(List<Long> ids);

    /**
     * 根据id批量查询
     * @param ids
     * @return
     */
    Map<Long,Long> getAvailQuentByIds(List<Long> ids);

    /**
     * 详情信息
     * @param id
     * @return
     */
    GoodsGiftDetailDTO getGoodsGifDetail (Long id);
}
