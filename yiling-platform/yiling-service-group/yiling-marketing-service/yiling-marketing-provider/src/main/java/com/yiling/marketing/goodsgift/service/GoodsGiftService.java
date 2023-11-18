package com.yiling.marketing.goodsgift.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.goodsgift.dto.request.QueryGoodsGiftRequest;
import com.yiling.marketing.goodsgift.dto.request.SaveGoodsGiftRequest;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;

/**
 * <p>
 * 赠品信息表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface GoodsGiftService extends BaseService<GoodsGiftDO> {

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
    List<GoodsGiftDO> queryGoodsGiftList(QueryGoodsGiftRequest request);

    /**
     * 修改赠品库信息
     * @param request
     * @return
     */
    Boolean update(SaveGoodsGiftRequest request);

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
    List<GoodsGiftDO> activityChoiceGoodsGiftList();

    /**
     * 详情信息
     * @param id
     * @return
     */
    GoodsGiftDetailDTO getGoodsGifDetail (Long id);

    /**
     * 根据id批量查询
     *
     * @param ids
     * @return
     */
    Map<Long, Long> getAvailQuentByIds(List<Long> ids);
}
