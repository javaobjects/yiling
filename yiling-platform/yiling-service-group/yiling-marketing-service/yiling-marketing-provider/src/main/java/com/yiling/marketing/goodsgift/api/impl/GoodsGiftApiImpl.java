package com.yiling.marketing.goodsgift.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.goodsgift.dto.request.QueryGoodsGiftRequest;
import com.yiling.marketing.goodsgift.dto.request.SaveGoodsGiftRequest;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;

/**
 * @author:wei.wang
 * @date:2021/11/2
 */
@DubboService
public class GoodsGiftApiImpl implements GoodsGiftApi {

    @Autowired
    private GoodsGiftService goodsGiftService;

    /**
     * 保存赠品库信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean save(SaveGoodsGiftRequest request) {
        return goodsGiftService.save(request);
    }

    /**
     * 赠品列表查询
     *
     * @param request
     * @return
     */
    @Override
    public List<GoodsGiftDTO> queryGoodsGiftList(QueryGoodsGiftRequest request) {
        List<GoodsGiftDO> goodsGiftDOS = goodsGiftService.queryGoodsGiftList(request);
        return PojoUtils.map(goodsGiftDOS,GoodsGiftDTO.class);
    }

    /**
     * 根据id获取赠品信息
     *
     * @param id
     * @return
     */
    @Override
    public GoodsGiftDTO getOneById(Long id) {
        GoodsGiftDO goodsGiftDO = goodsGiftService.getById(id);
        return PojoUtils.map(goodsGiftDO,GoodsGiftDTO.class);
    }

    /**
     * 修改赠品库信息
     * @param request
     * @return
     */
    @Override
    public Boolean update(SaveGoodsGiftRequest request) {
        return goodsGiftService.update(request);
    }

    /**
     * @param id
     * @param opUserId 操作人
     * @return
     */
    @Override
    public Boolean delete(Long id, Long opUserId) {
        GoodsGiftDO giftDO = new GoodsGiftDO();
        giftDO.setId(id);
        giftDO.setUpdateUser(opUserId);
        giftDO.setUpdateTime(new Date());
        return  goodsGiftService.deleteByIdWithFill(giftDO) > 0;
    }

    /**
     * 扣减赠品库存数量
     *
     * @param quantity 数量
     * @param id       赠品id
     * @return
     */
    @Override
    public boolean deduct(Integer quantity, Long id) {
        return goodsGiftService.deduct(quantity,id);
    }

    /**
     * 返还赠品库存数量
     *
     * @param quantity          变换的数量
     * @param id                赠品id
     * @return
     */
    @Override
    public boolean increase(Integer quantity, Long id) {
        return goodsGiftService.increase(quantity,id);
    }

    /**
     * 活动选择赠品
     *
     * @return
     */
    @Override
    public List<GoodsGiftDTO> activityChoiceGoodsGiftList() {
        return PojoUtils.map(goodsGiftService.activityChoiceGoodsGiftList(),GoodsGiftDTO.class);
    }

    /**
     * 根据id批量查询
     *
     * @param ids
     * @return
     */
    @Override
    public List<GoodsGiftDTO> listByListId(List<Long> ids) {
        List<GoodsGiftDO> goodsGiftList = goodsGiftService.listByIds(ids);
        return PojoUtils.map(goodsGiftList,GoodsGiftDTO.class) ;
    }

    @Override
    public Map<Long, Long> getAvailQuentByIds(List<Long> ids) {
        return goodsGiftService.getAvailQuentByIds(ids);
    }

    /**
     * 详情信息
     *
     * @param id
     * @return
     */
    @Override
    public GoodsGiftDetailDTO getGoodsGifDetail(Long id) {

        return  goodsGiftService.getGoodsGifDetail(id);
    }


}
