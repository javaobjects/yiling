package com.yiling.admin.b2b.lottery.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.b2b.lottery.form.QueryGoodsGiftForm;
import com.yiling.admin.b2b.lottery.form.SaveGoodsGiftForm;
import com.yiling.admin.b2b.lottery.form.UpdateGoodsGiftForm;
import com.yiling.admin.b2b.lottery.vo.CardVO;
import com.yiling.admin.b2b.lottery.vo.GoodsGiftVO;
import com.yiling.admin.b2b.lottery.vo.QueryGoodsGiftListVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.goodsgift.dto.request.QueryGoodsGiftRequest;
import com.yiling.marketing.goodsgift.dto.request.SaveGoodsGiftRequest;
import com.yiling.marketing.goodsgift.enums.GoodsGiftErrorCode;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 赠品信息表 前端控制器
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@RestController
@RequestMapping("/goods/gift")
@Api(tags = "赠品库")
public class GoodsGiftController extends BaseController {

    @DubboReference
    GoodsGiftApi         goodsGiftApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "新增赠品")
    @PostMapping("/save")
    @Log(title = "新增赠品",businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> save(@CurrentUser CurrentAdminInfo adminInfo,
                                @RequestBody @Valid SaveGoodsGiftForm form) {
        if(form.getSafeQuantity().compareTo(form.getQuantity()) >0){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_QUANTITY_ERROR);
        }
        validGoodsGiftPrice(form.getPrice());
        SaveGoodsGiftRequest request = PojoUtils.map(form, SaveGoodsGiftRequest.class);
        request.setSponsorType(1);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = goodsGiftApi.save(request);
        return Result.success(result);
    }

    private void validGoodsGiftPrice(BigDecimal price) {
        // 为空或者为负数或者小数点后面长度大于2报错
        if (ObjectUtil.isEmpty(price)) {
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_PRICE_EMPTY);
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_PRICE_NEGATIVE);
        }
        if (price.scale() > 2) {
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_PRICE_SCALE_TOO_LONG);
        }
    }

    @ApiOperation(value = "赠品库列表")
    @PostMapping("/get/list")
    public Result<CollectionObject<List<QueryGoodsGiftListVO>>> queryGoodsGiftList(@CurrentUser CurrentAdminInfo adminInfo,
                                                                                   @RequestBody @Valid QueryGoodsGiftForm form) {

        QueryGoodsGiftRequest request = PojoUtils.map(form, QueryGoodsGiftRequest.class);
        request.setSponsorType(1);
        List<GoodsGiftDTO> goodsGiftList = goodsGiftApi.queryGoodsGiftList(request);

        List<QueryGoodsGiftListVO> result = PojoUtils.map(goodsGiftList, QueryGoodsGiftListVO.class);
        Map<Long, List<PromotionGoodsGiftLimitDTO>> map = new HashMap<>();
        List<Long> ids = result.stream().map(QueryGoodsGiftListVO::getId).collect(Collectors.toList());

        //TODO wei.wang 抽奖加入后需要加入判断该商品是否出于活动中
        if(CollectionUtil.isNotEmpty(ids)){
            List<PromotionGoodsGiftLimitDTO> promotionGoodsGiftLimitDTOS = promotionActivityApi.queryByGiftIdList(ids);
            for(PromotionGoodsGiftLimitDTO one : promotionGoodsGiftLimitDTOS){
                if(map.containsKey(one.getGoodsGiftId())){
                    List<PromotionGoodsGiftLimitDTO> promotionList = map.get(one.getGoodsGiftId());
                    promotionList.add(one);
                }else{
                    map.put(one.getGoodsGiftId(),new ArrayList<PromotionGoodsGiftLimitDTO>(){{add(one);}});
                }
            }
        }

        for (QueryGoodsGiftListVO one : result) {
            List<PromotionGoodsGiftLimitDTO> promotionList = map.get(one.getId());
            if (CollectionUtil.isNotEmpty(promotionList)) {
                one.setJoinActivityFlag(true);
            } else {
                one.setJoinActivityFlag(false);
            }
        }
        return Result.success(new CollectionObject(result));
    }

    @ApiOperation(value = "赠品库明细")
    @GetMapping("/get")
    public Result<GoodsGiftVO> queryGoodsGift(@CurrentUser CurrentAdminInfo adminInfo,
                                              @RequestParam("id") Long id) {

        GoodsGiftDetailDTO goodsGifDetail = goodsGiftApi.getGoodsGifDetail(id);

        GoodsGiftVO result = PojoUtils.map(goodsGifDetail, GoodsGiftVO.class);
        String cardNo = goodsGifDetail.getCardNo();
        String password = goodsGifDetail.getPassword();
        if(StringUtils.isNotEmpty(cardNo)){
            List<CardVO> cardList = new ArrayList<>();
            String[] cardNoList =cardNo.split(",",-1);
            String[] passwordList = password.split(",",-1);
            for(int i=0;i<passwordList.length;i++){
                CardVO cardVO = new CardVO();
                cardVO.setCardNo(cardNoList[i]);
                cardVO.setPassword(passwordList[i]);
                cardList.add(cardVO);
            }
            result.setCardList(cardList);
        }

        if (StringUtils.isNotEmpty(goodsGifDetail.getPictureUrl())) {
            FileInfoVO fileInfo = new FileInfoVO();
            fileInfo.setFileUrl(fileService.getUrl(goodsGifDetail.getPictureUrl(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
            fileInfo.setFileKey(goodsGifDetail.getPictureUrl());
            result.setFileInfo(fileInfo);
        }

        return Result.success(result);
    }

    @ApiOperation(value = "修改赠品")
    @PostMapping("/update")
    @Log(title = "修改赠品",businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> update(@CurrentUser CurrentAdminInfo adminInfo,
                                @RequestBody @Valid UpdateGoodsGiftForm form) {
        GoodsGiftDTO giftDTO = goodsGiftApi.getOneById(form.getId());
        validGoodsGiftPrice(form.getPrice());
        if(giftDTO == null){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_NOT_EXIST);
        }
        if(giftDTO.getSafeQuantity().compareTo(form.getSafeQuantity()) >0){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_SAFE_QUANTITY_ERROR);
        }
        if(giftDTO.getQuantity().compareTo(form.getQuantity()) > 0){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_QUANTITY_ERROR);
        }
        if(form.getSafeQuantity().compareTo(form.getQuantity()) >0){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_QUANTITY_ERROR);
        }
        //TODO wei.wang抽奖加入后需要加入判断该商品是否出于活动中
        List<PromotionGoodsGiftLimitDTO> promotionGoodsGiftList = promotionActivityApi.queryByGiftIdList(new ArrayList<Long>() {{
            add(form.getId());
        }});
        if(CollectionUtil.isNotEmpty(promotionGoodsGiftList)){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_JOIN_ACTIVITY_NOT_UPDATE);
        }
        SaveGoodsGiftRequest request = PojoUtils.map(form, SaveGoodsGiftRequest.class);
        request.setSponsorType(1);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = goodsGiftApi.update(request);
        return Result.success(result);
    }

    @ApiOperation(value = "赠品库删除")
    @GetMapping("/delete")
    @Log(title = "删除赠品",businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> delete (@CurrentUser CurrentAdminInfo adminInfo,
                                              @RequestParam("id") Long id) {

        //TODO wei.wang抽奖加入后需要加入判断该商品是否出于活动中
        List<PromotionGoodsGiftLimitDTO> promotionGoodsGiftList = promotionActivityApi.queryByGiftIdList(new ArrayList<Long>() {{
            add(id);
        }});
        if(CollectionUtil.isNotEmpty(promotionGoodsGiftList)){
            throw new BusinessException(GoodsGiftErrorCode.GOODS_GIFT_JOIN_ACTIVITY_NOT_UPDATE);
        }
        Boolean result = goodsGiftApi.delete(id, adminInfo.getCurrentUserId());
        return Result.success(result);
    }


}
