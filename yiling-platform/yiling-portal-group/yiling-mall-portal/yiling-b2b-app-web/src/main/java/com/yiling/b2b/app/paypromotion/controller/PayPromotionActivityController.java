package com.yiling.b2b.app.paypromotion.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yiling.b2b.app.goods.utils.GoodsAssemblyUtils;
import com.yiling.b2b.app.paypromotion.form.QueryPayPromotionGoodsSearchFrom;
import com.yiling.b2b.app.strategy.vo.StrategyGoodsSearchListVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityEidOrGoodsIdDTO;
import com.yiling.search.goods.api.EsGoodsSearchApi;
import com.yiling.search.goods.dto.request.EsActivityGoodsSearchRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付促销
 *
 * @author: yong.zhang
 * @date: 2023/5/5 0005
 */
@Slf4j
@Api(tags = "支付促销")
@RestController
@RequestMapping("/payPromotion/activity")
public class PayPromotionActivityController extends BaseController {

    @DubboReference
    PayPromotionActivityApi payPromotionActivityApi;

    @DubboReference
    CustomerApi customerApi;

    @DubboReference
    EsGoodsSearchApi esGoodsSearchApi;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    @UserAccessAuthentication
    @ApiOperation(value = "支付促销-可使用商品列表", httpMethod = "POST")
    @PostMapping(path = "/payPromotionGoodsSearch")
    public Result<StrategyGoodsSearchListVO> strategyGoodsSearch(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPayPromotionGoodsSearchFrom form) {
        log.info("payPromotionGoodsSearch, form -> {}", JSON.toJSONString(form));
        log.info("payPromotionGoodsSearch, staffInfo -> {}", JSON.toJSONString(staffInfo));

        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        PayPromotionActivityEidOrGoodsIdDTO activityEidOrGoodsIdDTO = payPromotionActivityApi.getGoodsListPageByActivityId(form.getActivityId(), buyerEid, form.getSellerEid());
        log.info("payPromotionGoodsSearch, activityEidOrGoodsIdDTO -> {}", JSON.toJSONString(activityEidOrGoodsIdDTO));

        if (activityEidOrGoodsIdDTO == null) {
            StrategyGoodsSearchListVO strategyGoodsSearchListVO = new StrategyGoodsSearchListVO();
            //            strategyGoodsSearchListVO.setTitle(activityEidOrGoodsIdDTO.getTitle());
            return Result.success(strategyGoodsSearchListVO);
        }

        EsActivityGoodsSearchRequest request = PojoUtils.map(form, EsActivityGoodsSearchRequest.class);
        request.setMallFlag(1);
        request.setMallStatus(GoodsStatusEnum.UP_SHELF.getCode());
        request.setAllEidFlag(activityEidOrGoodsIdDTO.getAllEidFlag());
        request.setEidList(activityEidOrGoodsIdDTO.getEidList());
        request.setGoodsIdList(activityEidOrGoodsIdDTO.getGoodsIdList());
        request.setSellSpecificationsIdList(activityEidOrGoodsIdDTO.getSellSpecificationsIdList());
        request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
        if (buyerEid > 0) {
            QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
            enterpriseRequest.setCustomerEid(buyerEid);
            enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
            enterpriseRequest.setLimit(50);
            List<Long> sortEid = customerApi.getEidListByCustomerEid(enterpriseRequest);
            request.setSortEid(sortEid);
        }
        EsAggregationDTO data = esGoodsSearchApi.searchActivityGoods(request);
        log.info("payPromotionGoodsSearch, data -> {}", JSON.toJSONString(data));
        List<GoodsInfoDTO> goodsAggDTOList = data.getData();
        if (CollUtil.isNotEmpty(goodsAggDTOList)) {
            // 商品ID集合
            List<Long> goodsIds = goodsAggDTOList.stream().map(BaseDTO::getId).distinct().collect(Collectors.toList());
            data.setData(goodsAssemblyUtils.assembly(goodsIds, buyerEid));
        }
        StrategyGoodsSearchListVO strategyGoodsSearchListVO = PojoUtils.map(data, StrategyGoodsSearchListVO.class);
        strategyGoodsSearchListVO.setTitle(activityEidOrGoodsIdDTO.getTitle());
        return Result.success(strategyGoodsSearchListVO);
    }
}
