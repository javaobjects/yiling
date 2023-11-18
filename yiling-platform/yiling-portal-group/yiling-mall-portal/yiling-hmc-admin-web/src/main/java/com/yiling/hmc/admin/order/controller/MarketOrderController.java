package com.yiling.hmc.admin.order.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.admin.order.form.AddMerchantRemarkForm;
import com.yiling.hmc.admin.order.form.QueryMarketOrderPageForm;
import com.yiling.hmc.admin.order.form.SaveOrderDeliveryForm;
import com.yiling.hmc.admin.order.vo.HmcPrescriptionGoodsInfoVO;
import com.yiling.hmc.admin.order.vo.MarketOrderDetailVO;
import com.yiling.hmc.admin.order.vo.MarketOrderInfoDetailVO;
import com.yiling.hmc.admin.order.vo.QueryMarketOrderInfoVO;
import com.yiling.hmc.admin.order.vo.QueryMarketOrderPageVO;
import com.yiling.hmc.order.api.MarketOrderAddressApi;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.api.MarketOrderDetailApi;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderAddressDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.dto.request.MarketOrderDeliveryRequest;
import com.yiling.hmc.order.dto.request.MarketOrderSaveRequest;
import com.yiling.hmc.order.dto.request.QueryAdminMarkerOrderPageRequest;
import com.yiling.hmc.order.enums.HmcMarketOrderTypeEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.ih.patient.api.HmcPrescriptionApi;
import com.yiling.ih.patient.dto.HmcPrescriptionGoodsInfoDTO;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcDoctorInfoDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.bo.CurrentUserInfo;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: benben.jia
 * @data: 2023/03/02
 */
@RestController
@RequestMapping("/marketOrder/")
@Api(tags = "市场订单")
@Slf4j
public class MarketOrderController extends BaseController {

    @DubboReference
    MarketOrderApi marketOrderApi;

    @Autowired
    FileService fileService;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    MarketOrderDetailApi marketOrderDetailApi;

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    DoctorApi doctorApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    MarketOrderAddressApi marketOrderAddressApi;

    @DubboReference
    HmcPrescriptionApi prescriptionApi;

    @ApiOperation(value = "列表")
    @PostMapping("/queryOrderPage")
    public Result<Page<QueryMarketOrderPageVO>> queryOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryMarketOrderPageForm form) {
        QueryAdminMarkerOrderPageRequest request = PojoUtils.map(form, QueryAdminMarkerOrderPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        List goodsIdList = Collections.emptyList();
        String goodsName = form.getGoodsName();
        if (StringUtils.isNotBlank(goodsName)) {
            List<MarketOrderDetailDTO> marketOrderDetailDTOS = marketOrderDetailApi.queryByGoodsNameList(goodsName);
            if (CollectionUtils.isEmpty(marketOrderDetailDTOS)) {
                return Result.success(request.getPage());
            }
            goodsIdList = marketOrderDetailDTOS.stream().map(MarketOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        }
        List userList = Collections.emptyList();
        String nickName = form.getNickName();
        if (StringUtils.isNotBlank(nickName)) {
            QueryHmcUserPageListRequest queryHmcUserPageListRequest = new QueryHmcUserPageListRequest();
            queryHmcUserPageListRequest.setCurrent(1).setSize(1000);
            queryHmcUserPageListRequest.setNickName(nickName);
            Page<HmcUser> hmcUserPage = hmcUserApi.pageList(queryHmcUserPageListRequest);
            if (hmcUserPage.getTotal() == 0) {
                return Result.success(request.getPage());
            }
            userList = hmcUserPage.getRecords().stream().map(HmcUser::getUserId).distinct().collect(Collectors.toList());
        }
        request.setGoodsIdList(goodsIdList);
        request.setUserList(userList);
        if(Objects.nonNull(form.getBeginTime())) {
            request.setBeginTime(DateUtil.beginOfDay(form.getBeginTime()));
        }
        if(Objects.nonNull(form.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(form.getEndTime()));
        }
        Page<AdminMarketOrderDTO> page = marketOrderApi.queryAdminMarketOrderPage(request);
        if (page.getTotal() == 0) {
            return Result.success(request.getPage());
        }
        Page<QueryMarketOrderPageVO> voPage = PojoUtils.map(page, QueryMarketOrderPageVO.class);
        //查询药品信息
        List<Long> orderIds = voPage.getRecords().stream().map(QueryMarketOrderPageVO::getId).distinct().collect(Collectors.toList());
        List<MarketOrderDetailDTO> detailDTOS = marketOrderDetailApi.queryByOrderIdList(orderIds);
        List<Long> goodsIds = detailDTOS.stream().map(MarketOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> dtoMap = goodsDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        List<MarketOrderDetailVO> orderDetailVOS = PojoUtils.map(detailDTOS, MarketOrderDetailVO.class);
        orderDetailVOS.forEach(e -> {
            if (dtoMap.containsKey(e.getGoodsId())) {
                e.setPic(fileService.getUrl(dtoMap.get(e.getGoodsId()).getPic(), FileTypeEnum.GOODS_PICTURE));
            }
        });
        Map<Long, List<MarketOrderDetailVO>> couponMap = orderDetailVOS.stream().collect(Collectors.groupingBy(MarketOrderDetailVO::getOrderId));
        //查询用户信息
        List<Long> userIdList = voPage.getRecords().stream().map(QueryMarketOrderPageVO::getCreateUser).distinct().collect(Collectors.toList());
        List<HmcUser> hmcUsers = hmcUserApi.listByIds(userIdList);
        Map<Long, HmcUser> userMap = hmcUsers.stream().collect(Collectors.toMap(HmcUser::getUserId, Function.identity()));

        //查询医生信息
        List<Integer> doctorIdList = voPage.getRecords().stream().map(QueryMarketOrderPageVO::getDoctorId).distinct().collect(Collectors.toList());
        List<HmcDoctorInfoDTO> doctorInfoByIds = doctorApi.getDoctorInfoByIds(doctorIdList);
        Map<Integer, HmcDoctorInfoDTO> doctorMap = doctorInfoByIds.stream().collect(Collectors.toMap(HmcDoctorInfoDTO::getId, Function.identity()));
        voPage.getRecords().forEach(e -> {
            if(Objects.nonNull(e.getPrescriptionType())) {
                e.setPrescriptionType(e.getPrescriptionType() + 1);
            } else {
                e.setPrescriptionType(0);
            }
            if (couponMap.containsKey(e.getId())) {
                e.setDetailVOS(couponMap.get(e.getId()));
            }
            if (userMap.containsKey(e.getCreateUser())) {
                e.setNickName(userMap.get(e.getCreateUser()).getNickName());
                e.setUserMobile(userMap.get(e.getCreateUser()).getMobile());
            }
            if (doctorMap.containsKey(e.getDoctorId())) {
                e.setDoctorName(doctorMap.get(e.getDoctorId()).getDoctorName());
                e.setHospitalName(doctorMap.get(e.getDoctorId()).getHospitalName());
            }
            if (e.getPaymentStatus().equals(HmcPaymentStatusEnum.UN_PAY.getCode())) {
                e.setRealTotalAmount("未支付");
            } else {
                e.setRealTotalAmount(e.getOrderTotalAmount().toPlainString());
            }

            // 处方订单 -> 获取商品
            if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(e.getMarketOrderType())) {
                HmcPrescriptionGoodsInfoDTO prescriptionGoodsById = prescriptionApi.getPrescriptionGoodsById(e.getPrescriptionId().intValue());
                HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO = PojoUtils.map(prescriptionGoodsById, HmcPrescriptionGoodsInfoVO.class);
                if (CollUtil.isNotEmpty(prescriptionGoodsInfoVO.getGoodsList())) {
                    String hmcPrescriptionGoodsGoodsDesc = prescriptionGoodsInfoVO.getGoodsList().stream().map(item -> item.getGoodsName() + " " + item.getNum() + "g").collect(Collectors.joining("、"));
                    e.setHmcPrescriptionGoodsGoodsDesc(hmcPrescriptionGoodsGoodsDesc);
                }
                e.setPrescriptionGoodsInfoVO(prescriptionGoodsInfoVO);
            }
        });
        return Result.success(voPage);
    }


    @ApiOperation(value = "获取详情")
    @GetMapping("/queryOrder")
    public Result<QueryMarketOrderInfoVO> queryMarketOrder(@CurrentUser CurrentUserInfo currentUser, @ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id) {
        MarketOrderDTO marketOrderDTO = marketOrderApi.queryById(id);
        if (Objects.isNull(marketOrderDTO)) {
            return Result.failed("未查询到订单信息");
        }
        //收货地址
        QueryMarketOrderInfoVO marketOrderVO = PojoUtils.map(marketOrderDTO, QueryMarketOrderInfoVO.class);
        MarketOrderAddressDTO addressDTO = marketOrderAddressApi.getAddressByOrderId(marketOrderVO.getId());
        PojoUtils.map(addressDTO, marketOrderVO);
        //商品
        List<MarketOrderDetailDTO> marketOrderDetailDTOS = marketOrderDetailApi.queryByOrderIdList(Lists.newArrayList(marketOrderVO.getId()));
        List<Long> goodsIds = marketOrderDetailDTOS.stream().map(MarketOrderDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsDTO> goodsDTOS = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> dtoMap = goodsDTOS.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        List<MarketOrderInfoDetailVO> orderInfoDetailVOS = PojoUtils.map(marketOrderDetailDTOS, MarketOrderInfoDetailVO.class);
        orderInfoDetailVOS.forEach(e -> {
            if (dtoMap.containsKey(e.getGoodsId())) {
                e.setPic(fileService.getUrl(dtoMap.get(e.getGoodsId()).getPic(), FileTypeEnum.GOODS_PICTURE));
            }
        });
        marketOrderVO.setDetailVOS(orderInfoDetailVOS);

        if (marketOrderVO.getPayTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setPayTime(null);
        }
        if (marketOrderVO.getDeliverTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setDeliverTime(null);
        }
        if (marketOrderVO.getCancelTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setCancelTime(null);
        }
        if (marketOrderVO.getReceiveTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
            marketOrderVO.setReceiveTime(null);
        }

        // 处方订单 -> 获取商品
        if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(marketOrderDTO.getMarketOrderType())) {
            HmcPrescriptionGoodsInfoDTO prescriptionGoodsById = prescriptionApi.getPrescriptionGoodsById(marketOrderDTO.getPrescriptionId().intValue());
            HmcPrescriptionGoodsInfoVO prescriptionGoodsInfoVO = PojoUtils.map(prescriptionGoodsById, HmcPrescriptionGoodsInfoVO.class);
            if(Objects.nonNull(prescriptionGoodsInfoVO)) {
                Integer prescriptionType = prescriptionGoodsInfoVO.getPrescriptionType();
                if (Objects.isNull(prescriptionType)) {
                    prescriptionGoodsInfoVO.setPrescriptionType(0);
                } else {
                    prescriptionGoodsInfoVO.setPrescriptionType(prescriptionType + 1);
                }
            }
            marketOrderVO.setPrescriptionGoodsInfoVO(prescriptionGoodsInfoVO);
        }

        return Result.success(marketOrderVO);
    }


    @ApiOperation(value = "添加商家运营备注")
    @PostMapping("/addPlatformRemark")
    public Result<Boolean> addPlatformRemark(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody AddMerchantRemarkForm form) {
        MarketOrderSaveRequest request = new MarketOrderSaveRequest();
        request.setOpUserId(staffInfo.getCurrentUserId());
        PojoUtils.map(form, request);
        marketOrderApi.saveRemark(request);
        return Result.success(true);
    }


    @ApiOperation(value = "订单发货/更改物流信息")
    @PostMapping("/orderDelivery")
    public Result<Boolean> orderDelivery(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody SaveOrderDeliveryForm form) {
        MarketOrderDeliveryRequest request = PojoUtils.map(form, MarketOrderDeliveryRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        marketOrderApi.orderDelivery(request);
        return Result.success(true);
    }


}
