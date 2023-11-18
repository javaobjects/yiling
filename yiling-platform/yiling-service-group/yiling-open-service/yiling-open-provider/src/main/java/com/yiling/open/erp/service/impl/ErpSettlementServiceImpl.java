package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.yiling.open.erp.dto.ErpClientDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.payment.api.PaymentDaysOrderApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpSettlementMapper;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpSettlementDO;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpSettlementService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderPaymentMethodApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.dto.request.UpdateOrderPaymentStatusRequest;
import com.yiling.order.order.enums.OrderTradeTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.settlement.api.SettlementApi;
import com.yiling.order.settlement.dto.request.SaveSettlementRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-05
 */
@Slf4j
@Service(value = "erpSettlementService")
public class ErpSettlementServiceImpl extends ErpEntityServiceImpl implements ErpSettlementService {

    @Resource
    private ErpSettlementMapper erpSettlementMapper;

    @Resource
    private ErpClientService erpClientService;

    @DubboReference
    private SettlementApi settlementApi;

    @DubboReference
    private PaymentDaysOrderApi paymentDaysOrderApi;

    @DubboReference
    private OrderApi orderApi;

    @DubboReference
    private OrderPaymentMethodApi orderPaymentMethodApi;

    @DubboReference
    private OrderDetailApi orderDetailApi;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpSettlementDO erpSettlementDO = (ErpSettlementDO) baseErpEntity;
        // 1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpSettlementDO.getSuId(), erpSettlementDO.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpSettlementMapper.updateSyncStatusAndMsg(erpSettlementDO.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        // erpGoodsBatch.getOperType() == 3 删除逻辑
        if (OperTypeEnum.DELETE.getCode().equals(erpSettlementDO.getOperType())) {
            erpSettlementMapper.updateSyncStatusAndMsg(erpSettlementDO.getId(), 3, "暂不处理删除结算单");
            return false;
//            erpSettlementDO = erpSettlementMapper.selectById(erpSettlementDO.getId());
//            erpSettlementDO.setOperType(3);
//            if (erpSettlementDO == null) {
//                // 如果是删除商品库存信息，但是查询op库数据为空
//                erpSettlementMapper.updateSyncStatusAndMsg(erpSettlementDO.getId(), 3, "查询库存信息为空");
//                return false;
//            }
        }
        return syncSettlement(erpSettlementDO, erpClient);
    }

    @Override
    public void syncSettlement() {
        List<ErpSettlementDO> erpSettlementList = erpSettlementMapper.syncSettlement();
        for (ErpSettlementDO erpSettlementDO : erpSettlementList) {
            int i = erpSettlementMapper.updateSyncStatusByStatusAndId(erpSettlementDO.getId(), SyncStatus.SYNCING.getCode(),
                    SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                onlineData(erpSettlementDO);
            }
        }
    }

    public boolean syncSettlement(ErpSettlementDO erpSettlementDO, ErpClientDTO erpClient) {
        Long id = erpSettlementDO.getId();
        try {
            //验证所有单据下面的明细相加为0
            //先判断是否存在原始数据
            QueryWrapper<ErpSettlementDO> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<ErpSettlementDO> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(ErpSettlementDO::getChargeNo, erpSettlementDO.getChargeNo());
            lambdaQueryWrapper.eq(ErpSettlementDO::getSuId, erpClient.getSuId());
            lambdaQueryWrapper.eq(ErpSettlementDO::getOperType, 1);

            List<ErpSettlementDO> list = erpSettlementMapper.selectList(queryWrapper);
            BigDecimal sellAmount = BigDecimal.ZERO;
            BigDecimal returnedAmount = BigDecimal.ZERO;
            Integer backAmountType=0;
            for (ErpSettlementDO erpSettlement : list) {
                if (erpSettlement.getSyncStatus() == 3) {
                    erpSettlementMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "单据里面有异常单子");
                    return false;
                }
                if (erpSettlement.getSyncStatus() == 2) {
                    return false;
                }

                if (erpSettlement.getChargeOffType().equals("销售发票")) {
                    sellAmount.add(new BigDecimal(erpSettlement.getChargeOffAmount()));
                }
                if (erpSettlement.getChargeOffType().equals("退销售回款") || erpSettlement.getChargeOffType().equals("销售回款")) {
                    returnedAmount.add(new BigDecimal(erpSettlement.getChargeOffAmount()));
                }
                if(erpSettlement.getChargeOffType().equals("销售回款")){
                    backAmountType=this.getBackAmountType(erpSettlement.getReturnType());
                }
            }

            //校验是否等于零
            if (returnedAmount.compareTo(sellAmount) != 0) {
                // 如果更新失败 ，则改变erp商品库中商品同步状态为失败
                erpSettlementMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "结算数据校验不通过，计算结果不为0");
                return false;
            }
            //先取出所有的负的情况，有冲红的情况
            BigDecimal discountsAmount = BigDecimal.ZERO;
            for (ErpSettlementDO erpSettlement : list) {
                if (new BigDecimal(erpSettlement.getChargeOffAmount()).compareTo(BigDecimal.ZERO) < 0) {
                    discountsAmount = discountsAmount.add(new BigDecimal(erpSettlement.getChargeOffAmount()));
                }
            }

            //处理核销金额计算
            List<ErpSettlementDO> erpSettlementDOList = list.stream().filter(e -> e.getChargeOffType().equals("销售发票") && new BigDecimal(e.getChargeOffAmount()).compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            List<SaveSettlementRequest.SaveSettlementDetailRequest> saveSettlementDetailRequestList = new ArrayList<>();
            for (ErpSettlementDO settlement : erpSettlementDOList) {
                SaveSettlementRequest.SaveSettlementDetailRequest saveSettlementRequest = new SaveSettlementRequest.SaveSettlementDetailRequest();
                discountsAmount = new BigDecimal(settlement.getChargeOffAmount()).add(discountsAmount);
                //判断是否小于0，如果小于0证明回款核销金额为0
                if (discountsAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    saveSettlementRequest.setBackAmount(BigDecimal.ZERO);
                    saveSettlementRequest.setDiscountsAmount(new BigDecimal(settlement.getChargeOffAmount()));
                } else if (discountsAmount.compareTo(BigDecimal.ZERO) > 0) {
                    saveSettlementRequest.setBackAmount(discountsAmount);
                    saveSettlementRequest.setDiscountsAmount(new BigDecimal(settlement.getChargeOffAmount()).subtract(discountsAmount));
                    discountsAmount = BigDecimal.ZERO;
                }
                saveSettlementRequest.setSettlementAmount(new BigDecimal(settlement.getChargeOffAmount()));
                saveSettlementRequest.setErpReceivableNo(settlement.getChargeOffNo());
                saveSettlementRequest.setGoodsInSn(settlement.getGoodsInSn());
                saveSettlementDetailRequestList.add(saveSettlementRequest);
            }

            //按照应收单号组装map信息
            Map<String, List<SaveSettlementRequest.SaveSettlementDetailRequest>> map = new HashMap<>();
            for (SaveSettlementRequest.SaveSettlementDetailRequest saveSettlementDetailRequest : saveSettlementDetailRequestList) {
                if (map.containsKey(saveSettlementDetailRequest.getErpReceivableNo())) {
                    map.get(saveSettlementDetailRequest.getErpReceivableNo()).add(saveSettlementDetailRequest);
                } else {
                    List<SaveSettlementRequest.SaveSettlementDetailRequest> saveSettlementDetailRequests = new ArrayList<>();
                    saveSettlementDetailRequests.add(saveSettlementDetailRequest);
                    map.put(saveSettlementDetailRequest.getErpReceivableNo(), saveSettlementDetailRequests);
                }
            }

            //判断订单的金额和应收的金额是否匹配
            for (Map.Entry<String, List<SaveSettlementRequest.SaveSettlementDetailRequest>> entry : map.entrySet()) {
                SaveSettlementRequest saveSettlementRequest = new SaveSettlementRequest();
                saveSettlementRequest.setErpReceivableNo(entry.getKey());
                List<SaveSettlementRequest.SaveSettlementDetailRequest> settlementDetailRequestList = entry.getValue();
                BigDecimal backAmountd = BigDecimal.ZERO;
                BigDecimal discountsAmountd = BigDecimal.ZERO;
                BigDecimal settlementAmountd = BigDecimal.ZERO;
                OrderDTO orderDTO = orderApi.getOrderByErpReceivableNo(entry.getKey());
                if (orderDTO == null) {
                    continue;
                }

                List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());

                Map<String, OrderDetailDTO> goodsMap = orderDetailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getGoodsErpCode, Function.identity()));
                saveSettlementRequest.setOrderId(orderDTO.getId());
                for (SaveSettlementRequest.SaveSettlementDetailRequest settlementDetail : settlementDetailRequestList) {
                    backAmountd = backAmountd.add(settlementDetail.getBackAmount());
                    discountsAmountd = discountsAmountd.add(settlementDetail.getDiscountsAmount());
                    settlementAmountd=settlementAmountd.add(settlementDetail.getSettlementAmount());
                    settlementDetail.setGoodsId(goodsMap.get(settlementDetail.getGoodsInSn()).getGoodsId());
                    settlementDetail.setOrderDetailId(goodsMap.get(settlementDetail.getGoodsInSn()).getId());
                    settlementDetail.setOrderId(orderDTO.getId());
                    settlementDetail.setBuyerEid(orderDTO.getBuyerEid());
                    settlementDetail.setSellerEid(orderDTO.getSellerEid());
                }
                saveSettlementRequest.setBuyerEid(orderDTO.getBuyerEid());
                saveSettlementRequest.setSellerEid(orderDTO.getSellerEid());
                saveSettlementRequest.setDeliveryTime(orderDTO.getDeliveryTime());
                saveSettlementRequest.setSettlementAmount(settlementAmountd);
                saveSettlementRequest.setBackAmount(backAmountd);
                saveSettlementRequest.setDiscountsAmount(discountsAmountd);
                saveSettlementRequest.setSaveSettlementDetailRequestList(settlementDetailRequestList);
                saveSettlementRequest.setBackAmountType(backAmountType);
                settlementApi.saveSettlement(saveSettlementRequest);

                //判断订单是否完全结算完成
                BigDecimal settlementAmount = settlementApi.getsettlementAmountByOrderId(orderDTO.getId());
                BigDecimal orderSettlementAmount = this.getOrderSettlementAmount(orderDTO);
                log.info("订单orderId={},订单结算金额{}，eas结算金额{}", orderDTO.getId(), orderSettlementAmount, settlementAmount);
                if (orderSettlementAmount.compareTo(settlementAmount) == 0) {
                    if (orderDTO.getPaymentMethod().equals(PaymentMethodEnum.PAYMENT_DAYS.getCode().intValue())) {
                        paymentDaysOrderApi.updatePaymentOrderAmount(orderDTO.getId());
                    }
                    UpdateOrderPaymentStatusRequest request=new UpdateOrderPaymentStatusRequest();
                    request.setOrderId(orderDTO.getId());
                    request.setPaymentTime(new Date());
                    request.setPaymentStatus(PaymentStatusEnum.PAID.getCode());
                    orderApi.updatePaymentStatus(request);

                    // 注意:目前除了,预售订单，支付金额均为全额支付
                    CreateOrderPayMentMethodRequest createOrderPayMentMethodRequest = PojoUtils.map(orderDTO,CreateOrderPayMentMethodRequest.class);
                    createOrderPayMentMethodRequest.setTradeType(OrderTradeTypeEnum.FULL.getCode());
                    createOrderPayMentMethodRequest.setPaymentTime(request.getPaymentTime());
                    createOrderPayMentMethodRequest.setPaymentAmount(orderDTO.getPaymentAmount());
                    createOrderPayMentMethodRequest.setOrderId(orderDTO.getId());

                    // 保存支付方式
                    orderPaymentMethodApi.save(createOrderPayMentMethodRequest);
                }
            }
            for (ErpSettlementDO erpSettlement : list) {
                erpSettlementMapper.updateSyncStatusAndMsg(erpSettlement.getId(), SyncStatus.SUCCESS.getCode(),
                        "同步成功");
            }
            return true;
        } catch (Exception e) {
            erpSettlementMapper.updateSyncStatusAndMsg(id, SyncStatus.FAIL.getCode(), "系统异常");
            log.error("EAS结算单同步出现错误", e);
        }
        return false;
    }

    public BigDecimal getOrderSettlementAmount(OrderDTO orderDTO) {
        return orderDTO.getInvoiceAmount();
    }

    public Integer getBackAmountType(String returnType){
        Map<String,String> map=new HashMap<>();
        map.put("01","电汇");
        map.put("010","现金");
        map.put("020","应收票据(银行承兑汇票)");
        map.put("022","应付票据(银行承兑汇票)");
        map.put("05","银行汇票");
        map.put("06","现金支票");
        map.put("07","转账支票");
        map.put("08","普通支票");
        if(Arrays.asList("01","010","06","07","08").contains(returnType)){
            return 1;
        }
        if(Arrays.asList("020","022","05").contains(returnType)){
            return 3;
        }
        return 0;
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpSettlementMapper;
    }
}
