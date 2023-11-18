package com.yiling.f2b.admin.goods.controller;

import java.math.BigDecimal;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.f2b.admin.goods.form.SaveOrUpdateLimitPriceForm;
import com.yiling.f2b.admin.goods.vo.GoodsLimitPriceVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.pricing.goods.api.PopGoodsLimitPriceApi;
import com.yiling.pricing.goods.dto.PopGoodsLimitPriceDTO;
import com.yiling.pricing.goods.dto.request.SaveOrUpdatePopLimitPriceRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsLimitPriceController
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@RestController
@RequestMapping("price/limit")
@Api(tags = "商品限价模块接口")
@Slf4j
public class GoodsLimitPriceController extends BaseController {
    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsLimitPriceApi popGoodsLimitPriceApi;

    @ApiOperation(value = "获取商品限价信息")
    @GetMapping("/getLimitPrice")
    public Result<GoodsLimitPriceVO> getLimitPrice(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("goodsId") Long goodsId){
        GoodsDTO goodsDTO = goodsApi.queryInfo(goodsId);
        if(null == goodsDTO){
            return Result.failed("商品不存在");
        }
        if(!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDTO.getAuditStatus())){
            return Result.failed("该商品状态不可用");
        }
        PopGoodsLimitPriceDTO limitPriceDTO = popGoodsLimitPriceApi.getLimitPriceBySpecificationsId(goodsDTO.getSellSpecificationsId(), null);

        return Result.success(PojoUtils.map(limitPriceDTO,GoodsLimitPriceVO.class));
    }

    @ApiOperation(value = "保存或修改限价信息")
    @PostMapping("/saveOrUpdateLimitPrice")
    public Result<Boolean> saveOrUpdateLimitPrice(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody SaveOrUpdateLimitPriceForm form){
        GoodsDTO goodsDTO = goodsApi.queryInfo(form.getGoodsId());
        if(null == goodsDTO){
            return Result.failed("商品不存在");
        }
        if(!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDTO.getAuditStatus())){
            return Result.failed("该商品状态不可用");
        }
        if(null == form.getLowerLimitPrice()||null == form.getUpperLimitPrice()){
            return Result.failed("价格下限和价格上限不能为空");
        }
        if(form.getLowerLimitPrice().compareTo(BigDecimal.ZERO) <=0 || form.getUpperLimitPrice().compareTo(BigDecimal.ZERO) <=0  ){
            return Result.failed("价格下限和价格上限必须大于0");
        }
        BigDecimal lowerLimitPrice=form.getLowerLimitPrice().setScale(4, BigDecimal.ROUND_DOWN);
        BigDecimal upperLimitPrice=form.getUpperLimitPrice().setScale(4, BigDecimal.ROUND_DOWN);
        if(upperLimitPrice.compareTo(lowerLimitPrice)<0){
            return Result.failed("价格上限必须大于等于价格下限");
        }
        SaveOrUpdatePopLimitPriceRequest request = new SaveOrUpdatePopLimitPriceRequest();
        request.setId(form.getId());
        request.setStandardId(goodsDTO.getStandardId());
        request.setSellSpecificationsId(goodsDTO.getSellSpecificationsId());
        request.setLowerLimitPrice(lowerLimitPrice);
        request.setUpperLimitPrice(upperLimitPrice);
        request.setOpUserId(staffInfo.getCurrentUserId());
        popGoodsLimitPriceApi.saveOrUpdate(request);
        return Result.success(true);
    }
}
