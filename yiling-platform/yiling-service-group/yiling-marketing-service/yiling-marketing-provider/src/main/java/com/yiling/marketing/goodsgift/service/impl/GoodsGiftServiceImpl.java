package com.yiling.marketing.goodsgift.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.goodsgift.dao.GoodsGiftMapper;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.goodsgift.dto.request.CardRequest;
import com.yiling.marketing.goodsgift.dto.request.QueryGoodsGiftRequest;
import com.yiling.marketing.goodsgift.dto.request.SaveGoodsGiftRequest;
import com.yiling.marketing.goodsgift.entity.GoodsGiftCouponActivityDO;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.entity.GoodsGiftMemberDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftCouponActivityService;
import com.yiling.marketing.goodsgift.service.GoodsGiftMemberService;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 赠品信息表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Service
public class GoodsGiftServiceImpl extends BaseServiceImpl<GoodsGiftMapper, GoodsGiftDO> implements GoodsGiftService {

    @Autowired
    private GoodsGiftMemberService         goodsGiftMemberService;
    @Autowired
    private GoodsGiftCouponActivityService goodsGiftCouponActivityService;

    /**
     * 保存赠品库信息
     *
     * @param request
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(SaveGoodsGiftRequest request) {
        GoodsGiftDTO giftDTO = PojoUtils.map(request, GoodsGiftDTO.class);
        giftDTO.setPictureUrl(request.getPictureKey());
        giftDTO.setAvailableQuantity(giftDTO.getSafeQuantity());
        giftDTO.setIntroduction(request.getIntroduction());
        if (request.getGoodsType() == 2) {
            List<CardRequest> cardList = request.getCardList();
            if (CollectionUtil.isNotEmpty(cardList)) {
                String cardNo = cardList.stream().map(CardRequest::getCardNo).collect(Collectors.joining(","));
                String password = cardList.stream().map(CardRequest::getPassword).collect(Collectors.joining(","));
                giftDTO.setCardNo(cardNo);
                giftDTO.setPassword(password);
            }
        }
        boolean result=this.baseMapper.saveGoodsGiftDO(giftDTO);
        return result;
    }

    /**
     * 赠品列表查询
     *
     * @param request
     * @return
     */
    @Override
    public List<GoodsGiftDO> queryGoodsGiftList(QueryGoodsGiftRequest request) {
        QueryWrapper<GoodsGiftDO> wrapper = new QueryWrapper<>();
        if (request.getId() != null && request.getId() != 0) {
            wrapper.lambda().eq(GoodsGiftDO::getId, request.getId());
        }
        if (request.getBusinessType() != null && request.getBusinessType() != 0) {
            wrapper.lambda().eq(GoodsGiftDO::getBusinessType, request.getBusinessType());
        }
        if (StringUtils.isNotEmpty(request.getName())) {
            wrapper.lambda().like(GoodsGiftDO::getName, request.getName());
        }
        if (request.getSponsorType() != null && request.getSponsorType() != 0) {
            wrapper.lambda().like(GoodsGiftDO::getSponsorType, request.getSponsorType());
        }
        if (CollectionUtil.isNotEmpty(request.getGoodsType())) {
            wrapper.lambda().in(GoodsGiftDO::getGoodsType, request.getGoodsType());
        }
        return list(wrapper);
    }

    /**
     * 修改赠品库信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean update(SaveGoodsGiftRequest request) {
        GoodsGiftDO goodsGiftOne = getById(request.getId());

        GoodsGiftDO goodsGiftDO = PojoUtils.map(request, GoodsGiftDO.class);
        goodsGiftDO.setAvailableQuantity(goodsGiftDO.getSafeQuantity() - goodsGiftOne.getUseQuantity());
        goodsGiftDO.setPictureUrl(request.getPictureKey());
        if (goodsGiftOne.getGoodsType() == 2) {
            List<CardRequest> cardList = request.getCardList();
            if (CollectionUtil.isNotEmpty(cardList)) {
                String cardNo = cardList.stream().map(CardRequest::getCardNo).collect(Collectors.joining(","));
                String password = cardList.stream().map(CardRequest::getPassword).collect(Collectors.joining(","));
                goodsGiftDO.setCardNo(cardNo);
                goodsGiftDO.setPassword(password);
            }
        }

        QueryWrapper<GoodsGiftDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GoodsGiftDO::getId, goodsGiftOne.getId())
                .eq(GoodsGiftDO::getUseQuantity, goodsGiftOne.getUseQuantity());
        boolean result = update(goodsGiftDO, wrapper);
        this.baseMapper.updateIntroduct(goodsGiftDO.getId(), request.getIntroduction());
        if (result) {
            if (goodsGiftOne.getGoodsType() == 4) {
                GoodsGiftMemberDO one = new GoodsGiftMemberDO();
                one.setUpdateUser(request.getOpUserId());
                one.setUpdateTime(request.getOpTime());
                QueryWrapper<GoodsGiftMemberDO> goodsWrapper = new QueryWrapper<>();
                goodsWrapper.lambda().eq(GoodsGiftMemberDO::getGoodsGiftId, goodsGiftOne.getId());
                goodsGiftMemberService.batchDeleteWithFill(one, goodsWrapper);

                GoodsGiftMemberDO goodsGiftMember = new GoodsGiftMemberDO();
                goodsGiftMember.setGoodsGiftId(goodsGiftDO.getId());
                goodsGiftMember.setB2bMemberId(request.getMemberId());
                goodsGiftMember.setCreateUser(request.getOpUserId());
                goodsGiftMember.setCreateTime(request.getOpTime());
                goodsGiftMember.setUpdateUser(request.getOpUserId());
                goodsGiftMember.setUpdateTime(request.getOpTime());
                goodsGiftMemberService.save(goodsGiftMember);
            } else if (goodsGiftOne.getGoodsType() == 3) {
                GoodsGiftCouponActivityDO one = new GoodsGiftCouponActivityDO();
                one.setUpdateUser(request.getOpUserId());
                one.setUpdateTime(request.getOpTime());
                QueryWrapper<GoodsGiftCouponActivityDO> goodsWrapper = new QueryWrapper<>();
                goodsWrapper.lambda().eq(GoodsGiftCouponActivityDO::getGoodsGiftId, goodsGiftOne.getId());
                goodsGiftCouponActivityService.batchDeleteWithFill(one, goodsWrapper);

                GoodsGiftCouponActivityDO goodsGiftCouponActivityDO = new GoodsGiftCouponActivityDO();
                goodsGiftCouponActivityDO.setGoodsGiftId(goodsGiftDO.getId());
                goodsGiftCouponActivityDO.setCouponActivityId(request.getCouponActivityId());
                goodsGiftCouponActivityDO.setCreateUser(request.getOpUserId());
                goodsGiftCouponActivityDO.setCreateTime(request.getOpTime());
                goodsGiftCouponActivityDO.setUpdateUser(request.getOpUserId());
                goodsGiftCouponActivityDO.setUpdateTime(request.getOpTime());
                goodsGiftCouponActivityService.save(goodsGiftCouponActivityDO);
            }
        }
        return result;
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

        return this.baseMapper.deduct(quantity, id);
    }

    /**
     * 返还赠品库存数量
     *
     * @param quantity 变换的数量
     * @param id       赠品id
     * @return
     */
    @Override
    public boolean increase(Integer quantity, Long id) {
        return this.baseMapper.increase(quantity, id);
    }

    /**
     * 活动选择赠品
     *
     * @return
     */
    @Override
    public List<GoodsGiftDO> activityChoiceGoodsGiftList() {
        QueryWrapper<GoodsGiftDO> wrapper = new QueryWrapper();
        wrapper.lambda().gt(GoodsGiftDO::getAvailableQuantity, 0);
        return list(wrapper);
    }

    /**
     * 详情信息
     *
     * @param id
     * @return
     */
    @Override
    public GoodsGiftDetailDTO getGoodsGifDetail(Long id) {
        GoodsGiftDO goodsGiftDO = this.getById(id);
        GoodsGiftDetailDTO result = PojoUtils.map(goodsGiftDO, GoodsGiftDetailDTO.class);
        String introduction=this.baseMapper.getIntroductById(id);
        result.setIntroduction(introduction);
        if (result.getGoodsType() == 4) {
            QueryWrapper<GoodsGiftMemberDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(GoodsGiftMemberDO :: getGoodsGiftId,goodsGiftDO.getId())
                    .last("limit 1");

            GoodsGiftMemberDO one = goodsGiftMemberService.getOne(wrapper);
            if(one != null){
                result.setMemberId(one.getB2bMemberId());
            }


        } else if (result.getGoodsType() == 3) {

            QueryWrapper<GoodsGiftCouponActivityDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(GoodsGiftCouponActivityDO :: getGoodsGiftId,goodsGiftDO.getId())
                    .last("limit 1");

            GoodsGiftCouponActivityDO one = goodsGiftCouponActivityService.getOne(wrapper);
            if(one != null){
                result.setCouponActivityId(one.getCouponActivityId());
            }

        }
        return result;
    }

    @Override
    public Map<Long, Long> getAvailQuentByIds(List<Long> ids) {
        List<GoodsGiftDO> goodsGiftList = this.listByIds(ids);
        if (CollectionUtils.isEmpty(goodsGiftList)) {
            return MapUtil.newHashMap();
        }

        Map<Long, Long> collect = goodsGiftList.stream().collect(Collectors.toMap(GoodsGiftDO::getId, GoodsGiftDO::getAvailableQuantity));
        Map<Long, Long> result = new HashMap<>(ids.size());
        ids.forEach(item->{
            if(collect.get(item)!=null){
                result.put(item,collect.get(item));
            }else {
                result.put(item,0L);
            }
        });

        return result;
    }
}
