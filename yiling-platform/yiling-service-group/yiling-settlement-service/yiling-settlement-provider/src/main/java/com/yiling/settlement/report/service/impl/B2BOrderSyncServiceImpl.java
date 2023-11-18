package com.yiling.settlement.report.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.bo.CouponActivityRulesBO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.ReturnDetailBathDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.settlement.report.bo.B2bOrderSyncBO;
import com.yiling.settlement.report.dao.B2bOrderSyncMapper;
import com.yiling.settlement.report.dto.ReportB2bOrderSyncDTO;
import com.yiling.settlement.report.dto.request.QueryOrderSyncPageListRequest;
import com.yiling.settlement.report.dto.request.UpdateB2bOrderIdenRequest;
import com.yiling.settlement.report.entity.B2bOrderSyncDO;
import com.yiling.settlement.report.entity.LogDO;
import com.yiling.settlement.report.enums.ReportErrorCode;
import com.yiling.settlement.report.enums.ReportLogTypeEnum;
import com.yiling.settlement.report.enums.ReportOrderIdenEnum;
import com.yiling.settlement.report.enums.ReportOrderaAnormalReasonEnum;
import com.yiling.settlement.report.enums.ReportSettStatusEnum;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.settlement.report.service.B2bOrderSyncService;
import com.yiling.settlement.report.service.LogService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 返利报表的B2B订单同步表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-08-08
 */
@Slf4j
@Service
public class B2BOrderSyncServiceImpl extends BaseServiceImpl<B2bOrderSyncMapper, B2bOrderSyncDO> implements B2bOrderSyncService {

    @Autowired
    LogService logService;

    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    CouponActivityApi couponActivityApi;

    @Override
    public List<B2bOrderSyncBO> queryOrderSyncByOrderCode(List<String> orderCodeList) {
        if (CollUtil.isEmpty(orderCodeList)) {
            return ListUtil.toList();
        }
        LambdaQueryWrapper<B2bOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(B2bOrderSyncDO::getOrderNo, orderCodeList);
        List<B2bOrderSyncDO> list = list(wrapper);

        return PojoUtils.map(list, B2bOrderSyncBO.class);
    }

    @Override
    public List<B2bOrderSyncBO> queryOrderSync(String orderNo) {
        if (StrUtil.isBlank(orderNo)) {
            return null;
        }
        LambdaQueryWrapper<B2bOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(B2bOrderSyncDO::getOrderNo, orderNo);
        List<B2bOrderSyncDO> list = list(wrapper);

        return PojoUtils.map(list, B2bOrderSyncBO.class);
    }

    @Override
    public List<ReportB2bOrderSyncDTO> queryOrderSync(Long orderId) {
        LambdaQueryWrapper<B2bOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(B2bOrderSyncDO::getOrderId, orderId);
        return PojoUtils.map(list(wrapper), ReportB2bOrderSyncDTO.class);
    }

    @Override
    public Boolean createOrderSync(String orderCode) {
        List<B2bOrderSyncBO> orderSyncBO = queryOrderSync(orderCode);
        //如果订单数据已存在
        if (CollUtil.isNotEmpty(orderSyncBO)) {
            return Boolean.TRUE;
        }
        List<OrderDTO> orderDTOS = orderApi.listByOrderNos(ListUtil.toList(orderCode));
        if (CollUtil.isEmpty(orderDTOS)) {
            log.error("订单收货后同步至返利订单同步失败，原因：根据订单号没有查询到订单，订单号：{}", orderCode);
            return Boolean.FALSE;
        }
        OrderDTO orderDTO = orderDTOS.stream().findAny().get();
        List<B2bOrderSyncDO> receiveInfo = queryOrderReceiveInfo(orderDTO);
        if (CollUtil.isEmpty(receiveInfo)) {
            log.error("此订单没有收货信息，单号={}", orderDTO.getOrderNo());
            throw new BusinessException(ReportErrorCode.REPORT_B2B_ORDER_SYNC_INFO_INCOMPLETE);
        }

        return saveBatch(receiveInfo);
    }

    @Override
    public Page<ReportB2bOrderSyncDTO> queryB2bOrderSyncInfoPageList(QueryOrderSyncPageListRequest request) {
        request.setEndReceiveTime(DateUtil.endOfDay(request.getEndReceiveTime()));
        LambdaQueryWrapper<B2bOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.in(CollUtil.isNotEmpty(request.getIdList()), B2bOrderSyncDO::getId, request.getIdList());
        wrapper.in(B2bOrderSyncDO::getSellerEid, request.getSellerEidList());
        wrapper.ge(B2bOrderSyncDO::getReceiveTime, request.getStartReceiveTime());
        wrapper.le(B2bOrderSyncDO::getReceiveTime, request.getEndReceiveTime());
        wrapper.eq(ObjectUtil.isNotNull(request.getReportSettStatus()) && ObjectUtil.notEqual(request.getReportSettStatus(), 0), B2bOrderSyncDO::getReportSettStatus, request.getReportSettStatus());
        wrapper.in(CollUtil.isNotEmpty(request.getReportStatusList()), B2bOrderSyncDO::getReportStatus, request.getReportStatusList());
        wrapper.eq(ObjectUtil.isNotNull(request.getIdentificationStatus()) && ObjectUtil.notEqual(request.getIdentificationStatus(), 0), B2bOrderSyncDO::getIdentificationStatus, request.getIdentificationStatus());
        if (ObjectUtil.equal(request.getFilterInvalidOrder(), 1)) {
            wrapper.ne(B2bOrderSyncDO::getIdentificationStatus, ReportOrderIdenEnum.INVALID.getCode());
        }
        Page<B2bOrderSyncDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportB2bOrderSyncDTO.class);
    }

    @Override
    public Page<String> queryB2bOrderNoPageList(QueryOrderSyncPageListRequest request) {
        if (ObjectUtil.isNotNull(request.getEndReceiveTime())) {
            request.setEndReceiveTime(DateUtil.endOfDay(request.getEndReceiveTime()));
        }
        return baseMapper.queryB2bOrderNoPageList(request.getPage(), request);
    }

    @Override
    public Page<Long> queryB2bSyncKeyPageList(QueryOrderSyncPageListRequest request) {
        return baseMapper.queryB2bSyncKeyPageList(request.getPage(), request);
    }

    @Override
    public void rejectOrderSync(Long reportId, ReportStatusEnum statusEnum) {
        LambdaQueryWrapper<B2bOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(B2bOrderSyncDO::getReportId, reportId);
        List<B2bOrderSyncDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        B2bOrderSyncDO orderSyncDO = new B2bOrderSyncDO();
        orderSyncDO.setReportSettStatus(ReportSettStatusEnum.UN_CALCULATE.getCode());
        orderSyncDO.setReportStatus(statusEnum.getCode());
        orderSyncDO.setReportId(0L);
        orderSyncDO.setOpUserId(0L);
        orderSyncDO.setOpTime(new Date());
        boolean isSuccess = update(orderSyncDO, wrapper);
        if (!isSuccess) {
            log.error("驳回b2b报表时，更新b2b订单同步表失败，参数={}，报表id={}", orderSyncDO, reportId);
            throw new ServiceException(ReportErrorCode.REPORT_REJECT_FAIL.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateB2bOrderIdentification(UpdateB2bOrderIdenRequest request) {
        Boolean result;
        LambdaQueryWrapper<B2bOrderSyncDO> wrapper = Wrappers.lambdaQuery();
        List<Long> updateIdList = ListUtil.toList();
        //更新的数据
        B2bOrderSyncDO var = new B2bOrderSyncDO();
        var.setIdentificationStatus(request.getUpdateIdenStatus());
        var.setAbnormalReason(request.getAbnormalReason());
        var.setAbnormalDescribed(request.getAbnormalDescribed());
        var.setOpUserId(request.getOpUserId());
        var.setOpTime(new Date());

        //按id更新
        if (CollUtil.isNotEmpty(request.getIdList())) {
            updateIdList.addAll(request.getIdList());
            wrapper.in(B2bOrderSyncDO::getId, request.getIdList());
            result = update(var, wrapper);
        } else {
            //按查询条件更新
            QueryOrderSyncPageListRequest queryRequest = PojoUtils.map(request, QueryOrderSyncPageListRequest.class);
            if (ObjectUtil.isNotNull(request.getEndReceiveTime())) {
                queryRequest.setEndReceiveTime(DateUtil.endOfDay(request.getEndReceiveTime()));
            }
            //分页查询订单列表
            int current = 1;
            Page<Long> orderPage;
            do {
                queryRequest.setCurrent(current);
                queryRequest.setSize(100);
                //分页查询符合结算条件的订单
                orderPage = queryB2bSyncKeyPageList(queryRequest);
                if (CollUtil.isEmpty(orderPage.getRecords())) {
                    break;
                }
                List<Long> orderSyncKeyList = orderPage.getRecords();
                updateIdList.addAll(orderSyncKeyList);
                //更新订单状态
                wrapper.in(B2bOrderSyncDO::getId, orderSyncKeyList);
                boolean isSuccess = update(var, wrapper);
                if (!isSuccess) {
                    log.error("更新B2B订单标识时影响的条数为0，参数={},id={}", var, orderSyncKeyList);
                    throw new ServiceException(ResultCode.FAILED);
                }
                current = current + 1;
            } while (orderPage != null && CollUtil.isNotEmpty(orderPage.getRecords()));
            result = Boolean.TRUE;
        }
        if (CollUtil.isNotEmpty(updateIdList)) {
            //插入日志
            LogDO logDO = new LogDO();
            logDO.setDataId(JSON.toJSONString(updateIdList));
            logDO.setType(ReportLogTypeEnum.UPDATE_B2B_ORDER_IDENT.getCode());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(ReportOrderIdenEnum.getByCode(request.getUpdateIdenStatus()).getName());
            if (ObjectUtil.isNotNull(request.getAbnormalReason()) && ObjectUtil.notEqual(request.getAbnormalReason(), 0)) {
                stringBuilder.append(StrUtil.DASHED);
                stringBuilder.append(ReportOrderaAnormalReasonEnum.getByCode(request.getAbnormalReason()).getName());
            }
            if (StrUtil.isNotBlank(request.getAbnormalDescribed())) {
                stringBuilder.append(StrUtil.DASHED);
                stringBuilder.append(request.getAbnormalDescribed());
            }
            logDO.setOpValue(stringBuilder.toString());
            if (ObjectUtil.isNull(request.getOpUserId()) || ObjectUtil.equal(request.getOpUserId(), 0L)) {
                logDO.setOpRemark("生成报表时系统判定库存不足");
            }
            logDO.setOpUserId(request.getOpUserId());
            logService.createLog(logDO);
        }
        return result;
    }

    @Override
    public Boolean initOrderData() {
        //分页查询同步失败的订单列表
        int current = 1;
        Page<OrderDTO> page;
        QueryBackOrderInfoRequest request = new QueryBackOrderInfoRequest();
        //分页查询同步失败的订单列表
        do {
            List<B2bOrderSyncDO> data = ListUtil.toList();
            request.setCurrent(current);
            request.setSize(100);
            request.setOrderStatus(OrderStatusEnum.RECEIVED.getCode());
            request.setOrderType(OrderTypeEnum.B2B.getCode());
            //分页查询订单列表
            page = orderApi.getBackOrderPage(request);
            if (CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            //查询是否已经同步
            List<OrderDTO> records = page.getRecords();
            List<B2bOrderSyncBO> existenceOrder = queryOrderSyncByOrderCode(records.stream().map(OrderDTO::getOrderNo).collect(Collectors.toList()));

            List<String> existenceOrderNoList = existenceOrder.stream().map(B2bOrderSyncBO::getOrderNo).distinct().collect(Collectors.toList());

            records.forEach(e -> {
                //已经同步的订单进行忽略
                if (existenceOrderNoList.contains(e.getOrderNo())) {
                    return;
                }
                //计算收货信息
                List<B2bOrderSyncDO> receiveInfo = queryOrderReceiveInfo(e);
                data.addAll(receiveInfo);
            });

            current = current + 1;
            if (CollUtil.isNotEmpty(data)) {
                boolean saveBatch = saveBatch(data);
                if (!saveBatch) {
                    log.error("初始返利报表B2B订单数据 据失败，参数={}", data);
                }
            }
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return Boolean.TRUE;
    }

    /**
     * 根据订单号查询订单的收货信息
     *
     * @param orderDTO
     * @return
     */
    private List<B2bOrderSyncDO> queryOrderReceiveInfo(OrderDTO orderDTO) {

        //查询订单明细
        List<OrderDetailDTO> details = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
        //商品明细
        List<Long> detailIdList = details.stream().map(OrderDetailDTO::getId).distinct().collect(Collectors.toList());
        Map<Long, OrderDetailDTO> detailMap = details.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o));
        //查询orderChange
        List<OrderDetailChangeDTO> changeList = orderDetailChangeApi.listByOrderId(orderDTO.getId());
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = changeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, e -> e));
        //查询配送单
        List<OrderDeliveryDTO> delivers = orderDeliveryApi.getOrderDeliveryList(orderDTO.getId());

        //查询退货信息 审核通过的
        List<ReturnDetailBathDTO> returnDetailBathDTOS = orderReturnDetailBatchApi.queryByDetailId(detailIdList).stream().filter(e -> ObjectUtil.notEqual(OrderReturnTypeEnum.SELLER_RETURN_ORDER.getCode(), e.getReturnType())).collect(Collectors.toList());

        //参加的活动
        List<Long> activityIdList = details.stream().map(OrderDetailDTO::getPromotionActivityId).filter(e -> ObjectUtil.isNotNull(e) && ObjectUtil.notEqual(e, 0L)).distinct().collect(Collectors.toList());
        Map<Long, PromotionActivityDTO> activityMap = MapUtil.newHashMap();
        //如果订单参加了活动则查询活动
        if (CollUtil.isNotEmpty(activityIdList)) {
            activityMap = promotionActivityApi.batchQueryByIdList(activityIdList).stream().collect(Collectors.toMap(PromotionActivityDTO::getId, e -> e));
        }
        //查询优惠券使用
        Map<Integer, OrderCouponUseDTO> couponUseDTOMap = MapUtil.newHashMap();
        //如果订单使用了优惠券则查询优惠券使用
        List<OrderDetailChangeDTO> couponChange = changeList.stream().filter(e -> BigDecimal.ZERO.compareTo(e.getPlatformCouponDiscountAmount()) == -1 || BigDecimal.ZERO.compareTo(e.getCouponDiscountAmount()) == -1).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(couponChange)) {
            couponUseDTOMap = orderCouponUseApi.listByOrderIds(ListUtil.toList(orderDTO.getId())).stream().collect(Collectors.toMap(OrderCouponUseDTO::getCouponType, e -> e));
        }

        Map<Long, PromotionActivityDTO> finalActivityMap = activityMap;
        Map<Long, OrderDetailChangeDTO> finalOrderDetailChangeMap = orderDetailChangeMap;
        Map<Integer, OrderCouponUseDTO> finalCouponUseDTOMap = couponUseDTOMap;

        List<B2bOrderSyncDO> result = ListUtil.toList();
        delivers.forEach(delivery -> {
            B2bOrderSyncDO b2bOrderSync = PojoUtils.map(delivery, B2bOrderSyncDO.class);
            b2bOrderSync.setSellerEid(orderDTO.getSellerEid());
            b2bOrderSync.setBuyerEid(orderDTO.getBuyerEid());
            b2bOrderSync.setOrderNo(orderDTO.getOrderNo());
            b2bOrderSync.setOrderSource(orderDTO.getOrderSource());
            b2bOrderSync.setOrderStatus(orderDTO.getOrderStatus());
            b2bOrderSync.setPaymentMethod(orderDTO.getPaymentMethod());
            b2bOrderSync.setPaymentStatus(orderDTO.getPaymentStatus());
            b2bOrderSync.setOrderCreateTime(orderDTO.getCreateTime());

            initB2bOrder(b2bOrderSync);
            //退货数量
            b2bOrderSync.setGoodsName(detailMap.get(delivery.getDetailId()).getGoodsName());
            b2bOrderSync.setSpecifications(detailMap.get(delivery.getDetailId()).getGoodsSpecification());
            b2bOrderSync.setLicense(detailMap.get(delivery.getDetailId()).getGoodsLicenseNo());
            b2bOrderSync.setGoodsManufacturer(detailMap.get(delivery.getDetailId()).getGoodsManufacturer());
            b2bOrderSync.setOrderGoodsType(detailMap.get(delivery.getDetailId()).getGoodsType());

            //退货数量
            Integer returnQuantity = this.getReturnQuantity(returnDetailBathDTOS, delivery.getDetailId(), delivery.getBatchNo());
            b2bOrderSync.setRefundQuantity(returnQuantity);
            b2bOrderSync.setReceiveQuantity(delivery.getDeliveryQuantity() - returnQuantity);

            OrderDetailDTO detailDTO = detailMap.get(delivery.getDetailId());
            b2bOrderSync.setOriginalPrice(detailDTO.getOriginalPrice());
            b2bOrderSync.setGoodsPrice(detailDTO.getGoodsPrice());
            b2bOrderSync.setGoodsAmount(detailDTO.getGoodsAmount());

            //计算活动相关
            calculateActivityInfo(b2bOrderSync, detailDTO, finalActivityMap, b2bOrderSync.getReceiveQuantity(), delivery.getGoodsQuantity());
            //计算优惠券相关
            calculateCouponInfo(b2bOrderSync, finalOrderDetailChangeMap, finalCouponUseDTOMap, b2bOrderSync.getReceiveQuantity(), delivery.getGoodsQuantity());
            b2bOrderSync.setReceiveTime(orderDTO.getReceiveTime());
            result.add(b2bOrderSync);
        });

        return result;
    }


    /**
     * 初始数据
     *
     * @param detailB2b
     * @return
     */
    private void initB2bOrder(B2bOrderSyncDO detailB2b) {
        detailB2b.setPlatformAmount(BigDecimal.ZERO);
        detailB2b.setPlatformPercentage(BigDecimal.ZERO);
        detailB2b.setShopAmount(BigDecimal.ZERO);
        detailB2b.setShopPercentage(BigDecimal.ZERO);
        detailB2b.setOpUserId(null);
    }

    /**
     * 获取退货数量
     *
     * @param returnDetailBathDTOList
     * @param goodsDetailId
     * @param batchNo
     * @return
     */
    private Integer getReturnQuantity(List<ReturnDetailBathDTO> returnDetailBathDTOList, Long goodsDetailId, String batchNo) {
        if (CollUtil.isEmpty(returnDetailBathDTOList)) {
            return 0;
        }
        List<ReturnDetailBathDTO> returnDetailBathDTOS = returnDetailBathDTOList.stream().filter(r -> {
            if (r.getDetailId().equals(goodsDetailId) && batchNo.equals(r.getBatchNo())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(returnDetailBathDTOS)) {
            return 0;
        }
        return returnDetailBathDTOS.stream().mapToInt(ReturnDetailBathDTO::getReturnQuantity).sum();
    }

    /**
     * 设置活动相关
     *
     * @param b2bOrderSync
     * @param detailDTO
     * @param activityMap
     * @param receiveQuantity
     * @param goodsQuantity
     */
    private void calculateActivityInfo(B2bOrderSyncDO b2bOrderSync, OrderDetailDTO detailDTO, Map<Long, PromotionActivityDTO> activityMap, Integer receiveQuantity, Integer goodsQuantity) {

        //秒杀特价商品小计=原价*数量
        //秒杀特价优惠金额=原价*数量-goodsAmount
        if (ObjectUtil.equal(detailDTO.getPromotionActivityType(), PromotionActivityTypeEnum.SPECIAL.getCode()) || ObjectUtil.equal(detailDTO.getPromotionActivityType(), PromotionActivityTypeEnum.LIMIT.getCode()) || ObjectUtil.equal(detailDTO.getPromotionActivityType(), PromotionActivityTypeEnum.COMBINATION.getCode())) {
            BigDecimal sellAmount = b2bOrderSync.getOriginalPrice().subtract(detailDTO.getGoodsPrice()).multiply(new BigDecimal(b2bOrderSync.getReceiveQuantity()));
            //如果秒杀特价金额为负数
            if (BigDecimal.ZERO.compareTo(sellAmount) == 1) {
                sellAmount = BigDecimal.ZERO;
            }

            b2bOrderSync.setGoodsAmount(NumberUtil.round(b2bOrderSync.getGoodsPrice().multiply(new BigDecimal(b2bOrderSync.getGoodsQuantity())), 2, RoundingMode.HALF_UP));

            //            detailB2b.setPaymentAmount(NumberUtil.round(detailDTO.getGoodsAmount(), 2, RoundingMode.HALF_UP));
            b2bOrderSync.setPaymentAmount(NumberUtil.round(detailDTO.getGoodsPrice().multiply(new BigDecimal(detailDTO.getReceiveQuantity().toString())), 2, RoundingMode.HALF_UP));

            b2bOrderSync.setDiscountAmount(NumberUtil.round(sellAmount, 2, RoundingMode.HALF_UP));
            BigDecimal percentage = BigDecimal.ONE.subtract(detailDTO.getGoodsPrice().divide(detailDTO.getOriginalPrice(), 5, RoundingMode.HALF_UP));
            //如果秒杀特价金额为负数百分之置为0
            if (BigDecimal.ZERO.compareTo(percentage) == 1) {
                percentage = BigDecimal.ZERO;
            }
            b2bOrderSync.setDiscountPercentage(percentage);

            //设置活动的平台承担百分比
            PromotionActivityDTO activityDTO = activityMap.get(detailDTO.getPromotionActivityId());
            b2bOrderSync.setActivityType(PromotionActivityTypeEnum.getByCode(detailDTO.getPromotionActivityType()).getName());
            b2bOrderSync.setActivityDescribe(activityDTO.getName());
            BigDecimal platformPercentage = activityDTO.getPlatformPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
            //平台承担折扣金额=折扣金额*平台承担比例
            b2bOrderSync.setPlatformAmount(NumberUtil.round(b2bOrderSync.getDiscountAmount().multiply(platformPercentage), 2, RoundingMode.HALF_UP));
            //平台承担折扣比例=折扣比率*平台承担比例
            b2bOrderSync.setPlatformPercentage(NumberUtil.round(b2bOrderSync.getDiscountPercentage().multiply(platformPercentage), 5, RoundingMode.HALF_UP));
            BigDecimal shopPercentage = activityDTO.getMerchantPercent().divide(BigDecimal.TEN).divide(BigDecimal.TEN);
            //商家承担折扣金额=折扣金额*商家承担比例
            b2bOrderSync.setShopAmount(NumberUtil.round(b2bOrderSync.getDiscountAmount().multiply(shopPercentage), 2, RoundingMode.HALF_UP));
            //商家承担折扣比例=折扣比率*商家承担比例
            b2bOrderSync.setShopPercentage(NumberUtil.round(b2bOrderSync.getDiscountPercentage().multiply(shopPercentage), 5, RoundingMode.HALF_UP));
        } else {
            //折扣金额
            //优惠券总金额=商家+平台
            BigDecimal couponAmount = detailDTO.getPlatformCouponDiscountAmount().add(detailDTO.getCouponDiscountAmount());
            //折扣金额
            //优惠券总折扣金额=优惠券总金额*（签收数量/购买数量）
            BigDecimal sellAmount = couponAmount.multiply(new BigDecimal(receiveQuantity.toString()).divide(new BigDecimal(goodsQuantity), 5, RoundingMode.HALF_UP));
            //实付金额=商品单价*签收数量-促销金额
            BigDecimal price = detailDTO.getGoodsPrice();
            //支付金额=商品单价*批次签收数量-总折扣金额
            b2bOrderSync.setPaymentAmount(NumberUtil.round(price.multiply(new BigDecimal(receiveQuantity.toString())).subtract(sellAmount), 2, RoundingMode.HALF_UP));

            b2bOrderSync.setDiscountAmount(NumberUtil.round(sellAmount, 2, RoundingMode.HALF_UP));
            if (BigDecimal.ZERO.compareTo(couponAmount) == 0) {
                b2bOrderSync.setDiscountPercentage(BigDecimal.ZERO);
            } else {
                //总折扣比率=优惠券总金额/（商品单价*购买数量）
                b2bOrderSync.setDiscountPercentage(NumberUtil.round(couponAmount.divide(price.multiply(new BigDecimal(goodsQuantity.toString())), 5, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP));
            }
        }
    }

    /**
     * 计算优惠券相关
     *
     * @param b2bOrderSync
     * @param orderDetailChangeMap
     * @param couponUseDTOMap
     * @param receiveQuantity
     * @param goodsQuantity
     */
    private void calculateCouponInfo(B2bOrderSyncDO b2bOrderSync, Map<Long, OrderDetailChangeDTO> orderDetailChangeMap, Map<Integer, OrderCouponUseDTO> couponUseDTOMap, Integer receiveQuantity, Integer goodsQuantity) {
        OrderDetailChangeDTO changeDTO = orderDetailChangeMap.get(b2bOrderSync.getDetailId());
        if (ObjectUtil.isNull(changeDTO)) {
            return;
        }
        //如果用了优惠券
        if (BigDecimal.ZERO.compareTo(changeDTO.getReceivePlatformCouponDiscountAmount()) == -1 || BigDecimal.ZERO.compareTo(changeDTO.getReceiveCouponDiscountAmount()) == -1) {
            b2bOrderSync.setActivityType("优惠券");
            //平台优惠券金额（商品小计）
            BigDecimal platformAmount = BigDecimal.ZERO;
            //商家优惠券金额（商品小计）
            BigDecimal shopAmount = BigDecimal.ZERO;
            //如果平台券金额不为0
            if (BigDecimal.ZERO.compareTo(changeDTO.getPlatformCouponDiscountAmount()) != 0) {
                OrderCouponUseDTO couponUseDTO = couponUseDTOMap.get(1);
                BigDecimal amount = changeDTO.getPlatformCouponDiscountAmount();
                //平台承担金额
                BigDecimal pa = amount.multiply(couponUseDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                //商家承担金额
                BigDecimal sa = amount.subtract(pa);
                platformAmount = platformAmount.add(pa);
                shopAmount = shopAmount.add(sa);
                CouponActivityRulesBO activityRulesBO = couponActivityApi.getCouponActivityRulesById(couponUseDTO.getCouponActivityId());
                if (ObjectUtil.isNotNull(activityRulesBO)) {
                    b2bOrderSync.setActivityDescribe(activityRulesBO.getCouponRules());
                }
            }
            //如果商家券金额不为0
            if (BigDecimal.ZERO.compareTo(changeDTO.getCouponDiscountAmount()) != 0) {
                OrderCouponUseDTO couponUseDTO = couponUseDTOMap.get(2);
                BigDecimal amount = changeDTO.getCouponDiscountAmount();
                //平台承担金额
                BigDecimal pa = amount.multiply(couponUseDTO.getPlatformRatio().divide(BigDecimal.TEN).divide(BigDecimal.TEN));
                //商家承担金额
                BigDecimal sa = amount.subtract(pa);
                platformAmount = platformAmount.add(pa);
                shopAmount = shopAmount.add(sa);
                CouponActivityRulesBO activityRulesBO = couponActivityApi.getCouponActivityRulesById(couponUseDTO.getCouponActivityId());
                if (ObjectUtil.isNotNull(activityRulesBO)) {
                    String content = b2bOrderSync.getActivityDescribe();
                    if (StrUtil.isBlank(content)) {
                        b2bOrderSync.setActivityDescribe(activityRulesBO.getCouponRules());
                    } else {
                        b2bOrderSync.setActivityDescribe(content + StrUtil.CR + activityRulesBO.getCouponRules());
                    }
                }
            }

            //平台承担比例=平台承担金额/收货小计
            if (BigDecimal.ZERO.compareTo(platformAmount) == 0) {
                b2bOrderSync.setPlatformPercentage(BigDecimal.ZERO);
            } else {
                b2bOrderSync.setPlatformPercentage(platformAmount.divide(b2bOrderSync.getGoodsAmount(), 5, RoundingMode.HALF_UP));
            }
            //商家承担比例=商家承担金额/收货小计
            if (BigDecimal.ZERO.compareTo(shopAmount) == 0) {
                b2bOrderSync.setShopPercentage(BigDecimal.ZERO);
            } else {
                b2bOrderSync.setShopPercentage(shopAmount.divide(b2bOrderSync.getGoodsAmount(), 5, RoundingMode.HALF_UP));
            }

            //平台优惠券金额（配送单分摊）=平台优惠券金额（商品小计）*（签收数量/购买数量）
            platformAmount = platformAmount.multiply(new BigDecimal(receiveQuantity.toString()).divide(new BigDecimal(goodsQuantity.toString()), 5, RoundingMode.HALF_UP));
            //商家优惠券金额（配送单分摊）=商家优惠券金额（商品小计）*（签收数量/购买数量）
            shopAmount = shopAmount.multiply(new BigDecimal(receiveQuantity.toString()).divide(new BigDecimal(goodsQuantity.toString()), 5, RoundingMode.HALF_UP));

            b2bOrderSync.setPlatformAmount(NumberUtil.round(platformAmount, 2, RoundingMode.HALF_UP));
            b2bOrderSync.setShopAmount(NumberUtil.round(shopAmount, 2, RoundingMode.HALF_UP));
        }
    }
}
