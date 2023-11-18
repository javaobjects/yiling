package com.yiling.admin.data.center.report.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.report.form.QueryB2BOrderCountForm;
import com.yiling.admin.data.center.report.form.QueryB2BOrderGoodsLagsPageForm;
import com.yiling.admin.data.center.report.vo.B2BOrderCountVO;
import com.yiling.admin.data.center.report.vo.B2BOrderGoodsLagsVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsTagApi;
import com.yiling.goods.standard.dto.StandardGoodsTagDTO;
import com.yiling.goods.standard.dto.request.QueryStandardGoodsTagsRequest;
import com.yiling.order.order.api.OrderExtendApi;
import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.enums.OrderTypeEnum;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/report/order")
@Api(tags = "统计报表")
@Slf4j
public class B2BOrderCountController extends BaseController {

    @DubboReference
    StandardGoodsTagApi standardGoodsTagApi;
    @DubboReference
    OrderExtendApi orderExtendApi;


    @ApiOperation(value = "B2B订单报表")
    @PostMapping("/B2B/count")
    public Result<B2BOrderCountVO> getB2BOrderCount(@Valid @RequestBody QueryB2BOrderCountForm form) {
        QueryB2BOrderCountRequest request = PojoUtils.map(form, QueryB2BOrderCountRequest.class);
        request.setOrderType(OrderTypeEnum.B2B.getCode());
        B2BOrderCountVO result = new B2BOrderCountVO();
        if(form.getStandardGoodsTagId() != null && form.getStandardGoodsTagId() !=0 ){
            List<Long> standardIdList = standardGoodsTagApi.getStandardIdListByTagId(form.getStandardGoodsTagId());
            request.setStandardIdList(standardIdList);
        }
        if(request.getStartTime() != null ){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(request.getEndTime() != null ){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        OrderB2BCountAmountDTO b2BCountAmount = orderExtendApi.getB2BCountAmount(request);
        request.setType(0);
        Integer orderQuantity = orderExtendApi.getB2BCountQuantity(request);
        request.setType(1);
        Integer buyerQuantity = orderExtendApi.getB2BCountQuantity(request);
        request.setType(2);
        Integer sellerQuantity = orderExtendApi.getB2BCountQuantity(request);

        result.setBuyerQuantity(buyerQuantity);
        result.setDiscountAmount(b2BCountAmount.getDiscountAmount());
        result.setOriginalAmount(b2BCountAmount.getOriginalAmount());
        result.setSellerQuantity(sellerQuantity);
        result.setOrderQuantity(orderQuantity);

        if(request.getEndTime() == null && request.getEndTime() == null){

            result.setBuyerQuantityMonthRatio(BigDecimal.ZERO);
            result.setBuyerQuantityYearRatio(BigDecimal.ZERO);

            result.setDiscountAmountMonthRatio(BigDecimal.ZERO);
            result.setDiscountAmountYearRatio(BigDecimal.ZERO);

            result.setOrderQuantityMonthRatio(BigDecimal.ZERO);
            result.setOrderQuantityYearRatio(BigDecimal.ZERO);

            result.setOriginalAmountMonthRatio(BigDecimal.ZERO);
            result.setOriginalAmountYearRatio(BigDecimal.ZERO);

            result.setSellerQuantityMonthRatio(BigDecimal.ZERO);
            result.setSellerQuantityYearRatio(BigDecimal.ZERO);
            return Result.success(result) ;
        }

        if(form.getStartTime() != null){
            request.setStartTime(DateUtil.beginOfDay(form.getStartTime()).offset(DateField.MONTH, -1));
        }
        if(form.getEndTime() != null){
            request.setEndTime(DateUtil.endOfDay(form.getEndTime()).offset(DateField.MONTH, -1));
        }
        //环比数据
        request.setType(0);
        OrderB2BCountAmountDTO monthRatioAmount = orderExtendApi.getB2BCountAmount(request);
        Integer monthRatioOrderQuantity = orderExtendApi.getB2BCountQuantity(request);
        request.setType(1);
        Integer monthRatioBuyerQuantity = orderExtendApi.getB2BCountQuantity(request);
        request.setType(2);
        Integer monthRatioSellerQuantity = orderExtendApi.getB2BCountQuantity(request);
        if(monthRatioAmount.getOriginalAmount().compareTo(BigDecimal.ZERO) == 0){
            result.setOriginalAmountMonthRatio(BigDecimal.ZERO);
        }else{
            result.setOriginalAmountMonthRatio(b2BCountAmount.getOriginalAmount().subtract(monthRatioAmount.getOriginalAmount()).multiply(BigDecimal.valueOf(100)).divide(monthRatioAmount.getOriginalAmount(),2,BigDecimal.ROUND_HALF_UP));
        }
        if(monthRatioAmount.getDiscountAmount().compareTo(BigDecimal.ZERO) == 0){
            result.setDiscountAmountMonthRatio(BigDecimal.ZERO);
        }else{
            result.setDiscountAmountMonthRatio(b2BCountAmount.getDiscountAmount().subtract(monthRatioAmount.getDiscountAmount()).multiply(BigDecimal.valueOf(100)).divide(monthRatioAmount.getDiscountAmount(),2,BigDecimal.ROUND_HALF_UP));

        }
        if(monthRatioSellerQuantity == 0){
            result.setSellerQuantityMonthRatio(BigDecimal.ZERO);
        }else{
            result.setSellerQuantityMonthRatio(BigDecimal.valueOf(sellerQuantity - monthRatioSellerQuantity).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(monthRatioSellerQuantity),2,BigDecimal.ROUND_HALF_UP)) ;

        }
        if(monthRatioOrderQuantity == 0){
            result.setOrderQuantityMonthRatio(BigDecimal.ZERO);
        }else{
            result.setOrderQuantityMonthRatio(BigDecimal.valueOf(orderQuantity - monthRatioOrderQuantity).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(monthRatioOrderQuantity),2,BigDecimal.ROUND_HALF_UP)) ;
        }
        if(monthRatioBuyerQuantity == 0){
            result.setBuyerQuantityMonthRatio(BigDecimal.ZERO);
        }else{
            result.setBuyerQuantityMonthRatio(BigDecimal.valueOf(buyerQuantity - monthRatioBuyerQuantity).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(monthRatioBuyerQuantity),2,BigDecimal.ROUND_HALF_UP)) ;
        }

        //同比数据
        if(form.getStartTime() != null){

            request.setStartTime(DateUtil.beginOfDay(form.getStartTime()).offset(DateField.YEAR, -1));

        }
        if(form.getEndTime() != null){

            request.setEndTime(DateUtil.endOfDay(form.getEndTime()).offset(DateField.YEAR, -1));

        }
        request.setType(0);
        OrderB2BCountAmountDTO yearRatioAmount = orderExtendApi.getB2BCountAmount(request);
        Integer yearRatioOrderQuantity = orderExtendApi.getB2BCountQuantity(request);
        request.setType(1);
        Integer yearRatioBuyerQuantity = orderExtendApi.getB2BCountQuantity(request);
        request.setType(2);
        Integer yearRatioSellerQuantity = orderExtendApi.getB2BCountQuantity(request);

        if(yearRatioAmount.getOriginalAmount().compareTo(BigDecimal.ZERO) == 0){
            result.setOriginalAmountYearRatio(BigDecimal.ZERO);
        }else{
            result.setOriginalAmountYearRatio(b2BCountAmount.getOriginalAmount().subtract(yearRatioAmount.getOriginalAmount()).multiply(BigDecimal.valueOf(100)).divide(yearRatioAmount.getOriginalAmount(),2,BigDecimal.ROUND_HALF_UP));
        }

        if(yearRatioAmount.getDiscountAmount().compareTo(BigDecimal.ZERO) == 0){
            result.setDiscountAmountYearRatio(BigDecimal.ZERO);
        }else{
            result.setDiscountAmountYearRatio(b2BCountAmount.getDiscountAmount().subtract(yearRatioAmount.getDiscountAmount()).multiply(BigDecimal.valueOf(100)).divide(yearRatioAmount.getDiscountAmount(),2,BigDecimal.ROUND_HALF_UP));

        }
        if(yearRatioSellerQuantity == 0){
            result.setSellerQuantityYearRatio(BigDecimal.ZERO);
        }else{
            result.setSellerQuantityYearRatio(BigDecimal.valueOf(sellerQuantity - yearRatioSellerQuantity).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(yearRatioSellerQuantity),2,BigDecimal.ROUND_HALF_UP)) ;

        }
        if(yearRatioOrderQuantity == 0){
            result.setOrderQuantityYearRatio(BigDecimal.ZERO);
        }else{
            result.setOrderQuantityYearRatio(BigDecimal.valueOf(orderQuantity - yearRatioOrderQuantity).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(yearRatioOrderQuantity),2,BigDecimal.ROUND_HALF_UP)) ;

        }
        if(yearRatioBuyerQuantity == 0){
            result.setBuyerQuantityYearRatio(BigDecimal.ZERO);
        }else{
            result.setBuyerQuantityYearRatio(BigDecimal.valueOf(buyerQuantity - yearRatioBuyerQuantity).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(yearRatioBuyerQuantity),2,BigDecimal.ROUND_HALF_UP)) ;

        }

        return Result.success(result);
    }

    @ApiOperation(value = "获取商品标签")
    @PostMapping("/get/goods/lag")
    public Result<Page<B2BOrderGoodsLagsVO>> getB2BOrderCount(@Valid @RequestBody QueryB2BOrderGoodsLagsPageForm form) {
        QueryStandardGoodsTagsRequest request = PojoUtils.map(form, QueryStandardGoodsTagsRequest.class);
        Page<StandardGoodsTagDTO> standardGoodsTagList = standardGoodsTagApi.queryTagsListPage(request);
        return Result.success(PojoUtils.map(standardGoodsTagList,B2BOrderGoodsLagsVO.class));
    }

}
