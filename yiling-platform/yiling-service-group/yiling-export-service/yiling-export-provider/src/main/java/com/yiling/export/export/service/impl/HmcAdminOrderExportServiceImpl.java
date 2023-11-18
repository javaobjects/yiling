package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.HmcAdminOrderBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.api.OrderDetailControlApi;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.enums.HmcDeliverTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcPaymentMethodEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.settlement.enums.InsuranceSettlementStatusEnum;
import com.yiling.hmc.settlement.enums.TerminalSettlementStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 运营后台兑保记录导出
 *
 * @author: yong.zhang
 * @date: 2022/4/11
 */
@Slf4j
@Service("hmcAdminOrderExportService")
public class HmcAdminOrderExportServiceImpl implements BaseExportQueryDataService<OrderPageRequest> {

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    GoodsPurchaseControlApi goodsPurchaseControlApi;

    @DubboReference
    OrderDetailControlApi orderDetailControlApi;

    @DubboReference
    EnterpriseApi enterpriseApi;


    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "对应订单号");
        FIELD.put("thirdConfirmNo", "第三方兑保编号");
        FIELD.put("policyNo", "对应保司保险单号");
        FIELD.put("orderTime", "下单时间");
        FIELD.put("orderStatus", "订单状态");
        FIELD.put("ename", "药品服务终端");
        FIELD.put("paymentMethod", "支付方式");
        FIELD.put("paymentTime", "支付时间/确认时间");
        FIELD.put("totalAmount", "订单额");
        FIELD.put("paymentStatus", "用户支付状态");
        FIELD.put("finishTime", "完成时间");
        FIELD.put("insuranceSettlementAmount", "理赔款结算额");
        FIELD.put("insuranceSettlementStatus", "保司结算状态");
        FIELD.put("terminalSettlementAmount", "终端结算额");
        FIELD.put("terminalSettlementStatus", "终端结算状态");
        FIELD.put("deliverType", "配送方式");
        FIELD.put("orderDetail", "商品明细");
    }

    @Override
    public QueryExportDataDTO queryData(OrderPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<OrderDTO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            log.info("兑保记录导出,请求数据为:[{}]", request);
            //  查询导出的数据填入data
            page = orderApi.pageList(request);
            log.info("兑保记录导出,返回数据为:[{}]", page);
            List<OrderDTO> dtoList = page.getRecords();
            if (CollUtil.isEmpty(dtoList)) {
                continue;
            }
            for (OrderDTO orderDTO : dtoList) {
                HmcAdminOrderBO hmcAdminOrderBO = new HmcAdminOrderBO();
                hmcAdminOrderBO.setOrderNo(orderDTO.getOrderNo());
                hmcAdminOrderBO.setThirdConfirmNo(orderDTO.getThirdConfirmNo());
                hmcAdminOrderBO.setPolicyNo(orderDTO.getPolicyNo());
                hmcAdminOrderBO.setOrderTime(DateUtil.format(orderDTO.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
                hmcAdminOrderBO.setOrderStatus(getOrderStatus(orderDTO.getOrderStatus()));
                hmcAdminOrderBO.setEname(orderDTO.getEname());
                hmcAdminOrderBO.setPaymentMethod(getPaymentMethod(orderDTO.getPaymentMethod()));
                hmcAdminOrderBO.setPaymentTime(DateUtil.format(orderDTO.getPaymentTime(), "yyyy-MM-dd HH:mm:ss"));
                hmcAdminOrderBO.setTotalAmount(orderDTO.getTotalAmount());
                hmcAdminOrderBO.setPaymentStatus(getPaymentStatus(orderDTO.getPaymentStatus()));
                hmcAdminOrderBO.setFinishTime(DateUtil.format(orderDTO.getFinishTime(), "yyyy-MM-dd HH:mm:ss"));
                hmcAdminOrderBO.setInsuranceSettlementAmount(orderDTO.getInsuranceSettleAmount());
                hmcAdminOrderBO.setInsuranceSettlementStatus(getInsuranceSettlementStatus(orderDTO.getInsuranceSettleStatus()));
                hmcAdminOrderBO.setTerminalSettlementAmount(orderDTO.getTerminalSettleAmount());
                hmcAdminOrderBO.setTerminalSettlementStatus(getTerminalSettlementStatus(orderDTO.getTerminalSettleStatus()));
                hmcAdminOrderBO.setDeliverType(getDeliverType(orderDTO.getDeliverType()));

                List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.listByOrderId(orderDTO.getId());

                // 管控信息
                List<OrderDetailControlBO> orderDetailControlBOS = orderDetailControlApi.listByOrderIdAndSellSpecificationsIdList(orderDTO.getId(), null);
                Map<Long, OrderDetailControlBO> detailControlBOMap = orderDetailControlBOS.stream().collect(Collectors.toMap(OrderDetailControlBO::getSellSpecificationsId, e -> e, (k1, k2) -> k1));

                StringBuilder sb = new StringBuilder();
                for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
                    sb.append("商品名称：").append(orderDetailDTO.getGoodsName()).append("，数量：").append(orderDetailDTO.getGoodsQuantity()).append("，保司结算单价：").append(orderDetailDTO.getSettlePrice()).append("，药品供应商结算单价：").append(orderDetailDTO.getTerminalSettlePrice()).append("，管控采购渠道：");
                    OrderDetailControlBO detailControlBO = detailControlBOMap.get(orderDetailDTO.getSellSpecificationsId());
                    if (null != detailControlBO) {
                        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(detailControlBO.getEidList());
                        List<String> channelNameList = enterpriseDTOS.stream().map(EnterpriseDTO::getName).collect(Collectors.toList());
                        sb.append(getChannelName(channelNameList));
                    }
                    sb.append("\n");
                }
                hmcAdminOrderBO.setOrderDetail(sb.toString());
                Map<String, Object> dataPojo = BeanUtil.beanToMap(hmcAdminOrderBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("兑保记录导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public OrderPageRequest getParam(Map<String, Object> map) {
        OrderPageRequest request = PojoUtils.map(map, OrderPageRequest.class);
        String orderStatusStr = map.getOrDefault("orderStatusStr", "").toString();
        if (StringUtils.isNotBlank(orderStatusStr)) {
            List<Integer> idList = new ArrayList<>();
            String[] split = orderStatusStr.split(",");
            for (String str : split) {
                idList.add(Integer.parseInt(str));
            }
            request.setOrderStatusList(idList);
        }
        return request;
    }

    private String getOrderStatus(Integer orderStatus) {
        // 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
        HmcOrderStatusEnum hmcOrderStatusEnum = HmcOrderStatusEnum.getByCode(orderStatus);
        if (null == hmcOrderStatusEnum) {
            return "---";
        }
        return hmcOrderStatusEnum.getName();
    }

    private String getPaymentMethod(Integer paymentMethod) {
        // 支付方式:1-保险理赔结算
        HmcPaymentMethodEnum hmcPaymentMethodEnum = HmcPaymentMethodEnum.getByCode(paymentMethod);
        if (null == hmcPaymentMethodEnum) {
            return "---";
        }
        return hmcPaymentMethodEnum.getName();
    }

    private String getPaymentStatus(Integer paymentStatus) {
        // 支付状态:1-未支付/2-已支付/3-已退款/4-部分退款
        HmcPaymentStatusEnum hmcPaymentStatusEnum = HmcPaymentStatusEnum.getByCode(paymentStatus);
        if (null == hmcPaymentStatusEnum) {
            return "---";
        }
        return hmcPaymentStatusEnum.getName();
    }

    private String getInsuranceSettlementStatus(Integer insuranceSettlementStatus) {
        // 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
        InsuranceSettlementStatusEnum insuranceSettlementStatusEnum = InsuranceSettlementStatusEnum.getByCode(insuranceSettlementStatus);
        if (null == insuranceSettlementStatusEnum) {
            return "---";
        }
        return insuranceSettlementStatusEnum.getName();
    }

    private String getTerminalSettlementStatus(Integer terminalSettlementStatus) {
        // 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
        TerminalSettlementStatusEnum terminalSettlementStatusEnum = TerminalSettlementStatusEnum.getByCode(terminalSettlementStatus);
        if (null == terminalSettlementStatusEnum) {
            return "---";
        }
        return terminalSettlementStatusEnum.getName();
    }

    private String getDeliverType(Integer deliverType) {
        // 配送方式
        HmcDeliverTypeEnum hmcDeliverTypeEnum = HmcDeliverTypeEnum.getByCode(deliverType);
        if (null == hmcDeliverTypeEnum) {
            return "---";
        }
        return hmcDeliverTypeEnum.getName();
    }

    private String getChannelName(List<String> channelNameList) {
        return StringUtils.join(channelNameList, ",");
    }
}
