package com.yiling.b2b.admin.index.controller;

import java.util.ArrayList;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.admin.index.vo.B2BOrderQuantityVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.dto.B2BOrderQuantityDTO;
import com.yiling.order.order.enums.OrderPlatformTypeEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家后台首页
 *
 * @author:wei.wang
 * @date:2021/11/16
 */
@Slf4j
@Api(tags = "首页")
@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {

    @DubboReference
    OrderB2BApi        orderB2BApi;
    @DubboReference
    GoodsLimitPriceApi goodsLimitPriceApi;

    @ApiOperation(value = "订单数量查询")
    @GetMapping("/order/number")
    public Result<B2BOrderQuantityVO> pageList(@CurrentUser CurrentStaffInfo staffInfo) {
        B2BOrderQuantityDTO result = orderB2BApi.countB2BOrderQuantity(staffInfo.getCurrentEid(), OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_MALL_B2B);
        B2BOrderQuantityVO resultVo = PojoUtils.map(result, B2BOrderQuantityVO.class);
        Map<Long, Integer> map = goodsLimitPriceApi.getRecommendationFlagByCustomerEids(new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }}, Constants.YILING_EID);
        resultVo.setMemberPushShow(map !=null ? map.get(staffInfo.getCurrentEid()):0);

        B2BOrderQuantityDTO orderQuantity = orderB2BApi.countB2BOrderQuantity(staffInfo.getCurrentEid(), OrderPlatformTypeEnum.ORDER_PLATFORM_TYPE_APP_WEB);
        resultVo.setUnPurchaseDeliveryQuantity(orderQuantity.getUnDeliveryQuantity());
        resultVo.setUnPurchasePaymentQuantity(orderQuantity.getUnPaymentQuantity());
        resultVo.setUnPurchaseReceiveQuantity(orderQuantity.getUnReceiveQuantity());
        return Result.success(resultVo);
    }
}
